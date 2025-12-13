package com.example.demo.candidat.dto;

import java.time.LocalDate;

public record DiplomeDto(
        String intitule,
        String type,
        LocalDate dateCommission,
        String mention,
        String pays,
        String etablissement,
        String specialite,
        String ville,
        String province,
        Double moyenGenerale
) {
}
