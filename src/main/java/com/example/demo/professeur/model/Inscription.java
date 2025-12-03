package com.example.demo.professeur.model;

import com.example.demo.candidat.CandidatModel;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_inscription")
@Data
@NoArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dateDiposeDossier")
    private java.sql.Date dateDiposeDossier;

    @Column(name = "remarque", length = 255)
    private String remarque;

    @Column(name = "valider", nullable = false)
    private Boolean valider;  // tinyint(1) => boolean

    @ManyToOne
    @JoinColumn(name = "candidat_id", referencedColumnName = "id")
    private CandidatModel candidat;

    @ManyToOne
    @JoinColumn(name = "sujet_id", referencedColumnName = "id")
    private Sujet sujet;
}
