package com.example.demo.admin.controller;

import com.example.demo.security.user.Role;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Temporary endpoint to create admin user
 * DELETE THIS AFTER CREATING ADMIN
 */
@RestController
@RequestMapping("/api/setup")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SetupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(
            @RequestParam(defaultValue = "admin@edoctorat.ma") String email,
            @RequestParam(defaultValue = "admin123") String password) {

        // Check if user exists
        if (userRepository.findByEmail(email).isPresent()) {
            // Update existing user to admin
            UserAccount user = userRepository.findByEmail(email).get();
            Set<Role> roles = new HashSet<>(user.getRoles());
            roles.add(Role.ADMIN);
            user.setRoles(roles);
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok("User updated to ADMIN: " + email);
        }

        // Create new admin user
        UserAccount admin = new UserAccount();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setFullName("Administrateur");
        admin.setEnabled(true);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        admin.setRoles(roles);

        userRepository.save(admin);

        return ResponseEntity.ok("Admin user created: " + email + " / password: " + password);
    }
}
