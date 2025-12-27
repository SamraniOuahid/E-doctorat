package com.example.demo.security.web;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.security.dto.RegisterDto;
import com.example.demo.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Candidat register(@RequestBody RegisterDto dto) {
        return authService.registerCandidat(dto.email(), dto.password());
    }
}
