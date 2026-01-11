package com.example.demo.security.service;

import com.example.demo.security.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Set;

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
}
