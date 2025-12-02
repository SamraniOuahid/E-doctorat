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

    @Column(name = "commission_id", nullable = false)
    private Long commissionId;

    @Column(name = "professeur_id", nullable = false)
    private Long professeurId;
}
