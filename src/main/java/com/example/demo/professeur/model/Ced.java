package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_ced")
@Data
@NoArgsConstructor
public class Ced {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(length = 100)
    private String pathImage;

    @Column(length = 255)
    private String initiale;

    @Column(length = 255, nullable = false)
    private String titre;

    // directeur_id -> professeur_professeur.id (nullable)
    @ManyToOne
    @JoinColumn(name = "directeur_id", referencedColumnName = "id")
    private ProfesseurModel directeur;
}
