package com.example.demo.directeur.labo.dto;

import lombok.Data;

@Data
public class LaboResultatDto {

    private Long inscriptionId;

    private Long sujetId;
    private String sujetTitre;

    private Long candidatId;
    private String candidatNomComplet;
    private String candidatCne;

    private Boolean valider;   // true = accepté, false = refusé, null = en attente
    private String statut;     // "ACCEPTE", "REFUSE", "EN_ATTENTE"
    private String remarque;
}
