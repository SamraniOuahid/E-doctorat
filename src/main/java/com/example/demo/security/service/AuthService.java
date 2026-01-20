
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

import com.example.demo.security.user.*;

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
        // public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // // 1. Authentifier via AuthenticationManager
        // authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(
        // request.email(),
        // request.password()
        // )
        // );
        //
        // // 2. Récupérer l'utilisateur depuis la BDD
        // var user = repository.findByEmail(request.email())
        // .orElseThrow();
        //
        // // 3. Convertir en UserDetails pour le JWT
        // UserDetails userDetails = createSpringSecurityUser(user);
        //
        // // 4. Générer le token
        // var jwtToken = jwtService.generateToken(userDetails);
        // return new AuthenticationResponse(jwtToken);
        // }

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
                UserAccount user = userRepository.findByVerificationToken(token)
                                .orElseThrow(() -> new RuntimeException("Token invalide ou expiré"));

                user.setEnabled(true);
                user.setVerificationToken(null);
                userRepository.save(user);
        }

        public void resendVerificationEmail(String email) {
                userRepository.findByEmail(email).ifPresent(user -> {
                        if (user.isEnabled()) {
                                return; // Already verified, do nothing (security)
                        }
                        // Generate new token if needed or reuse existing? Better generate new to
                        // prolong validity if we had expiration
                        String token = java.util.UUID.randomUUID().toString();
                        user.setVerificationToken(token);
                        userRepository.save(user);
                        emailService.sendVerificationEmail(user.getEmail(), token);
                });
        }

        // ==========================================
        // 4. PASSWORD RESET
        // ==========================================
        @Transactional
        public void forgotPassword(String email) {
                UserAccount user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec cet email"));

                // Generate token
                String token = java.util.UUID.randomUUID().toString();
                user.setResetPasswordToken(token);
                user.setResetPasswordTokenExpiry(java.time.LocalDateTime.now().plusMinutes(15));

                userRepository.save(user); // Important: UserAccount must be updated

                emailService.sendPasswordResetEmail(user.getEmail(), token);
        }

        @Transactional
        public void resetPassword(String token, String newPassword) {
                UserAccount user = userRepository.findByResetPasswordToken(token)
                                .orElseThrow(() -> new RuntimeException("Token invalide"));

                if (user.getResetPasswordTokenExpiry().isBefore(java.time.LocalDateTime.now())) {
                        throw new RuntimeException("Le lien a expiré");
                }

                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetPasswordToken(null);
                user.setResetPasswordTokenExpiry(null);

                userRepository.save(user);
        }
}