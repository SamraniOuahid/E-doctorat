package com.example.demo.directeur.pole.controller;


import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.example.demo.directeur.pole.service.DirecteurPoleService;

@RestController
@RequestMapping("/api/directeur/pole")
@RequiredArgsConstructor
public class DirecteurPoleController {

    private final DirecteurPoleService service;

    @GetMapping("/candidats")
    public Object getCandidats() {
        return service.getAllCandidats();
    }

    @GetMapping("/sujets")
    public Object getSujets() {
        return service.getAllSujets();
    }

    @GetMapping("/resultats")
    public Object getResultats() {
        return service.getResultats();
    }

    @GetMapping("/inscriptions")
    public Object getInscriptions() {
        return service.getInscriptions();
    }

    @GetMapping("/rapport-inscription")
    public byte[] downloadRapport() {
        return service.telechargerRapportInscription();
    }
}
