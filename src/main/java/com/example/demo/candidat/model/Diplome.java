package com.example.demo.candidat;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@lombok.Getter
@lombok.Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CandidatDiplome")
@Entity
public class CandidatDiplome {
    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String intitule;
    private String type;
    private Date dateCommmission;
    private String mention;
    private String pays;
    private String etablissement;
    private String specialite;
    private String ville;
    private String province;
    private String moyenneGeneral;
    private String candidatId;

    @ManyToOne
    private CandidatModel candidat;
    @OneToMany
    private List<CandidatAnnexe> annexes = new ArrayList<>();
}
