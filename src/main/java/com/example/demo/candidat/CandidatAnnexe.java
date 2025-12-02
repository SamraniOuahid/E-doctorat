package com.example.demo.candidat;

import jakarta.persistence.*;
import lombok.*;

@lombok.Getter
@lombok.Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CandidatAnnexe")
@Entity
public class CandidatAnnexe {
    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String typeAnnexe;
    private String titre;
    private String pathFile;
    private long diplomeId;

    @ManyToOne
    private CandidatDiplome diplome;
}
