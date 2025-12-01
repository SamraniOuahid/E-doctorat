package com.example.demo.candidat;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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



}
