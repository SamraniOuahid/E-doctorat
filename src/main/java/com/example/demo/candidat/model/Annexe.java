package com.example.demo.candidat.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidat_annexe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Annexe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeAnnexe;
    private String titre;
    private String pathFile;

    @ManyToOne
    @JoinColumn(name = "diplome_id", nullable = false)
    private Diplome diplome;
}
