package com.example.demo.professeur.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "professeur_commission")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateCommission() {
        return dateCommission;
    }

    public void setDateCommission(LocalDate dateCommission) {
        this.dateCommission = dateCommission;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public Long getLaboratoireId() {
        return laboratoireId;
    }

    public void setLaboratoireId(Long laboratoireId) {
        this.laboratoireId = laboratoireId;
    }
}
