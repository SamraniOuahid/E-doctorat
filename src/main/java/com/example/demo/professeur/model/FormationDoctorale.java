package com.example.demo.professeur.model;

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

    @Column(name = "axeDeRecherche", columnDefinition = "LONGTEXT")
    private String axeDeRecherche;

    @Column(name = "dateAccreditation")
    private Date dateAccreditation;

    // ced_id -> professeur_ced.id
    @ManyToOne
    @JoinColumn(name = "ced_id", referencedColumnName = "id", nullable = false)
    private Ced ced;

    // etablissement_id -> professeur_etablissement.idEtablissement
    @ManyToOne
    @JoinColumn(
            name = "etablissement_id",
            referencedColumnName = "idEtablissement",
            nullable = false
    )
    private Etablissement etablissement;
}
