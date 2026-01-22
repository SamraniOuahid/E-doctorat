package com.example.demo.scolarite.model;

import com.example.demo.professeur.model.Laboratoire;
import com.example.demo.security.user.UserAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scolarite_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scolarite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name = "laboratoire_id", nullable = false)
    private Laboratoire laboratoire;
}
