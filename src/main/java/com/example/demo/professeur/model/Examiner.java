package com.example.demo.professeur.model;

import com.example.demo.candidat.model.*;
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

    @Column(length = 255)
    private String decision;

    @Column(nullable = false)
    private float noteDossier;

    @Column(nullable = false)
    private int noteEntretien;

    @Column(nullable = false)
    private boolean publier;

    @Column(nullable = false)
    private boolean valider;

    // commission_id -> professeur_commission.id
    @ManyToOne
    @JoinColumn(name = "commission_id", referencedColumnName = "id", nullable = false)
    private Commission commission;

    // sujet_id -> professeur_sujet.id
    @ManyToOne
    @JoinColumn(name = "sujet_id", referencedColumnName = "id", nullable = false)
    private Sujet sujet;

    // candidat_id -> candidat_candidat.id
    @ManyToOne
    @JoinColumn(name = "candidat_id", referencedColumnName = "id", nullable = false)
    private Candidat candidat;
}
