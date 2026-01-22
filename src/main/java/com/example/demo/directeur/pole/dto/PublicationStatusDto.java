package com.example.demo.directeur.pole.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicationStatusDto {
    private String type; // "principale" or "attente"
    private boolean publiee;
    private LocalDateTime datePublication;
    private String publishedBy;
}
