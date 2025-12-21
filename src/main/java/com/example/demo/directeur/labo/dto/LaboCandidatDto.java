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
}
