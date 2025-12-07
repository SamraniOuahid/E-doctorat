package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_commission_professeurs")
@Data
@NoArgsConstructor
public class CommissionProfesseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // commission_id -> professeur_commission.id
    @ManyToOne
    @JoinColumn(name = "commission_id", referencedColumnName = "id", nullable = false)
    private Commission commission;

    // professeur_id -> professeur_professeur.id
    @ManyToOne
    @JoinColumn(name = "professeur_id", referencedColumnName = "id", nullable = false)
    private ProfesseurModel professeur;
}
