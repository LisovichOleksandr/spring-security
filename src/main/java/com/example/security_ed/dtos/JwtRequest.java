package com.example.security_ed.dtos;

import lombok.Data;

@Data
public class JwtRequest {
    // Что нужно для формирования токена?
    private String username;
    private String password;
}

// Все "нам (т.е AuthController) такой обьект присылайте и мы что нибуть с ним сотворим"
