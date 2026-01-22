package com.example.demo.professeur.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_ced")
@Data
@NoArgsConstructor
public class Ced {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // POSTGRESQL FIX: Use TEXT or @Lob (not LONGTEXT)
    // POSTGRESQL FIX: Use TEXT or @Lob (not LONGTEXT)
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String pathImage;

    @Column(length = 255)
    private String initiale;

    @Column(length = 255, nullable = false)
    private String titre;

    // FIX: Add @JsonIgnoreProperties to stop infinite loop
    @ManyToOne
    @JoinColumn(name = "directeur_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"laboratoire", "etablissement", "sujets", "ced"})
    private ProfesseurModel directeur;

    // --- Manual Getters/Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPathImage() { return pathImage; }
    public void setPathImage(String pathImage) { this.pathImage = pathImage; }
    public String getInitiale() { return initiale; }
    public void setInitiale(String initiale) { this.initiale = initiale; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public ProfesseurModel getDirecteur() { return directeur; }
    public void setDirecteur(ProfesseurModel directeur) { this.directeur = directeur; }
}
