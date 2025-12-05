package com.example.demo.professeur.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professeur_etablissement")
@Data
@NoArgsConstructor
public class Etablissement {

    @Id
    @Column(name = "idEtablissement", length = 10)
    private String idEtablissement;

    @Column(name = "nomEtablissement", length = 255, nullable = false)
    private String nomEtablissement;
}
