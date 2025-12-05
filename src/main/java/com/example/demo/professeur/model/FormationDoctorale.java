package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_formationdoctorale")
@Data
@NoArgsConstructor
public class FormationDoctorale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pathImage", length = 100)
    private String pathImage;

    @Column(name = "initiale", length = 255)
    private String initiale;

    @Column(name = "titre", length = 255, nullable = false)
    private String titre;

    @Column(name = "axeDeRecherche", columnDefinition = "LONGTEXT")
    private String axeDeRecherche;

    @Column(name = "dateAccreditation")
    private java.sql.Date dateAccreditation;

    @ManyToOne
    @JoinColumn(name = "ced_id", referencedColumnName = "id")
    private Ced ced;

    @ManyToOne
    @JoinColumn(name = "etablissement_id", referencedColumnName = "id")
    private Etablissement etablissement;
}
