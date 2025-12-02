package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "professeur_commission")
@Data
@NoArgsConstructor
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dateCommission", nullable = false)
    private LocalDate dateCommission;

    @Column(name = "lieu", nullable = false, length = 255)
    private String lieu;

    @Column(name = "heure", nullable = false)
    private LocalTime heure;

    @Column(name = "labo_id", nullable = false)
    private Long laboratoireId;
}
