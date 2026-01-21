package com.example.demo.scolarite.controller;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.scolarite.dto.ValidationDossierDto;
import com.example.demo.scolarite.model.EtatDossier;
import com.example.demo.scolarite.service.ScolariteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scolarite")
@RequiredArgsConstructor
public class ScolariteController {

    private final ScolariteService scolariteService;

    // 1. Lister les dossiers
    @GetMapping("/dossiers")
    public ResponseEntity<List<Candidat>> getDossiers(@RequestParam(required = false) EtatDossier etat) {
        return ResponseEntity.ok(scolariteService.getAllDossiers(etat));
    }

    // 2. Voir un dossier en détail
    @GetMapping("/dossiers/{id}")
    public ResponseEntity<Candidat> getDossierDetail(@PathVariable Long id) {
        return ResponseEntity.ok(scolariteService.getDossier(id));
    }

    // 3. Valider / Commenter un dossier
    @PutMapping("/dossiers/{id}/validation")
    public ResponseEntity<Candidat> validerDossier(
            @PathVariable Long id,
            @RequestBody ValidationDossierDto dto) {
        return ResponseEntity.ok(scolariteService.validerDossier(id, dto));
    }

    // 4. Statistiques Dashboard
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(scolariteService.getDashboardStats());
    }
}
