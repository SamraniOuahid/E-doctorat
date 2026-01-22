package com.example.demo.candidat.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidat_notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // ex: "SUJET_ASSIGNE", "RESULTAT_PUBLIE", etc.

    public void setType(String type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Candidat candidat;

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }


    // Optionnel : si tu crées les entités Commission et Sujet
    // @ManyToOne
    // @JoinColumn(name = "commission_id")
    // private Commission commission;
    //
    // @ManyToOne
    // @JoinColumn(name = "sujet_id")
    // private Sujet sujet;
}
