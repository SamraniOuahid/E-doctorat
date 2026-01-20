package com.example.demo.candidat.controller;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.repository.CandidatRepository;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Temporary endpoint to fix missing candidat profiles
 * DELETE THIS AFTER FIXING THE ISSUE
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminFixController {

    private final UserRepository userRepository;
    private final CandidatRepository candidatRepository;

    @PostMapping("/fix-candidat/{email}")
    public ResponseEntity<String> fixCandidatProfile(@PathVariable String email) {
        // Find user
        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // Check if candidat already exists
        if (candidatRepository.findByUser_Email(email).isPresent()) {
            return ResponseEntity.ok("Candidat already exists for: " + email);
        }

        // Create candidat
        Candidat candidat = new Candidat();
        candidat.setEmail(email);
        candidat.setPassword(user.getPassword());
        candidat.setUser(user);

        candidatRepository.save(candidat);

        return ResponseEntity.ok("Candidat profile created for: " + email);
    }
}
