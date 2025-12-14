package com.example.demo.candidat.model;

import com.example.demo.professeur.model.Sujet; // Import the Sujet model from the professeur package
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data // Generates Getters, Setters, toString, etc. (Requires Lombok)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidat_choix") // This creates a new table in your DB
public class CandidatChoix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We store the ID of the candidate directly
    // This avoids complex circular dependencies with the main Candidat entity
    @Column(name = "candidat_id", nullable = false)
    private Long candidatId;

    // Relationship: Many choices can point to one Sujet
    @ManyToOne
    @JoinColumn(name = "sujet_id", nullable = false)
    private Sujet sujet;

    // Optional: It is good practice to know WHEN they applied
    @Column(name = "date_choix")
    private LocalDateTime dateChoix = LocalDateTime.now();
}