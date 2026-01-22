package com.example.demo.directeur.pole.model;

/**
 * Enum representing the status of a campaign phase
 */
public enum PhaseStatut {
    PLANIFIE,   // Phase is planned but dates not confirmed
    CONFIRME,   // Phase dates are confirmed
    EN_COURS,   // Phase is currently active
    TERMINE     // Phase has ended
}
