package com.example.demo.professeur.dto;

import lombok.Data;

@Data
public class SujetDto {
    private String titre;
    private String description;
    private boolean publier;
    private Long formationDoctoraleId; // if you need it now or later
    private Long coDirecteurId;        // optional
}
