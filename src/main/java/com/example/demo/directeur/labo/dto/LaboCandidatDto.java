package com.example.demo.directeur.labo.dto;

import lombok.Data;

@Data
public class LaboCandidatDto {

    private Long inscriptionId;

    private Long sujetId;
    private String sujetTitre;

    private Long candidatId;
    private String candidatNomComplet;
    private String candidatCne;

    // optionnel : état de la candidature vu par le labo
    private Boolean valider;   // true = accepté, false/null = en attente / refusé
    private String remarque;

    // --- Manual Getters/Setters ---
    public Long getInscriptionId() { return inscriptionId; }
    public void setInscriptionId(Long inscriptionId) { this.inscriptionId = inscriptionId; }
    public Long getSujetId() { return sujetId; }
    public void setSujetId(Long sujetId) { this.sujetId = sujetId; }
    public String getSujetTitre() { return sujetTitre; }
    public void setSujetTitre(String sujetTitre) { this.sujetTitre = sujetTitre; }
    public Long getCandidatId() { return candidatId; }
    public void setCandidatId(Long candidatId) { this.candidatId = candidatId; }
    public String getCandidatNomComplet() { return candidatNomComplet; }
    public void setCandidatNomComplet(String candidatNomComplet) { this.candidatNomComplet = candidatNomComplet; }
    public String getCandidatCne() { return candidatCne; }
    public void setCandidatCne(String candidatCne) { this.candidatCne = candidatCne; }
    public Boolean getValider() { return valider; }
    public void setValider(Boolean valider) { this.valider = valider; }
    public String getRemarque() { return remarque; }
    public void setRemarque(String remarque) { this.remarque = remarque; }
}
