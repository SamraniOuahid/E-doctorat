package com.example.demo.professeur.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "professeur_formationdoctorale")
@Data
@NoArgsConstructor
public class FormationDoctorale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String pathImage;

    @Column(length = 255)
    private String initiale;

    @Column(length = 255, nullable = false)
    private String titre;

    // POSTGRESQL FIX
    @Lob
    @Column(name = "axeDeRecherche")
    private String axeDeRecherche;

    @Column(name = "dateAccreditation")
    private Date dateAccreditation;

    // FIX 1: Stop infinite loop with CED
    @ManyToOne
    @JoinColumn(name = "ced_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("formationsDoctorales")
    private Ced ced;

    // FIX 2: Stop infinite loop with Etablissement
    @ManyToOne
    @JoinColumn(name = "etablissement_id", referencedColumnName = "idEtablissement", nullable = false)
    @JsonIgnoreProperties("formationsDoctorales")
    private Etablissement etablissement;
}
