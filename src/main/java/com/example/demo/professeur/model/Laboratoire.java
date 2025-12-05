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

    @Column(name = "nomLaboratoire", length = 255, nullable = false)
    private String nomLaboratoire;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "pathImage", length = 100)
    private String pathImage;

    @Column(name = "initial", length = 255)
    private String initial;

    // ced_id -> professeur_ced.id
    @ManyToOne
    @JoinColumn(name = "ced_id", referencedColumnName = "id")
    private Ced ced;

    // directeur_id -> professeur_professeur.id
    @ManyToOne
    @JoinColumn(name = "directeur_id", referencedColumnName = "id")
    private ProfesseurModel directeur;

    // etablissement_id -> professeur_etablissement.id
    @ManyToOne
    @JoinColumn(name = "etablissement_id", referencedColumnName = "id")
    private Etablissement etablissement;
}
