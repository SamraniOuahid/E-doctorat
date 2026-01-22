package com.example.demo.directeur.pole.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class PoleInscriptionDto {
    private Long id;
    private String cne;
    private String nom;
    private String prenom;
    private String sujet;
    private String directeur;
    private String laboratoire;
    private LocalDate dateInscription;
    private String statut;
}
