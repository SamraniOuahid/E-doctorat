package com.example.demo.directeur.pole.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoleLaboratoireDto {
    private Long id;
    private String nom;
    private Long formationId;
}
