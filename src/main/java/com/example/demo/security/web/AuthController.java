package com.example.demo.security.web;

import com.example.demo.security.dto.AuthenticationRequest;
import com.example.demo.security.dto.AuthenticationResponse;
import com.example.demo.security.oauth2.CustomOAuth2User;
import com.example.demo.security.service.AuthService;
import com.example.demo.security.user.Role;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    /**
     * Retourne l'utilisateur actuellement connecté.
     * Supporte:
     * - Google OIDC (DefaultOidcUser)
     * - CustomOAuth2User (si tu l'utilises)
     * - JWT/local (authentication.getName())
     *
     * - Si connecté : 200 + JSON (email, nom, roles...)
     * - Sinon : 401
     */
    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();

        String email = null;
        String fullName = null;
        String pictureUrl = null;
        boolean emailVerified = false;
        String provider = "local";

        // 1) Your custom wrapper (if used)
        if (principal instanceof CustomOAuth2User cu) {
            email = cu.getEmail();
            fullName = cu.getFullName();
            pictureUrl = cu.getAttribute("picture");
            Boolean ev = cu.getAttribute("email_verified");
            emailVerified = ev != null && ev;
            provider = "google";
        }
        // 2) Google OIDC default principal
        else if (principal instanceof OidcUser oidc) {
            email = oidc.getEmail(); // from OIDC claims
            fullName = oidc.getFullName(); // name
            pictureUrl = oidc.getPicture(); // picture
            Boolean ev = oidc.getEmailVerified();
            emailVerified = ev != null && ev;
            provider = "google";
        }
        // 3) Generic OAuth2 user (non-OIDC providers)
        else if (principal instanceof OAuth2User ou) {
            email = (String) ou.getAttributes().get("email");
            fullName = (String) ou.getAttributes().get("name");
            pictureUrl = (String) ou.getAttributes().get("picture");
            Object ev = ou.getAttributes().get("email_verified");
            emailVerified = Boolean.TRUE.equals(ev);
            provider = "oauth2";
        }
        // 4) JWT/local user (often auth.getName() = email)
        else {
            email = authentication.getName();
        }

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Load roles from DB (best proof backend <-> DB + consistent roles)
        UserAccount userAccount = userRepository.findByEmail(email).orElse(null);

        Set<String> roles = (userAccount == null || userAccount.getRoles() == null)
                ? Set.of()
                : userAccount.getRoles()
                        .stream()
                        .map(Role::name)
                        .collect(Collectors.toSet());

        if (fullName == null && userAccount != null) {
            fullName = userAccount.getFullName();
        }

        CurrentUserResponse body = new CurrentUserResponse(
                email,
                fullName,
                roles,
                provider,
                pictureUrl,
                emailVerified,
                Instant.now());

        return ResponseEntity.ok(body);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        System.out.println("AuthController received login request for: " + request.getEmail());
        try {
            return ResponseEntity.ok(authService.authenticate(request));
        } catch (Exception e) {
            System.out.println("AuthController login error: " + e.getMessage());
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody com.example.demo.security.dto.RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Logout: invalide la session Spring Security (ne déconnecte pas Google du
     * navigateur).
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.logout();
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email vérifié avec succès !");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam("email") String email) {
        authService.resendVerificationEmail(email);
        return ResponseEntity.ok("Si le compte existe et n'est pas activé, un email a été envoyé.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok("Si le compte existe, un email de réinitialisation a été envoyé.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Mot de passe réinitialisé avec succès !");
    }

    /**
     * DTO returned by /api/auth/me
     */
    public record CurrentUserResponse(
            String email,
            String fullName,
            Set<String> roles,
            String provider,
            String pictureUrl,
            boolean emailVerified,
            Instant serverTime) {
    }
}
