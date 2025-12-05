package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur")
@Data
@NoArgsConstructor
public class ProfesseurModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cin;

    private String telProfesseur;

    private String pathPhoto;

    private String grade;

    private String numSOM;

    @Column(nullable = false)
    private int nombreEncadrer;

    @Column(nullable = false)
    private int nombreProposer;

    // etablissement_id -> professeur_etablissement.idEtablissement
    @ManyToOne
    @JoinColumn(
            name = "etablissement_id",
            referencedColumnName = "idEtablissement",
            nullable = false
    )
    private Etablissement etablissement;

    // labo_id -> professeur_laboratoire.id  (nullable)
    @ManyToOne
    @JoinColumn(name = "labo_id", referencedColumnName = "id")
    private Laboratoire laboratoire;

    // user_id -> auth_user.id  (we keep only the FK, no relation entity)
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
