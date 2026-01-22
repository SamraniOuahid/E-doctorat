package com.example.demo.scolarite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScolariteCandidatDto {
    private Long id;
    private String cne;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String formation;
    private String sujet;
    private String directeur;
    private String etatDossier;
    private Boolean inscrit;
    private java.time.LocalDate dateInscription;
}
