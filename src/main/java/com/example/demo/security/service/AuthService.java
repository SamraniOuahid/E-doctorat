package com.example.demo.security.service; // Correction du package

import com.example.demo.security.dto.AuthenticationRequest;
import com.example.demo.security.dto.AuthenticationResponse;
import com.example.demo.security.dto.RegisterRequest;
import com.example.demo.security.user.User;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // 1. Création de l'utilisateur avec le Builder (plus propre)
        var user = User.builder()
                .nom(request.nom())         // Ajout du nom
                .prenom(request.prenom())   // Ajout du prénom
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() != null ? request.role() : "CANDIDAT")
                .build();

        // 2. Sauvegarder en base
        repository.save(user);

        // 3. Convertir en UserDetails pour le JWT
        // (Car ton entité User n'est pas forcément un UserDetails Spring Security)
        UserDetails userDetails = createSpringSecurityUser(user);

        // 4. Générer le token
        var jwtToken = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. Authentifier via AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // 2. Récupérer l'utilisateur depuis la BDD
        var user = repository.findByEmail(request.email())
                .orElseThrow();

        // 3. Convertir en UserDetails pour le JWT
        UserDetails userDetails = createSpringSecurityUser(user);

        // 4. Générer le token
        var jwtToken = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwtToken);
    }

    // Méthode utilitaire pour transformer ton Entité User en UserDetails (Spring Security)
    private UserDetails createSpringSecurityUser(User user) {
        String roleName = user.getRole().startsWith("ROLE_") ? user.getRole() : "ROLE_" + user.getRole();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(roleName))
        );
    }
}