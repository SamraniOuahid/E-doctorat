package com.example.demo.security.web;

import com.example.demo.security.oauth2.CustomOAuth2User;
import com.example.demo.security.user.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.demo.security.dto.AuthenticationRequest;
import com.example.demo.security.dto.AuthenticationResponse;
import com.example.demo.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Retourne l'utilisateur actuellement connecté (via Google OAuth2).
     *
     * - Si l'utilisateur est connecté : 200 + JSON avec email, nom, rôles, etc.
     * - Si personne n'est connecté : 401 (UNAUTHORIZED)
     */
    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me(
            @AuthenticationPrincipal CustomOAuth2User user) {
        if (user == null) {
            // Personne connectée → 401 pour que le front sache qu'il doit rediriger vers
            // login
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Rôles sous forme de chaîne ("PROFESSEUR", "DIRECTEUR_LABO", etc.)
        Set<String> roles = user.getUserAccount().getRoles()
                .stream()
                .map(Role::name)
                .collect(Collectors.toSet());

        // Récupération de quelques infos brutes de Google (optionnel)
        String pictureUrl = user.getAttribute("picture"); // URL avatar Google
        Boolean emailVerified = user.getAttribute("email_verified"); // peut être null
        String provider = "google";

        CurrentUserResponse body = new CurrentUserResponse(
                user.getEmail(),
                user.getFullName(),
                roles,
                provider,
                pictureUrl,
                emailVerified != null ? emailVerified : false,
                Instant.now() // juste pour debug / info
        );

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
     * Endpoint de logout.
     *
     * - Invalide la session Spring Security (mais NE DÉCONNECTE PAS le compte
     * Google dans le navigateur).
     * - À appeler depuis le front (POST /api/auth/logout).
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // Invalide l'authentification Spring Security
        request.logout();
        // Optionnel : invalidation de la session HTTP
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        // Pas de contenu à retourner
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
     * DTO retourné par /api/auth/me
     * (forme propre pour que le front consomme facilement).
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
