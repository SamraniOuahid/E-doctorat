package com.example.demo.candidat.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidat_postuler")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Postuler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pathFile;

    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;


}
