package com.example.demo.security.oauth2;

import com.example.demo.security.user.AuthProvider;
import com.example.demo.security.user.Role;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User googleUser = super.loadUser(userRequest);

        String email = googleUser.getAttribute("email");
        String name = googleUser.getAttribute("name");
        String sub  = googleUser.getAttribute("sub");

        if (email == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("NO_EMAIL"), "Google did not return email");
        }

        // Domain restriction
        if (!email.toLowerCase().endsWith("@usmba.ac.ma")) {
            throw new OAuth2AuthenticationException(new OAuth2Error("EMAIL_DOMAIN_NOT_ALLOWED"),
                    "Email must be @usmba.ac.ma");
        }

        // IMPORTANT: must already exist in DB (staff pre-created)
        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OAuth2AuthenticationException(
                        new OAuth2Error("NOT_REGISTERED"),
                        "This email is not authorized in the system"));

        // staff-only: candidates must NOT login by Google
        if (user.getProvider() == AuthProvider.LOCAL) {
            throw new OAuth2AuthenticationException(new OAuth2Error("GOOGLE_NOT_ALLOWED_FOR_LOCAL_ACCOUNT"),
                    "This account must login with email/password");
        }

        if (!hasAnyStaffRole(user.getRoles())) {
            throw new OAuth2AuthenticationException(new OAuth2Error("NOT_STAFF"),
                    "This account is not staff");
        }

        // update profile from Google (safe)
        user.setProvider(AuthProvider.GOOGLE);
        user.setProviderId(sub);
        if (name != null) user.setFullName(name);
        user.setEnabled(true);

        user = userRepository.save(user);

        return new CustomOAuth2User(user, googleUser.getAttributes());
    }

    private boolean hasAnyStaffRole(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) return false;
        return roles.contains(Role.PROFESSEUR)
                || roles.contains(Role.DIRECTEUR_LABO)
                || roles.contains(Role.DIRECTEUR_CED)
                || roles.contains(Role.SCOLARITE);
    }
}
