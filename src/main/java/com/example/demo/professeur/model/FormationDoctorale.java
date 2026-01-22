package com.example.demo.professeur.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "professeur_formationdoctorale")
@Data
@NoArgsConstructor
public class FormationDoctorale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String pathImage;

    @Column(length = 255)
    private String initiale;

    @Column(length = 255, nullable = false)
    private String titre;

    // POSTGRESQL FIX
    // POSTGRESQL FIX
    @Column(name = "axeDeRecherche", columnDefinition = "TEXT")
    private String axeDeRecherche;

    @Column(name = "dateAccreditation")
    private Date dateAccreditation;

    // FIX 1: Stop infinite loop with CED
    @ManyToOne
    @JoinColumn(name = "ced_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("formationsDoctorales")
    private Ced ced;

    // FIX 2: Stop infinite loop with Etablissement
    @ManyToOne
    @JoinColumn(name = "etablissement_id", referencedColumnName = "idEtablissement", nullable = false)
    @JsonIgnoreProperties("formationsDoctorales")
    private Etablissement etablissement;

    // --- Manual Getters/Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPathImage() { return pathImage; }
    public void setPathImage(String pathImage) { this.pathImage = pathImage; }
    public String getInitiale() { return initiale; }
    public void setInitiale(String initiale) { this.initiale = initiale; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getAxeDeRecherche() { return axeDeRecherche; }
    public void setAxeDeRecherche(String axeDeRecherche) { this.axeDeRecherche = axeDeRecherche; }
    public Date getDateAccreditation() { return dateAccreditation; }
    public void setDateAccreditation(Date dateAccreditation) { this.dateAccreditation = dateAccreditation; }
    public Ced getCed() { return ced; }
    public void setCed(Ced ced) { this.ced = ced; }
    public Etablissement getEtablissement() { return etablissement; }
    public void setEtablissement(Etablissement etablissement) { this.etablissement = etablissement; }
}
