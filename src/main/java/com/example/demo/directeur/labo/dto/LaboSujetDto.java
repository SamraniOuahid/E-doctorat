package com.example.demo.directeur.labo.dto;

import lombok.Data;

@Data
public class LaboSujetDto {

    private Long sujetId;
    private String sujetTitre;

    private Long professeurId;
    private String professeurGrade;
    private String professeurNumSom; // or CIN if you prefer
}
