package com.example.demo.candidat.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidat_notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Legacy field, kept for compatibility

    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Candidat candidat;

    // Enhanced fields for better notification management
    @Column(length = 500)
    private String message;

    @Column(nullable = false)
    @Builder.Default
    private Boolean lue = false; // Read status

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(length = 50)
    private String typeNotification; // "RESULT_LP", "RESULT_LA", "PHASE_CHANGE", etc.
}

