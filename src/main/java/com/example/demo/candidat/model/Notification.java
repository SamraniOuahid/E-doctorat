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

    private String type;

    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;


    // Optionnel : si tu crées les entités Commission et Sujet
    // @ManyToOne
    // @JoinColumn(name = "commission_id")
    // private Commission commission;
    //
    // @ManyToOne
    // @JoinColumn(name = "sujet_id")
    // private Sujet sujet;
}
