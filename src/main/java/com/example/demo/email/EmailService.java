package com.example.demo.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public void sendVerificationEmail(String to, String token) {
        String subject = "Vérification de votre compte E-Doctorat";
        String confirmationUrl = frontendUrl + "/verify-email?token=" + token;

        String htmlContent = "<p>Bonjour,</p>"
                + "<p>Merci de vous être inscrit sur E-Doctorat.</p>"
                + "<p>Veuillez cliquer sur le lien ci-dessous pour activer votre compte :</p>"
                + "<p><a href=\"" + confirmationUrl + "\">Activer mon compte</a></p>"
                + "<p>Si vous n'avez pas créé de compte, veuillez ignorer cet email.</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
            System.out.println("INFO - Verification email sent to: " + to);
        } catch (Exception e) {
            System.err.println("ERROR - Failed to send verification email to " + to + ": " + e.getMessage());
            System.out.println("DEBUG - Verification URL (Manual copy): " + confirmationUrl);
            // We don't rethrow to avoid breaking the registration transaction
        }
    }

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Réinitialisation de votre mot de passe - E-Doctorat";
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        System.out.println("DEBUG - Password Reset URL: " + resetUrl);

        String htmlContent = "<p>Bonjour,</p>"
                + "<p>Vous avez demandé la réinitialisation de votre mot de passe.</p>"
                + "<p>Veuillez cliquer sur le lien ci-dessous pour changer votre mot de passe :</p>"
                + "<p><a href=\"" + resetUrl + "\">Réinitialiser mon mot de passe</a></p>"
                + "<p>Ce lien expirera dans 15 minutes.</p>"
                + "<p>Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer cet email.</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("INFO - Password reset email sent to: " + to);
        } catch (Exception e) {
            System.err.println("ERROR - Failed to send password reset email to " + to + ": " + e.getMessage());
            System.out.println("DEBUG - Reset URL (Manual copy): " + resetUrl);
            // We don't rethrow to avoid breaking the process
        }
    }
}
