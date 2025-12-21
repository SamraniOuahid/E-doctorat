package com.example.demo.scolarite.model;

public enum EtatDossier {
    EN_ATTENTE,   // Dossier reçu, pas encore traité
    VALIDE,       // Dossier complet et accepté
    A_CORRIGER,   // Manque des pièces ou infos incorrectes
    REJETE        // Candidature refusée administrativement
}
