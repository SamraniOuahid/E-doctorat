package com.example.demo.candidat.controller;

import com.example.demo.candidat.model.Notification;
import com.example.demo.candidat.service.CandidatService;
import com.example.demo.professeur.model.Sujet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidats")
@CrossOrigin(origins = "*") //  Allow ur Frontend to talk to this Backend
public class CandidatController {

    @Autowired
    private CandidatService candidatService;

    // 1. FILTERS API
    // Example URL: /api/candidats/sujets?keyword=Java&laboId=1
    @GetMapping("/sujets")
    public ResponseEntity<List<Sujet>> searchSujets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long laboId,
            @RequestParam(required = false) Long formationId,
            @RequestParam(required = false) Long etablissementId) {

        List<Sujet> results = candidatService.searchSujets(keyword, laboId, formationId, etablissementId);
        return ResponseEntity.ok(results);
    }

    // 2. POSTULER API (Panier)
    // Example Body: [1, 5, 9]
    @PostMapping("/{id}/postuler")
    public ResponseEntity<String> postuler(@PathVariable Long id, @RequestBody List<Long> sujetIds) {
        try {
            candidatService.postuler(id, sujetIds);
            return ResponseEntity.ok("Candidature enregistrée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. NOTIFICATIONS API
    @GetMapping("/{id}/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(candidatService.getMyNotifications(id));
    }
}