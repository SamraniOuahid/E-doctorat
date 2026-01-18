package com.example.demo.security.oauth2;

import com.example.demo.security.jwt.JwtService;
import com.example.demo.security.user.UserAccount;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        // 1. Récupérer le user authentifié (CustomOAuth2User)
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        UserAccount userAccount = oauthUser.getUserAccount();

        // 2. Générer le JWT
        // On peut ajouter des claims extra si besoin (rôle, etc.)
        String jwtToken = jwtService.generateToken(userAccount);

        // 3. Construire l'URL de redirection vers le front
        // Idéalement, l'URL du front devrait être dans application.properties
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth2/callback")
                .queryParam("token", jwtToken)
                .build().toUriString();

        // 4. Rediriger
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
