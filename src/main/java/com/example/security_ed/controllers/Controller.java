/* 13 November 2024
Spring Security: Spring Security + REST + JWT
Александр Фисунов
*/

package com.example.security_ed.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class Controller {

    @GetMapping("/unsecured")
    public String unsecuredData(){
        return "Unsecured data";
    }

    @GetMapping("/secured")
    public String securedData(){
        return "Secured data";
    }

    @GetMapping("/admin")
    public String adminData(){
        return "Admin data";
    }

    // Поскольку нужно печатать инфо о мользователе, нужно понимать , а с кем имеем дело
    // С помощь СПРИНГА можем инжектить текушего пользователя - Principal
    @GetMapping("/info")
    public String userData(Principal principal){
        // В более сложном варианте можем из базы достать пользователя, по нему что-то отпечатать
        // Principal - это хорошо пе нему ЮЗЕРА найдем
        return principal.getName();
    }
}
// Контроллер есть! Дальше нужны сущности
