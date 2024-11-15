/* Контроллер для Аутентификации, в том числе и регистрации */

package com.example.security_ed.controllers;

import com.example.security_ed.dtos.JwtRequest;
import com.example.security_ed.dtos.JwtResponse;
import com.example.security_ed.exception.AppError;
import com.example.security_ed.services.UserService;
import com.example.security_ed.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    // И ИНЖЕКТИМ все что нам может пригодиться
    private final UserService userService; // Почему? Потому что к нам придет логин, пароль нужно понять кто ето такой,
                                           // и по нему сформировать токен
    private final JwtTokenUtils jwtTokenUtils;  // jwtTokenUtils - инжектьм сюда. Будем собирать токен
    private final AuthenticationManager authenticationManager;

    // Ми говорим:"Присылайте ПОСТ запрос, давайте логин пароль и мы вам токен отдадим".
    @PostMapping("/auth")
    // <?> - Любой может придти
    public ResponseEntity<?> creteAuthToken(@RequestBody JwtRequest authRequest){  // @RequestBody -  на вход прилетает JwtRequest
        // ( ) Как нам могут передать данные о пользователе? Для етого создадим несколько dtos-шек, и указиваем что
        // у нас есть JwtRequest "т.е нам присылают запрос с данными"
        // В ответ мы хотим вернуть токен поетому JwtResponse там-же в dtos
        // 1) - А пришедший логин и пароль он существует или нет? Если да то токен формируем, а нет (мягко уповестить пользователя)
        // что-бы ето вручную не проверять мы инжектим бин которий етим профиссионально занимается AuthenticationManager.
        // Почему мы сами етого не делаем? Потому-что мы не знаем какие там способы аутентификации настроели в КОНФИГАХ.
        try { // например скажет я не знаю кто ето, у них непривильные логин и пароль
            authenticationManager.authenticate( // authenticationManager - проведи пожалуйста аутентифокацию
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e){ // Мы перхвативаем BadCredentialsException
//            ResponseEntity<?> - ? Потому что может вернуться либо ошбка либо результат
//            А Давайте!!! для того чтобы описывать ошибки пропишим (какой-нибуть хороший) dtos exception.AppError.class
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), // json -тело
                    HttpStatus.UNAUTHORIZED);
        } // Если все пройшло хорошо, значит такой пользователь в базеесть, то мы после try-catch двинемся дальше
        // нужно достать userDetails по нему
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());// Берем имя из Jwt Token-a
        // Формируем токен
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
