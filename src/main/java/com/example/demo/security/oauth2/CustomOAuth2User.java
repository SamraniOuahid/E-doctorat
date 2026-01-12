package com.example.demo.security.oauth2;

import com.example.demo.security.user.Role;
import com.example.demo.security.user.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomOAuth2User implements OAuth2User {

    private final UserAccount user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(UserAccount user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_XXX for each role
        if (user.getRoles() == null) return List.of();

        return user.getRoles().stream()
                .map(Role::name)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();
    }

    @Override
    public String getName() {
        // Unique id from our DB
        return user.getId().toString();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getFullName() {
        return user.getFullName();
    }

    public UserAccount getUserAccount() {
        return user;
    }
}
