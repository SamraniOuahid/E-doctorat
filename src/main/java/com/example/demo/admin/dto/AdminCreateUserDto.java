package com.example.demo.admin.dto;

import com.example.demo.security.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateUserDto {
    private String email;
    private String password;
    private String fullName;
    private Set<Role> roles;
    private boolean enabled;
}
