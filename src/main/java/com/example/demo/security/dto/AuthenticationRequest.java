package com.example.demo.security.dto;

public record AuthenticationRequest(
        String email,
        String password
) {}