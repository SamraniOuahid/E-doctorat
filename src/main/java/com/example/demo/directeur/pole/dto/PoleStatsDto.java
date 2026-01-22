package com.example.demo.directeur.pole.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoleStatsDto {
    private long totalCandidats;
    private long formationsDoctorales;
    private long sujetsValides;
    private long commissionsPlanifiees;
}
