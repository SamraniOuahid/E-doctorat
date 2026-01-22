package com.example.demo.professeur.dto;

import lombok.Data;

@Data
public class SujetDto {
    private String titre;
    private String description;
    private boolean publier;
    private Long formationDoctoraleId; // if you need it now or later
    private Long coDirecteurId;        // optional

    // --- Manual Getters/Setters ---
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isPublier() { return publier; }
    public void setPublier(boolean publier) { this.publier = publier; }
    public Long getFormationDoctoraleId() { return formationDoctoraleId; }
    public void setFormationDoctoraleId(Long formationDoctoraleId) { this.formationDoctoraleId = formationDoctoraleId; }
    public Long getCoDirecteurId() { return coDirecteurId; }
    public void setCoDirecteurId(Long coDirecteurId) { this.coDirecteurId = coDirecteurId; }
}
