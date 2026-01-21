package com.example.demo.security.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByVerificationToken(String verificationToken);

    Optional<UserAccount> findByResetPasswordToken(String resetPasswordToken);

    long countByEnabled(boolean enabled);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(u) FROM UserAccount u JOIN u.roles r WHERE r = :role")
    long countByRole(Role role);
}
