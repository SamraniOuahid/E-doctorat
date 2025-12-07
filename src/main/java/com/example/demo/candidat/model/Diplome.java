package com.example.demo.candidat.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidat_diplome")
public class Diplome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String intitule;
    private String type;

    // dans la BD c'est dateCommission (date)
    private LocalDate dateCommission;

    private String mention;
    private String pays;
    private String etablissement;
    private String specialite;
    private String ville;
    private String province;

    // moyen_generale (double) dans la BD
    private Double moyenGenerale;

    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)   // FK -> candidat_candidat.id [file:1]
    private Candidat candidat;

    @OneToMany(mappedBy = "diplome", cascade = CascadeType.ALL)
    private List<Annexe> annexes = new ArrayList<>();
}
