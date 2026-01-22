package com.example.demo.scolarite.controller;

import com.example.demo.scolarite.dto.ScolariteCandidatDto;
import com.example.demo.scolarite.service.ScolariteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scolarite")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SCOLARITE')")
@CrossOrigin(origins = "*")
public class ScolariteController {

    private final ScolariteService scolariteService;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(scolariteService.getMyLabStats());
    }

    @GetMapping("/dossiers")
    public ResponseEntity<?> getDossiers(@RequestParam(required = false) String etat) {
        return ResponseEntity.ok(scolariteService.getMyLabDossiers(etat));
    }

    @GetMapping("/dossiers/{id}")
    public ResponseEntity<?> getDossierDetail(@PathVariable Long id) {
        return ResponseEntity.ok(scolariteService.getDossierDetail(id));
    }

    @PutMapping("/dossiers/{id}/validation")
    public ResponseEntity<?> validerDossier(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> validationData) {
        return ResponseEntity.ok(scolariteService.validerDossier(id, validationData));
    }

    @GetMapping("/candidats")
    public ResponseEntity<List<ScolariteCandidatDto>> getCandidats(
            @RequestParam(required = false) String cne,
            @RequestParam(required = false) Long formationId) {
        return ResponseEntity.ok(scolariteService.getMyLabCandidats(cne, formationId));
    }
    
    @GetMapping("/labo-info")
    public ResponseEntity<?> getLaboInfo() {
        var scolarite = scolariteService.getCurrentScolarite();
        return ResponseEntity.ok(java.util.Map.of(
            "laboName", scolarite.getLaboratoire().getNomLaboratoire(),
            "laboId", scolarite.getLaboratoire().getId()
        ));
    }
}
