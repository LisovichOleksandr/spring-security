package com.example.security_ed;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
    // Приватний ключ
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256); // Размер ключа для ES256

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        // Теперь privateKey можно использовать в signWith
        System.out.println("Private Key: " + privateKey);
    }
}
