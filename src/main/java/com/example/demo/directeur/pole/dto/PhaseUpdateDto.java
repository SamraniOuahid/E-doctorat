package com.example.demo.directeur.pole.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PhaseUpdateDto {
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
