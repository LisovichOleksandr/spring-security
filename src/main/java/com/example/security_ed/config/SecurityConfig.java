package com.example.security_ed.config;

import com.example.security_ed.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity // @Configuration входит сюда <-
@RequiredArgsConstructor
//Возможность защиты отдельных методов
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    // заинжектим сюда userService
    private final UserService userService;

    // Дальше нужно настроить три вещи: 1 Правила безопасности Где у нас живут пользователи
    // 2) Как хранятся и преобразуются пароли
    // 3) Кто является АУТЕНТИФИКЕЙШЫН МЕНЕДЖЕРОМ?

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        // Теперь мы при настройках будем указывать что пароль у нас хранится именно в таком виде, а не в виде ПЛАИН текста
    }

    // В прошлой версии ми АУТЕНТИФИКЕЙШІН МЕНЕДЖЕР инжектили из интерфейса
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Связываем все во едино, что-би использовался userService, passwordEncoder. Создаем DAOAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    // Все что до сих пор Ето базовые настройки. Теперь настроим правила безопасности т.е
    // К каким ендпоинтам есть доступ, а к каким нету.. Что делать если кто-то постучался в защищенную область
    // и он не прислал токен, и он не просит аутентификацию (ето делается через создание бина).
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf токени выключаем
                .cors(cors -> cors.disable())// cors  аналогично (similarly)
                .authorizeHttpRequests(authorise -> authorise
                        .requestMatchers("/secured").authenticated()
                        .requestMatchers("/info").authenticated()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
        //.and().addFilterBefore()...
        return http.build();
    }
}

/* Обсудим ка работает в REST-е безопасность
* В прошлой версии мы говорили что СПРИНГ понимает вошел ли пользователь если этот пользователь есть
*  в СПРИНГ_СЕКЮРИТИ_КОНТЕКСТ (т.е есть какая-то
* защищенная область "ЕНДПОИНТ", при доступе туда СПРИНГ смотрит если в контексте пользователь лежит значит мы
* его пропускаем, не лежит - не пропускаем) и там пользователь привязывался к JSessionID ( сессии стандартной ,
* которая реализуется через куки)
* Как Будет В РЕСТ?
* В REST никаких стандартных сессий через куки не будет (ми считаем что все что нужно для РЕСТ запроса оно должно жить в етом самом запросе)
*  */

// Дальше там детальный розбор про РЕСТ + JWT token
// создаем пакет utils и клас которий возьмет на себя роботу с токенами JWTTokenUtils





