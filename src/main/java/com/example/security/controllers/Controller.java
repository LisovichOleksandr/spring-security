package com.example.security.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    @GetMapping("/unsecured")
    public String unsecuredData(){
        return "Unsecured data";
    }
}
