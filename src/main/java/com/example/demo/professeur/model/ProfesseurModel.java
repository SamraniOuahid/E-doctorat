package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur")
@Data
@NoArgsConstructor
public class ProfesseurModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cin;

    private String telProfesseur;

    private String pathPhoto;

    private String grade;

    private String numSOM;

    private String nom;
    private String prenom;

    @Column(nullable = false)
    private int nombreEncadrer;

    @Column(nullable = false)
    private int nombreProposer;

    // etablissement_id -> professeur_etablissement.idEtablissement
    @ManyToOne
    @JoinColumn(
            name = "etablissement_id",
            referencedColumnName = "idEtablissement",
            nullable = false
    )
    private Etablissement etablissement;

    // labo_id -> professeur_laboratoire.id  (nullable)
    @ManyToOne
    @JoinColumn(name = "labo_id", referencedColumnName = "id")
    private Laboratoire laboratoire;

    // user_id -> auth_user.id  (we keep only the FK, no relation entity)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // --- Manual Getters/Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    public String getTelProfesseur() { return telProfesseur; }
    public void setTelProfesseur(String telProfesseur) { this.telProfesseur = telProfesseur; }
    public String getPathPhoto() { return pathPhoto; }
    public void setPathPhoto(String pathPhoto) { this.pathPhoto = pathPhoto; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getNumSOM() { return numSOM; }
    public void setNumSOM(String numSOM) { this.numSOM = numSOM; }
    public int getNombreEncadrer() { return nombreEncadrer; }
    public void setNombreEncadrer(int nombreEncadrer) { this.nombreEncadrer = nombreEncadrer; }
    public int getNombreProposer() { return nombreProposer; }
    public void setNombreProposer(int nombreProposer) { this.nombreProposer = nombreProposer; }
    public Etablissement getEtablissement() { return etablissement; }
    public void setEtablissement(Etablissement etablissement) { this.etablissement = etablissement; }
    public Laboratoire getLaboratoire() { return laboratoire; }
    public void setLaboratoire(Laboratoire laboratoire) { this.laboratoire = laboratoire; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
}
