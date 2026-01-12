package com.example.demo.security.oauth2;

import com.example.demo.security.service.AuthService;
import com.example.demo.security.user.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthService authService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 1) load raw user from Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2) synchronize with DB + enforce @usmba.ac.ma
        UserAccount user = authService.loadOrCreateFromGoogle(oAuth2User);

        // 3) wrap into our custom user (with roles)
        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
