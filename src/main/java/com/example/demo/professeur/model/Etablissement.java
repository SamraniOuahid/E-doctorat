package com.example.demo.professeur.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professeur_etablissement")
public class Etablissement {

    @Id
    @Column(name = "idEtablissement", length = 10)
    private String idEtablissement;

    @Column(name = "nomEtablissement", nullable = false, length = 255)
    private String nomEtablissement;

    public String getIdEtablissement() {
        return idEtablissement;
    }

    public void setIdEtablissement(String idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    public String getNomEtablissement() {
        return nomEtablissement;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }
}
