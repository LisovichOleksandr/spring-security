/* Мы захотим в будущем регистрировать пользователя */

package com.example.security_ed.dtos;

import lombok.Data;


@Data
public class RegistrationUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;

}
