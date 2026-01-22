package com.example.demo.professeur.dto;

import lombok.Data;

@Data
public class CandidatForProfDto {

    private Long inscriptionId;
    private Long candidatId;
    private String candidatNomComplet;
    private String email;
    private String sujetTitre;
    private Long sujetId;
    private Boolean valider;   // from Inscription
    private String remarque;   // optional

    // --- Manual Getters/Setters ---
    public Long getInscriptionId() { return inscriptionId; }
    public void setInscriptionId(Long inscriptionId) { this.inscriptionId = inscriptionId; }
    public Long getCandidatId() { return candidatId; }
    public void setCandidatId(Long candidatId) { this.candidatId = candidatId; }
    public String getCandidatNomComplet() { return candidatNomComplet; }
    public void setCandidatNomComplet(String candidatNomComplet) { this.candidatNomComplet = candidatNomComplet; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSujetTitre() { return sujetTitre; }
    public void setSujetTitre(String sujetTitre) { this.sujetTitre = sujetTitre; }
    public Long getSujetId() { return sujetId; }
    public void setSujetId(Long sujetId) { this.sujetId = sujetId; }
    public Boolean getValider() { return valider; }
    public void setValider(Boolean valider) { this.valider = valider; }
    public String getRemarque() { return remarque; }
    public void setRemarque(String remarque) { this.remarque = remarque; }
    private Boolean valider; // from Inscription
    private String remarque; // optional
    private String pathRecherche;
}
