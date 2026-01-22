package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_sujet")
@Data
@NoArgsConstructor
public class Sujet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean publier;

    public boolean isPublier() {
        return publier;
    }

    // coDirecteur_id -> professeur_professeur.id (nullable)
    @ManyToOne
    @JoinColumn(
            name = "coDirecteur_id",
            referencedColumnName = "id"
    )
    private ProfesseurModel coDirecteur;

    // formationDoctorale_id -> professeur_formationdoctorale.id
    @ManyToOne
    @JoinColumn(
            name = "formationDoctorale_id",
            referencedColumnName = "id",
            nullable = false
    )
    private FormationDoctorale formationDoctorale;

    // professeur_id -> professeur_professeur.id
    @ManyToOne
    @JoinColumn(
            name = "professeur_id",
            referencedColumnName = "id",
            nullable = false
    )
    private ProfesseurModel professeur;

    // Relationship with Commission
    @ManyToOne
    @JoinColumn(name = "commission_id", referencedColumnName = "id")
    private Commission commission;

    // --- Manual Getters/Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public void setPublier(boolean publier) { this.publier = publier; }
    public ProfesseurModel getCoDirecteur() { return coDirecteur; }
    public void setCoDirecteur(ProfesseurModel coDirecteur) { this.coDirecteur = coDirecteur; }
    public FormationDoctorale getFormationDoctorale() { return formationDoctorale; }
    public void setFormationDoctorale(FormationDoctorale formationDoctorale) { this.formationDoctorale = formationDoctorale; }
    public ProfesseurModel getProfesseur() { return professeur; }
    public void setProfesseur(ProfesseurModel professeur) { this.professeur = professeur; }
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 255)
        private String titre;

        @Lob
        private String description;

        @Column(nullable = false)
        private boolean publier;

        @Column(nullable = false)
        private boolean needSeparatedFile = false;

        // coDirecteur_id -> professeur_professeur.id (nullable)
        @ManyToOne
        @JoinColumn(name = "coDirecteur_id", referencedColumnName = "id")
        private ProfesseurModel coDirecteur;

        // formationDoctorale_id -> professeur_formationdoctorale.id
        @ManyToOne
        @JoinColumn(name = "formationDoctorale_id", referencedColumnName = "id", nullable = false)
        private FormationDoctorale formationDoctorale;

        // professeur_id -> professeur_professeur.id
        @ManyToOne
        @JoinColumn(name = "professeur_id", referencedColumnName = "id", nullable = false)
        private ProfesseurModel professeur;
}
