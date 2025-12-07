package com.example.demo.candidat.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidat_pays")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
}
