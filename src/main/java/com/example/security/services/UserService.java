package com.example.security.services;

import com.example.security.entities.User;
import com.example.security.repositories.RoleRepository;
import com.example.security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    // UserRepository, RoleRepository Ето у нас БИНЫ нам их нужно заинжектить. нам их нужно заинжектить
    // писать сетеры или кастомний конструктор не хочатся, поетому напишим что ето final поля и ЛМБОК
    // @RequiredArgsConstructor згенерит для всех ФИНАЛ полей конструктор
    private final UserRepository userRepository;
    // по хорошему к RoleRepository я должен создать сервис, и сюда инжектить сервис
    // (маленькое упрощение)
    private final RoleRepository roleRepository;

    // для начала напишым обертку для метода репозитория
    public Optional<User> findByUsername(String username){
        return userRepository.findByUserName(username);
    }

    @Override
    @Transactional // Скажем что ето транзакционный метод
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Нам здесь понадобиться в базе найти пользователя
        // СПРИНГ не знает что у нас является источником пользователей.
        // Ми в МАПЕ, ЛИСТЕ храним можем в файле. Но в нашем случае храним пользователей в БД
        // Ми пишим как достанем юзера из БД и преобразуэм к UserDetails к тому виду которий понимает СПРИНГ
        //User user = findByUsername(username) - здесь может пойти что-то не так, напр. некоректное имя
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        // Преобразуем ЮЗЕРА
        // Создадим СПРГИНГОВОГО ЮЗЕРА
        return new org.springframework.security.core.userdetails.User(
                // Что нужно росказать о пользователе? Как зовут, пароль, список ролей
                user.getUsername(),
                user.getPassword(),
                // СПРИНГ не знает что такое наши РОЛИ, у него есть такая штука Granted Authority Versus Role
                // из ЮЗЕРА достать роли и ОТМАПИТЬ к тому виду которий понимает СПРИНГ
                // new SimpleGrantedAuthority() -- ето строка обернутая в хитрый тип
                user.getRoles().stream().map(role ->
                        new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }
    // На будущее может захотим сохранять нового пользователя в БД
    public void createNewUser(User user){
        // Здесь можно било бы написать механизм проверки что такой пользователь уже существует, и кидать ошибку
        // что нельзя присылать такие данные ( но мы упростим )
        // считаем что у нас хороший пользователь и ему мы зададим базовий список ролей
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        userRepository.save(user);
    }
}
// Теперь можем приступать к конфигурирования нашей безопасности
















