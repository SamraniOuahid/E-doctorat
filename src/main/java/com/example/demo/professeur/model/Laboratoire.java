package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_laboratoire")
@Data
@NoArgsConstructor
public class Laboratoire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String nomLaboratoire;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(length = 100)
    private String pathImage;

    @Column(length = 255)
    private String initial;

    // ced_id -> professeur_ced.id
    @ManyToOne
    @JoinColumn(name = "ced_id", referencedColumnName = "id", nullable = false)
    private Ced ced;

    // directeur_id -> professeur_professeur.id
    @ManyToOne
    @JoinColumn(name = "directeur_id", referencedColumnName = "id", nullable = false)
    private ProfesseurModel directeur;

    // etablissement_id -> professeur_etablissement.idEtablissement
    @ManyToOne
    @JoinColumn(
            name = "etablissement_id",
            referencedColumnName = "idEtablissement",
            nullable = false
    )
    private Etablissement etablissement;
}
