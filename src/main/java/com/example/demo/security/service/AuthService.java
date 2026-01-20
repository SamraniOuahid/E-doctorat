package com.example.demo.security.service;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.repository.CandidatRepository;
import com.example.demo.email.EmailService;
import com.example.demo.security.dto.AuthenticationRequest;
import com.example.demo.security.dto.AuthenticationResponse;
import com.example.demo.security.dto.RegisterRequest;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.security.user.AuthProvider;
import com.example.demo.security.user.Role;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CandidatRepository candidatRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    // ==========================================
    // 1. LOGIN CLASSIQUE (Email + Password)
    // ==========================================
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. Authentifier via Spring Security (vérifie le mot de passe)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // 2. Récupérer l'utilisateur depuis la BDD
        UserAccount user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // 3. Générer le token avec les rôles
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", user.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList()));

        String jwtToken = jwtService.generateToken(extraClaims, user);

        return new AuthenticationResponse(jwtToken);
    }

    // ==========================================
    // 1.1 REGISTER CLASSIQUE
    // ==========================================
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // 1. Vérifier si l'email existe déjà
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        // 2. Créer le UserAccount
        UserAccount user = new UserAccount();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new HashSet<>(java.util.Collections.singletonList(Role.CANDIDAT)));
        user.setProvider(AuthProvider.LOCAL);
        user.setEnabled(false);
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);

        UserAccount savedUser = userRepository.save(user);

        // 3. Créer le profil Candidat associé (si Rôle est CANDIDAT, par défaut ici)
        Candidat candidat = new Candidat();
        candidat.setEmail(request.getEmail());
        candidat.setPassword(savedUser.getPassword());
        candidat.setUser(savedUser);
        candidatRepository.save(candidat);

        // 4. Générer le token
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", savedUser.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList()));
        String jwtToken = jwtService.generateToken(extraClaims, savedUser);

        // 5. Envoyer l'email de vérification
        emailService.sendVerificationEmail(user.getEmail(), token);

        return new AuthenticationResponse(jwtToken);
    }

    // ==========================================
    // 3. EMAIL VERIFICATION
    // ==========================================
    @Transactional
    public void verifyEmail(String token) {
        UserAccount user = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getVerificationToken()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }
}