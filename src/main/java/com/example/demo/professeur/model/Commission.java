package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
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
    private Date dateCommission;

    @Column(length = 255, nullable = false)
    private String lieu;

    @Column(name = "heure", nullable = false)
    private LocalTime heure;

    // labo_id -> professeur_laboratoire.id
    @ManyToOne
    @JoinColumn(name = "labo_id", referencedColumnName = "id", nullable = false)
    private Laboratoire laboratoire;
}
