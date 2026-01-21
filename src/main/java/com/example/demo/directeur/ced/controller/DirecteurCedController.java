package com.example.demo.directeur.ced.controller;

import com.example.demo.directeur.ced.service.DirecteurCedService;
import com.example.demo.candidat.model.CandidatChoix;
import com.example.demo.professeur.model.Examiner;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.model.Commission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/ced")
// @PreAuthorize("hasRole('DIRECTEUR_CED')") // A activer quand la sécurité sera complète
public class DirecteurCedController {

    @Autowired
    private DirecteurCedService directeurCedService;

    // 1. Consulter les Sujets d'une formation du CED
    @GetMapping("/{cedId}/formations/{formationId}/sujets")
    public ResponseEntity<List<Sujet>> getSujets(
            @PathVariable Long cedId,
            @PathVariable Long formationId) {
        return ResponseEntity.ok(directeurCedService.getSujetsByFormation(cedId, formationId));
    }

    // 2. Consulter les Candidats d'une formation (via leurs postulations)
    @GetMapping("/{cedId}/formations/{formationId}/candidats")
    public ResponseEntity<List<CandidatChoix>> getCandidats(
            @PathVariable Long cedId,
            @PathVariable Long formationId) {
        return ResponseEntity.ok(directeurCedService.getCandidatsByFormation(cedId, formationId));
    }

    // 3. Consulter les Résultats (Notes/Décisions)
    @GetMapping("/{cedId}/resultats")
    public ResponseEntity<List<Examiner>> getResultats(@PathVariable Long cedId) {
        return ResponseEntity.ok(directeurCedService.getResultatsByCed(cedId));
    }

    // 4. Consulter les Inscrits (Validés)
    @GetMapping("/{cedId}/inscrits")
    public ResponseEntity<List<Inscription>> getInscrits(@PathVariable Long cedId) {
        return ResponseEntity.ok(directeurCedService.getInscritsByCed(cedId));
    }

    // 5. Télécharger le rapport d'inscription (CSV)
    @GetMapping("/{cedId}/rapports/inscription")
    public ResponseEntity<InputStreamResource> downloadRapport(@PathVariable Long cedId) {
        ByteArrayInputStream stream = directeurCedService.generateRapportInscriptionCsv(cedId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=rapport_inscriptions_ced_" + cedId + ".csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new InputStreamResource(stream));
    }

    // 6. Obtenir l'ID du CED de l'utilisateur connecté (par email)
    @GetMapping("/me/id")
    public ResponseEntity<Long> getMyCedId(@RequestParam String email) {
        return ResponseEntity.ok(directeurCedService.getCedIdByEmail(email));
    }

    // 7. Consulter les Commissions du CED
    @GetMapping("/{cedId}/commissions")
    public ResponseEntity<List<Commission>> getCommissions(@PathVariable Long cedId) {
        return ResponseEntity.ok(directeurCedService.getCommissionsByCed(cedId));
    }
}