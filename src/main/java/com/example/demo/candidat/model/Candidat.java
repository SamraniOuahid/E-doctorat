package com.example.demo.candidat.model;
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
    private Integer etatDossier;
    private String situationFamiliale;

    @ManyToOne
    @JoinColumn(name = "pays_id", nullable = false)
    private Pays pays;

    // Si tu as une entité User (spring security) tu peux la mapper ici
    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL)
    private List<Postuler> postulers;


    public String getNomComplet() {
        String nom = (nomCandidatAr != null) ? nomCandidatAr : "";
        String prenom = (prenomCandidatAr != null) ? prenomCandidatAr : "";
        return (nom + " " + prenom).trim();
    }

}
