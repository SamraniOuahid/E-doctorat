package com.example.demo.security.service; // Correction du package

import com.example.demo.security.user.*;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Set;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public UserAccount loadOrCreateFromGoogle(OAuth2User googleUser) {
        String email = googleUser.getAttribute("email");
        String name  = googleUser.getAttribute("name");
        String sub   = googleUser.getAttribute("sub");

        // 1) Restriction domaine usmba
        if (email == null || !email.toLowerCase().endsWith("@usmba.ac.ma")) {
            throw new RuntimeException("Email doit être @usmba.ac.ma");
        }

        return userRepository.findByEmail(email)
                .map(existing -> updateExisting(existing, name, sub))
                .orElseGet(() -> createNew(email, name, sub));
    }

    private UserAccount updateExisting(UserAccount user, String name, String sub) {
        user.setFullName(name);
        user.setProvider(AuthProvider.GOOGLE);
        user.setProviderId(sub);
        return userRepository.save(user);
    }

    private UserAccount createNew(String email, String name, String sub) {
        // TODO: plus tard, déterminer les rôles à partir des tables professeur/directeur
        UserAccount user = UserAccount.builder()
                .email(email)
                .fullName(name)
                .provider(AuthProvider.GOOGLE)
                .providerId(sub)
                .roles(Set.of())  // pour le moment, pas de rôle automatique
                .build();

        return userRepository.save(user);
    }

//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        // 1. Authentifier via AuthenticationManager
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.email(),
//                        request.password()
//                )
//        );
//
//        // 2. Récupérer l'utilisateur depuis la BDD
//        var user = repository.findByEmail(request.email())
//                .orElseThrow();
//
//        // 3. Convertir en UserDetails pour le JWT
//        UserDetails userDetails = createSpringSecurityUser(user);
//
//        // 4. Générer le token
//        var jwtToken = jwtService.generateToken(userDetails);
//        return new AuthenticationResponse(jwtToken);
//    }

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