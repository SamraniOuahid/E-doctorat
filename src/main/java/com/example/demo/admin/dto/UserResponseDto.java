package com.example.demo.admin.dto;

import com.example.demo.security.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for user data in admin responses.
 * Excludes sensitive information like passwords.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String fullName;
    private Set<Role> roles;
    private boolean enabled;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
