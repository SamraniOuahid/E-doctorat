package com.example.demo.security.oauth2;

import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // We can just use the UserRepository we saw in AuthService
    // Need to verify the name of the repo bean. In AuthService it was
    // 'UserRepository'.
    // Let's check imports.

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 1) load raw user from Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2) synchronize with DB + enforce @usmba.ac.ma
        UserAccount user = loadOrCreateFromGoogle(oAuth2User);

        // 3) wrap into our custom user (with roles)
        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private UserAccount loadOrCreateFromGoogle(OAuth2User googleUser) {
        String email = googleUser.getAttribute("email");
        String name = googleUser.getAttribute("name");
        String sub = googleUser.getAttribute("sub"); // ID unique Google

        // Restriction domaine USMBA
        if (email == null || !email.toLowerCase().endsWith("@usmba.ac.ma")) {
            throw new RuntimeException("Email doit être @usmba.ac.ma");
        }

        return userRepository.findByEmail(email)
                .map(existing -> updateExisting(existing, name, sub))
                .orElseGet(() -> createNew(email, name, sub));
    }

    private UserAccount updateExisting(UserAccount user, String name, String sub) {
        // On met à jour le nom si nécessaire et on lie le compte à Google
        user.setFullName(name);
        user.setProvider(com.example.demo.security.user.AuthProvider.GOOGLE);
        user.setProviderId(sub);
        return userRepository.save(user);
    }

    private UserAccount createNew(String email, String name, String sub) {
        // Création d'un nouvel utilisateur par défaut PROFESSEUR
        // NB: Le profil complet (Etablissement, Grade, etc.) devra être complété plus
        // tard
        UserAccount user = new UserAccount();
        user.setEmail(email);
        user.setFullName(name);
        user.setRoles(new java.util.HashSet<>(
                java.util.Collections.singletonList(com.example.demo.security.user.Role.PROFESSEUR)));
        user.setPassword(""); // Pas de mot de passe car via Google
        user.setProvider(com.example.demo.security.user.AuthProvider.GOOGLE);
        user.setProviderId(sub);
        user.setEnabled(true); // Google users are verified by default

        return userRepository.save(user);
    }
}
