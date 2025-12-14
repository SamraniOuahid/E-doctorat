package com.example.demo.professeur.controller;

import com.example.demo.professeur.dto.CandidatForProfDto;
import com.example.demo.professeur.service.ProfesseurServicev1;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professeurs/{profId}/candidats")
public class ProfesseurControllerv1 {

    private final ProfesseurServicev1 service;

    public ProfesseurControllerv1(ProfesseurServicev1 service) {
        this.service = service;
    }

    // 1) ALL candidates who applied to ANY subject of this prof
    @GetMapping
    public List<CandidatForProfDto> getCandidatsForProf(@PathVariable Long profId,
                                                        @RequestParam(required = false) Long sujetId) {

        if (sujetId != null) {
            return service.getCandidatsBySujet(profId, sujetId);
        }
        return service.getCandidatsByProf(profId);
    }
}
