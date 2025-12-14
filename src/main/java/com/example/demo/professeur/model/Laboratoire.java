package com.example.demo.professeur.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // IMPORT THIS
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

    // FIX 1: Stop loop with CED
    @ManyToOne
    @JoinColumn(name = "ced_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("laboratoires")
    private Ced ced;

    // FIX 2: Stop loop with Directeur (Critical!)
    @ManyToOne
    @JoinColumn(name = "directeur_id", referencedColumnName = "id", nullable = false)
    // Don't load the director's lab or subjects again
    @JsonIgnoreProperties({"laboratoire", "etablissement", "sujets"})
    private ProfesseurModel directeur;

    // FIX 3: Stop loop with Etablissement
    @ManyToOne
    @JoinColumn(name = "etablissement_id", referencedColumnName = "idEtablissement", nullable = false)
    @JsonIgnoreProperties("laboratoires")
    private Etablissement etablissement;
}