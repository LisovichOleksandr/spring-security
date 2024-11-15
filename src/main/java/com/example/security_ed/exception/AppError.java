package com.example.security_ed.exception;

import lombok.Data;

import java.util.Date;

@Data
public class AppError {
    private int status;
    private String message; // Почему ето сообщение ему показываем
    private Date timestamp;  // Что-бы там время жило

    // Указать статус, сообщение, timestamp - ето просто текущее время
    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
