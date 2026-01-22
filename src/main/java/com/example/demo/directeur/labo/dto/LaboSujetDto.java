package com.example.demo.directeur.labo.dto;

import lombok.Data;

@Data
public class LaboSujetDto {

    private Long sujetId;
    private String sujetTitre;

    private Long professeurId;
    private String professeurGrade;
    private String professeurNumSom;

    private Long coDirecteurId;
    private String coDirecteurNom;

    private Long formationDoctoraleId;
    private String formationDoctoraleTitre;

    // --- Manual Getters/Setters ---
    public Long getSujetId() { return sujetId; }
    public void setSujetId(Long sujetId) { this.sujetId = sujetId; }
    public String getSujetTitre() { return sujetTitre; }
    public void setSujetTitre(String sujetTitre) { this.sujetTitre = sujetTitre; }
    public Long getProfesseurId() { return professeurId; }
    public void setProfesseurId(Long professeurId) { this.professeurId = professeurId; }
    public String getProfesseurGrade() { return professeurGrade; }
    public void setProfesseurGrade(String professeurGrade) { this.professeurGrade = professeurGrade; }
    public String getProfesseurNumSom() { return professeurNumSom; }
    public void setProfesseurNumSom(String professeurNumSom) { this.professeurNumSom = professeurNumSom; }
    public Long getCoDirecteurId() { return coDirecteurId; }
    public void setCoDirecteurId(Long coDirecteurId) { this.coDirecteurId = coDirecteurId; }
    public String getCoDirecteurNom() { return coDirecteurNom; }
    public void setCoDirecteurNom(String coDirecteurNom) { this.coDirecteurNom = coDirecteurNom; }
    public Long getFormationDoctoraleId() { return formationDoctoraleId; }
    public void setFormationDoctoraleId(Long formationDoctoraleId) { this.formationDoctoraleId = formationDoctoraleId; }
    public String getFormationDoctoraleTitre() { return formationDoctoraleTitre; }
    public void setFormationDoctoraleTitre(String formationDoctoraleTitre) { this.formationDoctoraleTitre = formationDoctoraleTitre; }
}
