package com.example.physical_activity_project;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456"; // contraseña que quieras para el usuario
        String hashed = encoder.encode(rawPassword);
        System.out.println(hashed);
    }
}

