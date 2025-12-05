package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_sujet")
@Data
@NoArgsConstructor
public class Sujet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titre;

    @Lob
    private String description;

    @Column(nullable = false)
    private boolean publier;

    //coDirecteur_id → professeur_professeur.id
    @ManyToOne
    @JoinColumn(
            name = "coDirecteur_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private ProfesseurModel coDirecteur;

    //formationDoctorale_id → professeur_formationdoctorale.id
    @ManyToOne
    @JoinColumn( name = "formationDoctorale_id", referencedColumnName = "id", nullable = false)
    private FormationDoctorale formationDoctorale;

    //professeur_id → professeur_professeur.id
    @ManyToOne
    @JoinColumn( name = "professeur_id", referencedColumnName = "id", nullable = false)
    private ProfesseurModel professeur;
}
