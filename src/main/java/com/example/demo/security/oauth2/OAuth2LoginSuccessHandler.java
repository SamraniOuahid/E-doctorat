package com.example.demo.security.oauth2;

import com.example.demo.security.jwt.JwtService;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private static final String FRONT_CALLBACK = "http://localhost:5173/oauth2/callback";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String email;
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2User cu) {
            email = cu.getEmail();
        } else if (principal instanceof OidcUser oidc) {
            email = oidc.getEmail();
        } else if (principal instanceof OAuth2User ou) {
            email = ou.getAttribute("email");
        } else {
            redirectError(request, response, "UNSUPPORTED_PRINCIPAL");
            return;
        }

        if (email == null) {
            redirectError(request, response, "NO_EMAIL");
            return;
        }

        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after OAuth2"));

        // put roles in JWT
        Map<String, Object> extra = new HashMap<>();
        extra.put("roles", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        String token = jwtService.generateToken(extra, user);

        String targetUrl = UriComponentsBuilder
                .fromUriString(FRONT_CALLBACK)
                .queryParam("token", token)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void redirectError(HttpServletRequest request, HttpServletResponse response, String errorCode)
            throws IOException {
        String url = UriComponentsBuilder
                .fromUriString(FRONT_CALLBACK)
                .queryParam("error", errorCode)
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
