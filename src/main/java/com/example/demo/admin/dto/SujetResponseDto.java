package com.example.demo.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for thesis subject (sujet) data in admin responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SujetResponseDto {
    private Long id;
    private String titre;
    private String description;
    private boolean publier;
    private String professeurNom;
    private String professeurPrenom;
    private String laboratoireNom;
    private String formationDoctoraleTitre;
    private int nombreCandidatures;
}
