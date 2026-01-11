package com.example.demo.security.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sec_user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;   // GOOGLE only for now

    private String providerId;       // Google "sub" claim (user id at Google)

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "sec_user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Role activeRole;         // for "changer de rôle" later
}
