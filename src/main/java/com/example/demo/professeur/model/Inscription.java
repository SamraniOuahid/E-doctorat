package com.example.demo.professeur.model;

import com.example.demo.candidat.model.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "professeur_inscription")
@Data
@NoArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dateDiposeDossier")
    private Date dateDiposeDossier;

    @Column(length = 255)
    private String remarque;

    @Column(nullable = false)
    private Boolean valider;   // tinyint(1)

    // candidat_id -> candidat_candidat.id
    @ManyToOne
    @JoinColumn(name = "candidat_id", referencedColumnName = "id", nullable = false)
    private Candidat candidat;

    // sujet_id -> professeur_sujet.id
    @ManyToOne
    @JoinColumn(name = "sujet_id", referencedColumnName = "id", nullable = false)
    private Sujet sujet;

    // --- Manual Getters/Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getDateDiposeDossier() { return dateDiposeDossier; }
    public void setDateDiposeDossier(Date dateDiposeDossier) { this.dateDiposeDossier = dateDiposeDossier; }
    public String getRemarque() { return remarque; }
    public void setRemarque(String remarque) { this.remarque = remarque; }
    public Boolean getValider() { return valider; }
    public void setValider(Boolean valider) { this.valider = valider; }
    public Candidat getCandidat() { return candidat; }
    public void setCandidat(Candidat candidat) { this.candidat = candidat; }
    public Sujet getSujet() { return sujet; }
    public void setSujet(Sujet sujet) { this.sujet = sujet; }
}
