package com.example.demo.security;

import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@org.springframework.test.context.TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/edoctorat",
        "spring.datasource.username=root",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.security.oauth2.client.registration.google.client-id=mock",
        "spring.security.oauth2.client.registration.google.client-secret=mock",
        "spring.mail.host=localhost",
        "spring.mail.port=1025",
        "spring.mail.username=test",
        "spring.mail.password=test"
})
public class PasswordResetTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    @Commit // Important to commit the transaction
    public void resetPassword() {
        UserAccount user = userRepository.findByEmail("rachid@gmail.com")
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Resetting password for: " + user.getEmail());
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);
        System.out.println("Password reset successfully.");
    }
}
