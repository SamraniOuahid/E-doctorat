package com.example.demo.security.service;

import com.example.demo.security.dto.AuthenticationRequest;
import com.example.demo.security.dto.AuthenticationResponse;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.security.user.AuthProvider;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final com.example.demo.candidat.repository.CandidatRepository candidatRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final com.example.demo.email.EmailService emailService;

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

        // 3. UserAccount implements UserDetails now
        // 4. Générer le token
        // 4. Générer le token avec les rôles
        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("roles", user.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.toList()));
        String jwtToken = jwtService.generateToken(extraClaims, user);

        return new AuthenticationResponse(jwtToken);
    }

    // ==========================================
    // 1.1 REGISTER CLASSIQUE
    // ==========================================
    @Transactional
    public AuthenticationResponse register(com.example.demo.security.dto.RegisterRequest request) {
        // 1. Vérifier si l'email existe déjà
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        // 2. Créer le UserAccount
        UserAccount user = new UserAccount();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new java.util.HashSet<>(
                java.util.Collections.singletonList(com.example.demo.security.user.Role.CANDIDAT)));
        user.setProvider(AuthProvider.LOCAL); // Local provider
        user.setEnabled(false); // Enable only after verification
        String token = java.util.UUID.randomUUID().toString();
        user.setVerificationToken(token);

        UserAccount savedUser = userRepository.save(user);

        // 3. Créer le profil Candidat associé (si Rôle est CANDIDAT, par défaut ici)
        com.example.demo.candidat.model.Candidat candidat = new com.example.demo.candidat.model.Candidat();
        candidat.setEmail(request.getEmail());
        candidat.setPassword(savedUser.getPassword()); // redondant mais présent dans entity Candidat
        candidat.setUser(savedUser);

        // Initialiser les champs obligatoires du candidat si besoin
        // candidat.setNomCandidatAr(request.getFullName());

        candidatRepository.save(candidat);

        // 4. Générer le token
        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("roles", savedUser.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.toList()));
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