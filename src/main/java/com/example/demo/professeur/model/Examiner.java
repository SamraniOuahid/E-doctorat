package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_examiner")
@Data
@NoArgsConstructor
public class Examiner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "decision", length = 255)
    private String decision;

    @Column(name = "noteDossier", nullable = false)
    private Float noteDossier;

    @Column(name = "noteEntretien", nullable = false)
    private Integer noteEntretien;

    @Column(name = "publier", nullable = false)
    private Boolean publier;

    @Column(name = "valider", nullable = false)
    private Boolean valider;

    @Column(name = "commission_id", nullable = false)
    private Long commissionId;

    @Column(name = "sujet_id", nullable = false)
    private Long sujetId;

    @Column(name = "candidat_id", nullable = false)
    private Long candidatId;
}
