package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur")
@Data
@NoArgsConstructor
public class ProfesseurModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cin;

    private String telProfesseur;

    private String pathPhoto;

    private String grade;

    private String numSOM;

    @Column(nullable = false)
    private int nombreEncadrer;

    @Column(nullable = false)
    private int nombreProposer;

    //etablissement_id
    @ManyToOne
    @JoinColumn(
            name = "etablissement_id",
            referencedColumnName = "idEtablissement",
            nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT)
    )
    private Etablissement etablissement;

    //labo_id
    @ManyToOne
    @JoinColumn( name = "labo_id", referencedColumnName = "id", nullable = true)
    private Laboratoire laboratoire;

    //user_id
    @ManyToOne
    @JoinColumn( name = "user_id", referencedColumnName = "id", nullable = false)
    private long user;
    
}
