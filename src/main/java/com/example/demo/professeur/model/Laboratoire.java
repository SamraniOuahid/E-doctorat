package com.example.demo.professeur.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_laboratoire")
@Data
@NoArgsConstructor
public class Laboratoire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String nomLaboratoire;

    // POSTGRESQL FIX
    // POSTGRESQL FIX: Use TEXT, not @Lob to avoid OID error
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String pathImage;

    @Column(length = 255)
    private String initial;

    // FIX 1: Stop loop with CED
    @ManyToOne
    @JoinColumn(name = "ced_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("laboratoires")
    private Ced ced;

    // FIX 2: Stop loop with Directeur
    @ManyToOne
    @JoinColumn(name = "directeur_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"laboratoire", "etablissement", "sujets"})
    private ProfesseurModel directeur;

    // FIX 3: Stop loop with Etablissement
    @ManyToOne
    @JoinColumn(name = "etablissement_id", referencedColumnName = "idEtablissement", nullable = false)
    @JsonIgnoreProperties("laboratoires")
    private Etablissement etablissement;

    // --- Manual Getters/Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomLaboratoire() { return nomLaboratoire; }
    public void setNomLaboratoire(String nomLaboratoire) { this.nomLaboratoire = nomLaboratoire; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPathImage() { return pathImage; }
    public void setPathImage(String pathImage) { this.pathImage = pathImage; }
    public String getInitial() { return initial; }
    public void setInitial(String initial) { this.initial = initial; }
    public Ced getCed() { return ced; }
    public void setCed(Ced ced) { this.ced = ced; }
    public ProfesseurModel getDirecteur() { return directeur; }
    public void setDirecteur(ProfesseurModel directeur) { this.directeur = directeur; }
    public Etablissement getEtablissement() { return etablissement; }
    public void setEtablissement(Etablissement etablissement) { this.etablissement = etablissement; }
}
