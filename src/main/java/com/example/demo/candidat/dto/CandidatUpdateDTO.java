package com.example.demo.candidat.dto;

import lombok.Data; // <--- TRES IMPORTANT
import java.time.LocalDate;

@Data // <--- SANS ÇA, RIEN NE MARCHE !
public class CandidatUpdateDTO {
    private String cne;
    private String cin;
    private String telCandidat;
    private String sexe;
    private String nomCandidatAr;
    private String prenomCandidatAr;
    private LocalDate dateDeNaissance;
    private String villeDeNaissance;
    private String villeDeNaissanceAr;
    private String adresse;
    private String adresseAr;
    private String ville;
    private String pays;
    private String academie;
    private String situationFamiliale;
    private String typeDeHandiCape;
    private String pathPhoto;
    private String pathCv;
}