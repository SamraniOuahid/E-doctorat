package com.example.demo.directeur.pole.dto;

import lombok.Data;

@Data
public class PoleSujetDto {
    private Long id;
    private String titre;
    private String professeur;
    private String coDirecteur;
    private String laboratoire;
    private String formation;
    private Long formationId;
    private Long laboId;
    private boolean publier;
}
