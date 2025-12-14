package com.example.demo.professeur.controller;

import com.example.demo.professeur.dto.CandidatForProfDto;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.service.ProfesseurService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professeurs")
@RequiredArgsConstructor
public class ProfesseurController {

    private final ProfesseurService professeurService;

    // ================== 1) Mes sujets : voir tous mes sujets ==================
    // GET /api/professeurs/{profId}/sujets
    @GetMapping("/{profId}/sujets")
    public List<Sujet> getSujets(@PathVariable Long profId) {
        return professeurService.getSujetsDuProfesseur(profId);
    }

    // ================== 2) Mes candidats + infos candidats ====================
    // GET /api/professeurs/{profId}/candidats
    //    -> tous les candidats qui ont postulé sur un sujet de ce prof
    //
    // GET /api/professeurs/{profId}/candidats?sujetId=12
    //    -> seulement les candidats pour le sujet 12 de ce prof
    @GetMapping("/{profId}/candidats")
    public List<CandidatForProfDto> getCandidatsForProf(
            @PathVariable Long profId,
            @RequestParam(required = false) Long sujetId
    ) {
        if (sujetId != null) {
            return professeurService.getCandidatsBySujet(profId, sujetId);
        }
        return professeurService.getCandidatsByProf(profId);
    }

    // (optionnel) si vous voulez aussi la liste brute des inscriptions par sujet
    // GET /api/professeurs/sujets/{sujetId}/inscriptions
    @GetMapping("/sujets/{sujetId}/inscriptions")
    public List<Inscription> getInscriptionsBySujet(@PathVariable Long sujetId) {
        return professeurService.getInscriptionsBySujet(sujetId);
    }

    // ================== 3) Examiner : accepter / refuser ======================
    // POST /api/professeurs/inscriptions/{inscriptionId}/accepter
    @PostMapping("/inscriptions/{inscriptionId}/accepter")
    public void accepter(@PathVariable Long inscriptionId) {
        professeurService.accepterCandidature(inscriptionId);
    }

    // POST /api/professeurs/inscriptions/{inscriptionId}/refuser
    @PostMapping("/inscriptions/{inscriptionId}/refuser")
    public void refuser(@PathVariable Long inscriptionId) {
        professeurService.refuserCandidature(inscriptionId);
    }
}
