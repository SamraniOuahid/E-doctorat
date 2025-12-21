package com.example.demo.directeur.labo.controller;

import com.example.demo.directeur.labo.dto.LaboCandidatDto;
import com.example.demo.directeur.labo.dto.LaboResultatDto;
import com.example.demo.directeur.labo.dto.LaboSujetDto;
import com.example.demo.directeur.labo.service.DirecteurLaboService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directeur-labo")
@RequiredArgsConstructor
public class DirecteurLaboController {

    private final DirecteurLaboService directeurLaboService;

    // ================== CONSULTER SUJETS ==================

    // GET /api/directeur-labo/{laboId}/sujets
    @GetMapping("/{laboId}/sujets")
    public List<LaboSujetDto> getSujets(@PathVariable Long laboId) {
        return directeurLaboService.getSujetsDuLabo(laboId);
    }

    // ================== CONSULTER CANDIDATS ==================

    // GET /api/directeur-labo/{laboId}/candidats
    // GET /api/directeur-labo/{laboId}/candidats?sujetId=12
    @GetMapping("/{laboId}/candidats")
    public List<LaboCandidatDto> getCandidats(
            @PathVariable Long laboId,
            @RequestParam(required = false) Long sujetId
    ) {
        return directeurLaboService.getCandidatsDuLabo(laboId, sujetId);
    }

    // ================== VOIR LES RESULTATS ==================

    // GET /api/directeur-labo/{laboId}/resultats
    @GetMapping("/{laboId}/resultats")
    public List<LaboResultatDto> getResultats(@PathVariable Long laboId) {
        return directeurLaboService.getResultatsDuLabo(laboId);
    }

    // ================== VOIR LES INSCRITS ==================

    // GET /api/directeur-labo/{laboId}/inscrits
    @GetMapping("/{laboId}/inscrits")
    public List<LaboResultatDto> getInscrits(@PathVariable Long laboId) {
        return directeurLaboService.getInscritsDuLabo(laboId);
    }

    // ================== TELECHARGER PV GLOBAL ==================

    // GET /api/directeur-labo/{laboId}/pv-global
    @GetMapping("/{laboId}/pv-global")
    public ResponseEntity<byte[]> downloadPvGlobal(@PathVariable Long laboId) {
        byte[] pdf = directeurLaboService.genererPvGlobal(laboId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"pv-global-labo-" + laboId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
