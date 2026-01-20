package com.example.demo.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String FRONT_CALLBACK = "http://localhost:5173/oauth2/callback";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String code = "OAUTH2_FAILED";
        if (exception instanceof OAuth2AuthenticationException oae) {
            code = oae.getError().getErrorCode();
        }

        String url = UriComponentsBuilder
                .fromUriString(FRONT_CALLBACK)
                .queryParam("error", code)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
