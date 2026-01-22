package com.example.demo.scolarite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScolariteDossierDto {
    private Long id;
    private String cne;
    private String nomCandidatAr;
    private String prenomCandidatAr;
    private String cin;
    private String email;
    private String telCandidat;
    private String ville;
    private String etatDossier;
    private String pathPhoto;
}
