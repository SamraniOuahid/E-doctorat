package com.example.demo.professeur.model;

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
}
