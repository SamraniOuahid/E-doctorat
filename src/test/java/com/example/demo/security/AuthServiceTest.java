package com.example.demo.security;

import com.example.demo.candidat.repository.CandidatRepository;
import com.example.demo.email.EmailService;
import com.example.demo.security.dto.AuthenticationResponse;
import com.example.demo.security.dto.RegisterRequest;
import com.example.demo.security.service.AuthService;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@org.springframework.test.context.TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/edoctorat",
        "spring.datasource.username=root",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.security.oauth2.client.registration.google.client-id=mock-id",
        "spring.security.oauth2.client.registration.google.client-secret=mock-secret",
        "spring.security.oauth2.client.registration.google.scope=email,profile",
        "spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}",
        "spring.security.oauth2.client.provider.google.authorization-uri=https://localhost/auth",
        "spring.security.oauth2.client.provider.google.token-uri=https://localhost/token",
        "spring.security.oauth2.client.provider.google.user-info-uri=https://localhost/userinfo",
        "spring.security.oauth2.client.provider.google.user-name-attribute=sub"
})
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidatRepository candidatRepository; // To clean up if needed/cascade check

    @MockBean
    private EmailService emailService; // Mock email to prevent sending

    @Test
    public void testRegisterWithLocalProvider() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test_provider_fix@test.com");
        request.setPassword("password123");
        request.setFullName("Test User Provider Fix");

        AuthenticationResponse response = authService.register(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());

        UserAccount user = userRepository.findByEmail("test_provider_fix@test.com").orElse(null);
        Assertions.assertNotNull(user);
        Assertions.assertEquals("LOCAL", user.getProvider().name());
    }
}
