package com.example.demo.professeur.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.service.ProfesseurService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professeur")
@RequiredArgsConstructor
public class ProfesseurController {

    private final ProfesseurService professeurService;

    @GetMapping("/{id}/sujets")
    public List<Sujet> getSujets(@PathVariable Long id) {
        return professeurService.getSujetsDuProfesseur(id);
    }

    @GetMapping("/sujet/{id}/candidats")
    public List<Inscription> getCandidats(@PathVariable Long id) {
        return professeurService.getCandidatsParSujet(id);
    }

    @PostMapping("/inscription/{id}/accepter")
    public void accepter(@PathVariable Long id) {
        professeurService.accepterCandidature(id);
    }

    @PostMapping("/inscription/{id}/refuser")
    public void refuser(@PathVariable Long id) {
        professeurService.refuserCandidature(id);
    }
}

