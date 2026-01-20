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
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }
}
