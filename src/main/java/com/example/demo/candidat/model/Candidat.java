package com.example.demo.candidat.model;

import com.example.demo.scolarite.model.EtatDossier; // Assure-toi d'importer l'Enum

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

    // On utilise l'Enum pour plus de sécurité (stocké en String dans la BDD pour la
    // lisibilité)
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
    private com.example.demo.security.user.UserAccount user;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL)
    private List<Diplome> diplomes;

    // --- Getters & Setters Manuels (Lombok Fix) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCne() { return cne; }
    public void setCne(String cne) { this.cne = cne; }
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    public String getNomCandidatAr() { return nomCandidatAr; }
    public void setNomCandidatAr(String nomCandidatAr) { this.nomCandidatAr = nomCandidatAr; }
    public String getPrenomCandidatAr() { return prenomCandidatAr; }
    public void setPrenomCandidatAr(String prenomCandidatAr) { this.prenomCandidatAr = prenomCandidatAr; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getAdresseAr() { return adresseAr; }
    public void setAdresseAr(String adresseAr) { this.adresseAr = adresseAr; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public String getVilleDeNaissance() { return villeDeNaissance; }
    public void setVilleDeNaissance(String villeDeNaissance) { this.villeDeNaissance = villeDeNaissance; }
    public String getVilleDeNaissanceAr() { return villeDeNaissanceAr; }
    public void setVilleDeNaissanceAr(String villeDeNaissanceAr) { this.villeDeNaissanceAr = villeDeNaissanceAr; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public LocalDate getDateDeNaissance() { return dateDeNaissance; }
    public void setDateDeNaissance(LocalDate dateDeNaissance) { this.dateDeNaissance = dateDeNaissance; }
    public String getTypeDeHandiCape() { return typeDeHandiCape; }
    public void setTypeDeHandiCape(String typeDeHandiCape) { this.typeDeHandiCape = typeDeHandiCape; }
    public String getAcademie() { return academie; }
    public void setAcademie(String academie) { this.academie = academie; }
    public String getTelCandidat() { return telCandidat; }
    public void setTelCandidat(String telCandidat) { this.telCandidat = telCandidat; }
    public String getPathCv() { return pathCv; }
    public void setPathCv(String pathCv) { this.pathCv = pathCv; }
    public String getPathPhoto() { return pathPhoto; }
    public void setPathPhoto(String pathPhoto) { this.pathPhoto = pathPhoto; }
    public String getSituationFamiliale() { return situationFamiliale; }
    public void setSituationFamiliale(String situationFamiliale) { this.situationFamiliale = situationFamiliale; }
    public EtatDossier getEtatDossier() { return etatDossier; }
    public void setEtatDossier(EtatDossier etatDossier) { this.etatDossier = etatDossier; }
    public String getCommentaireScolarite() { return commentaireScolarite; }
    public void setCommentaireScolarite(String commentaireScolarite) { this.commentaireScolarite = commentaireScolarite; }
    public Pays getPays() { return pays; }
    public void setPays(Pays pays) { this.pays = pays; }
    public List<Notification> getNotifications() { return notifications; }
    public void setNotifications(List<Notification> notifications) { this.notifications = notifications; }
    public List<Postuler> getPostulers() { return postulers; }
    public void setPostulers(List<Postuler> postulers) { this.postulers = postulers; }
    public com.example.demo.security.user.UserAccount getUser() { return user; }
    public void setUser(com.example.demo.security.user.UserAccount user) { this.user = user; }
    public List<Diplome> getDiplomes() { return diplomes; }
    public void setDiplomes(List<Diplome> diplomes) { this.diplomes = diplomes; }


    public String getNomComplet() {
        String nom = (nomCandidatAr != null) ? nomCandidatAr : "";
        String prenom = (prenomCandidatAr != null) ? prenomCandidatAr : "";
        return (nom + " " + prenom).trim();
    }

    public void setPays(String pays) {

    }

    /**
     * Check if profile is complete with all required fields
     * Required fields: cin, cne, nomCandidatAr, prenomCandidatAr, dateDeNaissance,
     * telCandidat, adresse, ville, sexe, pathPhoto, pathCv
     */
    public boolean isProfileComplete() {
        return cin != null && !cin.trim().isEmpty()
                && cne != null && !cne.trim().isEmpty()
                && nomCandidatAr != null && !nomCandidatAr.trim().isEmpty()
                && prenomCandidatAr != null && !prenomCandidatAr.trim().isEmpty()
                && dateDeNaissance != null
                && telCandidat != null && !telCandidat.trim().isEmpty()
                && adresse != null && !adresse.trim().isEmpty()
                && ville != null && !ville.trim().isEmpty()
                && sexe != null && !sexe.trim().isEmpty()
                && pathPhoto != null && !pathPhoto.trim().isEmpty()
                && pathCv != null && !pathCv.trim().isEmpty();
    }

    /**
     * Calculate profile completion percentage
     */
    public int getProfileCompletionPercentage() {
        int totalFields = 11; // Total required fields
        int completedFields = 0;

        if (cin != null && !cin.trim().isEmpty())
            completedFields++;
        if (cne != null && !cne.trim().isEmpty())
            completedFields++;
        if (nomCandidatAr != null && !nomCandidatAr.trim().isEmpty())
            completedFields++;
        if (prenomCandidatAr != null && !prenomCandidatAr.trim().isEmpty())
            completedFields++;
        if (dateDeNaissance != null)
            completedFields++;
        if (telCandidat != null && !telCandidat.trim().isEmpty())
            completedFields++;
        if (adresse != null && !adresse.trim().isEmpty())
            completedFields++;
        if (ville != null && !ville.trim().isEmpty())
            completedFields++;
        if (sexe != null && !sexe.trim().isEmpty())
            completedFields++;
        if (pathPhoto != null && !pathPhoto.trim().isEmpty())
            completedFields++;
        if (pathCv != null && !pathCv.trim().isEmpty())
            completedFields++;

        return (completedFields * 100) / totalFields;
    }
}
