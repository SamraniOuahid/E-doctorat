package com.example.demo.professeur.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professeur_ced")
public class Ced {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "pathImage", length = 100)
    private String pathImage;

    @Column(name = "initiale", length = 255)
    private String initiale;

    @Column(name = "titre", nullable = false, length = 255)
    private String titre;

    @Column(name = "directeur_id")
    private Long directeurId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getInitiale() {
        return initiale;
    }

    public void setInitiale(String initiale) {
        this.initiale = initiale;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Long getDirecteurId() {
        return directeurId;
    }

    public void setDirecteurId(Long directeurId) {
        this.directeurId = directeurId;
    }
}
