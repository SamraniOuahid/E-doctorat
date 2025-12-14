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
}
