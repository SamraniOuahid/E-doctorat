package com.example.demo.directeur.pole.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entity representing a phase of the doctoral campaign.
 * Phases define the timeline for various activities like:
 * - Proposition des sujets (professors propose subjects)
 * - Dépôt des dossiers (candidats submit applications)
 * - Présélection (committee reviews applications)
 * - Entretiens (oral interviews)
 * - Affichage résultats (publication of results)
 */
@Entity
@Table(name = "pole_phase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String code; // e.g., "PROP_SUJETS", "DEPOT_DOSSIERS", etc.

    @Column(nullable = false, length = 100)
    private String nom; // e.g., "Proposition des Sujets"

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PhaseStatut statut = PhaseStatut.PLANIFIE;

    @Column(nullable = false)
    private Integer ordre; // Order of phase (1, 2, 3...)

    @Column(length = 500)
    private String description;

    /**
     * Check if this phase is currently active
     */
    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(dateDebut) && !now.isAfter(dateFin);
    }

    /**
     * Check if this phase is in the past
     */
    public boolean isPast() {
        return LocalDate.now().isAfter(dateFin);
    }

    /**
     * Check if this phase is in the future
     */
    public boolean isFuture() {
        return LocalDate.now().isBefore(dateDebut);
    }
}
