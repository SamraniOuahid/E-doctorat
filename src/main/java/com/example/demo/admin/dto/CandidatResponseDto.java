package com.example.demo.admin.dto;

import com.example.demo.scolarite.model.EtatDossier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for candidat data in admin responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidatResponseDto {
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String cne;
    private String cin;
    private String telCandidat;
    private EtatDossier etatDossier;
    private String sujetTitre;
    private String professeurNom;
}
