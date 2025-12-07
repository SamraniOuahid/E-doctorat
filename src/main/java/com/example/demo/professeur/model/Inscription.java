package com.example.demo.professeur.model;

import com.example.demo.candidat.model.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "professeur_inscription")
@Data
@NoArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dateDiposeDossier")
    private Date dateDiposeDossier;

    @Column(length = 255)
    private String remarque;

    @Column(nullable = false)
    private Boolean valider;   // tinyint(1)

    // candidat_id -> candidat_candidat.id
    @ManyToOne
    @JoinColumn(name = "candidat_id", referencedColumnName = "id", nullable = false)
    private Candidat candidat;

    // sujet_id -> professeur_sujet.id
    @ManyToOne
    @JoinColumn(name = "sujet_id", referencedColumnName = "id", nullable = false)
    private Sujet sujet;
}
