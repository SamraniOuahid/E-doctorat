package com.example.demo.directeur.pole.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity to track publication status of LP (Liste Principale) and LA (Liste Attente).
 * Once published, this cannot be rolled back.
 */
@Entity
@Table(name = "pole_publication_resultat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicationResultat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private TypeListe type; // LP or LA

    @Column(nullable = false)
    @Builder.Default
    private Boolean publiee = false;

    @Column
    private LocalDateTime datePublication;

    @Column(length = 100)
    private String publishedBy; // Email or username of who published

    public enum TypeListe {
        PRINCIPALE, // Liste Principale (LP)
        ATTENTE     // Liste d'Attente (LA)
    }
}
