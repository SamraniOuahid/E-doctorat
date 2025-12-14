package com.example.demo.professeur.controller;

import com.example.demo.professeur.dto.SujetDto;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.service.SujetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professeurs/{profId}/sujets")
public class SujetController {

    private final SujetService sujetService;

    public SujetController(SujetService sujetService) {
        this.sujetService = sujetService;
    }

    // GET: list all my subjects
    @GetMapping
    public List<Sujet> getMySujets(@PathVariable Long profId) {
        return sujetService.getSujetsByProf(profId);
    }

    // POST: create new subject (with PA rule)
    @PostMapping
    public ResponseEntity<Sujet> createSujet(@PathVariable Long profId,
                                             @RequestBody SujetDto dto) {
        Sujet created = sujetService.createSujet(profId, dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // PUT: update subject
    @PutMapping("/{sujetId}")
    public Sujet updateSujet(@PathVariable Long profId,
                             @PathVariable Long sujetId,
                             @RequestBody SujetDto dto) {
        return sujetService.updateSujet(profId, sujetId, dto);
    }

    // DELETE: delete subject
    @DeleteMapping("/{sujetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSujet(@PathVariable Long profId,
                            @PathVariable Long sujetId) {
        sujetService.deleteSujet(profId, sujetId);
    }
}
