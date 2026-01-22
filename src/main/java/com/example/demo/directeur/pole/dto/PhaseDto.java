package com.example.demo.directeur.pole.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhaseDto {
    private Long id;
    private String code;
    private String nom;
    private String phase; // For frontend compatibility (e.g., "Phase 0", "Phase 1")
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;
    private Integer ordre;
    private String description;
    private boolean active;
    private boolean past;
    private boolean future;
}
