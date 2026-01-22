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

    public String getCne() { return cne; }
    public void setCne(String cne) { this.cne = cne; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getTelCandidat() { return telCandidat; }
    public void setTelCandidat(String telCandidat) { this.telCandidat = telCandidat; }

    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }

    public String getNomCandidatAr() { return nomCandidatAr; }
    public void setNomCandidatAr(String nomCandidatAr) { this.nomCandidatAr = nomCandidatAr; }

    public String getPrenomCandidatAr() { return prenomCandidatAr; }
    public void setPrenomCandidatAr(String prenomCandidatAr) { this.prenomCandidatAr = prenomCandidatAr; }

    public LocalDate getDateDeNaissance() { return dateDeNaissance; }
    public void setDateDeNaissance(LocalDate dateDeNaissance) { this.dateDeNaissance = dateDeNaissance; }

    public String getVilleDeNaissance() { return villeDeNaissance; }
    public void setVilleDeNaissance(String villeDeNaissance) { this.villeDeNaissance = villeDeNaissance; }

    public String getVilleDeNaissanceAr() { return villeDeNaissanceAr; }
    public void setVilleDeNaissanceAr(String villeDeNaissanceAr) { this.villeDeNaissanceAr = villeDeNaissanceAr; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getAdresseAr() { return adresseAr; }
    public void setAdresseAr(String adresseAr) { this.adresseAr = adresseAr; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public String getAcademie() { return academie; }
    public void setAcademie(String academie) { this.academie = academie; }

    public String getSituationFamiliale() { return situationFamiliale; }
    public void setSituationFamiliale(String situationFamiliale) { this.situationFamiliale = situationFamiliale; }

    public String getTypeDeHandiCape() { return typeDeHandiCape; }
    public void setTypeDeHandiCape(String typeDeHandiCape) { this.typeDeHandiCape = typeDeHandiCape; }

    public String getPathPhoto() { return pathPhoto; }
    public void setPathPhoto(String pathPhoto) { this.pathPhoto = pathPhoto; }

    public String getPathCv() { return pathCv; }
    public void setPathCv(String pathCv) { this.pathCv = pathCv; }
}