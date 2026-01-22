package com.example.demo.scolarite.model;

public enum EtatDossier {
    EN_ATTENTE, // Dossier reçu, pas encore traité
    VALIDE, // Dossier complet (Administrativement)
    A_CORRIGER, // Manque des pièces
    REJETE, // Refusé administrativement
    ACCEPTE, // Accepté définitivement (après accord prof/admin)
    REFUSE // Refusé définitivement
}
