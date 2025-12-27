package com.example.demo.scolarite.dto;

import com.example.demo.scolarite.model.EtatDossier;
import lombok.Data;

@Data
public class ValidationDossierDto {
    private EtatDossier etat;
    private String commentaire;
}
