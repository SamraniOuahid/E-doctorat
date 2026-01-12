package com.example.demo.candidat.model;

import com.example.demo.scolarite.model.EtatDossier; // Assure-toi d'importer l'Enum
import com.example.demo.security.user.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "candidat_candidat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String cne;
    private String cin;
    private String nomCandidatAr;
    private String prenomCandidatAr;
    private String adresse;
    private String adresseAr;
    private String sexe;
    private String villeDeNaissance;
    private String villeDeNaissanceAr;
    private String ville;
    private LocalDate dateDeNaissance;
    private String typeDeHandiCape;
    private String academie;
    private String telCandidat;
    private String pathCv;
    private String pathPhoto;
    private String situationFamiliale;

    // --- MODIFICATION ICI : Gestion de l'état du dossier ---

    // On utilise l'Enum pour plus de sécurité (stocké en String dans la BDD pour la lisibilité)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EtatDossier etatDossier = EtatDossier.EN_ATTENTE;

    // Nouveau champ pour le commentaire de la scolarité
    @Column(columnDefinition = "TEXT")
    private String commentaireScolarite;

    // -------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "pays_id", nullable = true) // mis nullable=true temporairement si erreur migration
    private Pays pays;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL)
    private List<Postuler> postulers;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String getNomComplet() {
        String nom = (nomCandidatAr != null) ? nomCandidatAr : "";
        String prenom = (prenomCandidatAr != null) ? prenomCandidatAr : "";
        return (nom + " " + prenom).trim();
    }

    public void setPays(String pays) {

    }
}
