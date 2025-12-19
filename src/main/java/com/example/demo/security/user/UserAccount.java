package com.example.demo.security.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;   // plus tard: hashé (BCrypt)

    @Column(nullable = false)
    private String role;       // ex: "ROLE_CANDIDAT"

    // relation 1–1 avec Candidat (côté candidat)
}
