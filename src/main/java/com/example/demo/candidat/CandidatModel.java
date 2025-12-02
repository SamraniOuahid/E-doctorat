package com.example.demo.candidat;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@lombok.Getter
@lombok.Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Candidat")
@Entity
public class CandidatModel {
    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cne;
    private String cin;
    private String nomCandidatAr;
    private String prenomCandidatAr;
    private String adress;
    private String adressAr;
    private String sexe;
    private String villeDeNaissance;
    private String villeDeNaissanceAr;
    private String ville;
    private Date dateDeNaissance;
    private String typeDeHandiCap;
    private String academie;
    private String telCandidat;
    private String pathCv;
    private String pathPhoto;
    private int etatDossier;
    private String situationFamiliale;
    private int paysId;
    private int userId;
    private int fonctionaire;

    @OneToMany
    private List<NotificationModel> notifications = new ArrayList<>();
    // Un candidat peux avoir plusieurs diplome
    private List<CandidatDiplome> diplomes = new ArrayList<>();
}
