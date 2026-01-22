package com.example.demo.directeur.pole.dto;

import lombok.Data;

@Data
public class PoleCandidatDto {
    private Long id;
    private String cne;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String dateNaissance;
    private String lieuNaissance;
    private String diplomePrecedent;
    private String etablissement;
    private Double moyenneGenerale;
    private String sujetTitre;
    private String laboratoire;
    private String formation;
    private Long formationId;
}
