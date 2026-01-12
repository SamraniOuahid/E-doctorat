package com.example.demo.security.dto;

public record RegisterRequest(
        String nom,
        String prenom,
        String email,
        String password,
        String role // ex: "CANDIDAT"
) {}