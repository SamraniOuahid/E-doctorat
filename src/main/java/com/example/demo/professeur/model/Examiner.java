package com.example.demo.professeur.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professeur_examiner")
public class Examiner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "decision", length = 255)
    private String decision;

    @Column(name = "noteDossier", nullable = false)
    private Float noteDossier;

    @Column(name = "noteEntretien", nullable = false)
    private Integer noteEntretien;

    @Column(name = "publier", nullable = false)
    private Boolean publier;

    @Column(name = "valider", nullable = false)
    private Boolean valider;

    @Column(name = "commission_id", nullable = false)
    private Long commissionId;

    @Column(name = "sujet_id", nullable = false)
    private Long sujetId;

    @Column(name = "candidat_id", nullable = false)
    private Long candidatId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Float getNoteDossier() {
        return noteDossier;
    }

    public void setNoteDossier(Float noteDossier) {
        this.noteDossier = noteDossier;
    }

    public Integer getNoteEntretien() {
        return noteEntretien;
    }

    public void setNoteEntretien(Integer noteEntretien) {
        this.noteEntretien = noteEntretien;
    }

    public Boolean getPublier() {
        return publier;
    }

    public void setPublier(Boolean publier) {
        this.publier = publier;
    }

    public Boolean getValider() {
        return valider;
    }

    public void setValider(Boolean valider) {
        this.valider = valider;
    }

    public Long getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(Long commissionId) {
        this.commissionId = commissionId;
    }

    public Long getSujetId() {
        return sujetId;
    }

    public void setSujetId(Long sujetId) {
        this.sujetId = sujetId;
    }

    public Long getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(Long candidatId) {
        this.candidatId = candidatId;
    }
}
