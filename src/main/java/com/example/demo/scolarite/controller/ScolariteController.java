package com.example.demo.scolarite.controller;

import com.example.demo.scolarite.dto.ScolariteCandidatDto;
import com.example.demo.scolarite.dto.ScolariteDossierDto;
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
    public ResponseEntity<org.springframework.data.domain.Page<ScolariteDossierDto>> getDossiers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String etat,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ResponseEntity.ok(scolariteService.getAllDossiers(search, etat, pageable));
    }

    @GetMapping("/candidats")
    public ResponseEntity<org.springframework.data.domain.Page<ScolariteCandidatDto>> getCandidats(
            @RequestParam(required = false) String cne,
            @RequestParam(required = false) Long formationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ResponseEntity.ok(scolariteService.getMyLabCandidats(cne, formationId, pageable));
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
