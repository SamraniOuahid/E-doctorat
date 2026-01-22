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

    // POST /api/directeur-labo/{laboId}/sujets
    @PostMapping("/{laboId}/sujets")
    public LaboSujetDto createSujet(@PathVariable Long laboId, @RequestBody LaboSujetDto dto) {
        return directeurLaboService.createSujet(laboId, dto);
    }

    // PUT /api/directeur-labo/{laboId}/sujets/{sujetId}
    @PutMapping("/{laboId}/sujets/{sujetId}")
    public LaboSujetDto updateSujet(@PathVariable Long laboId, @PathVariable Long sujetId, @RequestBody LaboSujetDto dto) {
        return directeurLaboService.updateSujet(laboId, sujetId, dto);
    }

    // DELETE /api/directeur-labo/{laboId}/sujets/{sujetId}
    @DeleteMapping("/{laboId}/sujets/{sujetId}")
    public ResponseEntity<Void> deleteSujet(@PathVariable Long laboId, @PathVariable Long sujetId) {
        directeurLaboService.deleteSujet(laboId, sujetId);
        return ResponseEntity.noContent().build();
    }

    // POST /api/directeur-labo/{laboId}/sujets/csv
    @PostMapping(value = "/{laboId}/sujets/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadSujetsCsv(
            @PathVariable Long laboId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            directeurLaboService.importSujetsCsv(laboId, file.getInputStream());
            return ResponseEntity.ok("Sujets imported successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error importing CSV: " + e.getMessage());
        }
    }

    // GET /api/directeur-labo/{laboId}/formations
    @GetMapping("/{laboId}/formations")
    public java.util.List<java.util.Map<String, Object>> getFormations(@PathVariable Long laboId) {
        return directeurLaboService.getFormationsByLabo(laboId);
    }

    // ================== CONSULTER CANDIDATS ==================

    // GET /api/directeur-labo/{laboId}/candidats
    // GET /api/directeur-labo/{laboId}/candidats?sujetId=12
    @GetMapping("/{laboId}/candidats")
    public List<LaboCandidatDto> getCandidats(
            @PathVariable Long laboId,
            @RequestParam(required = false) Long sujetId) {
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

    // GET /api/directeur-labo/{laboId}/professeurs
    @GetMapping("/{laboId}/professeurs")
    public List<com.example.demo.directeur.labo.dto.ProfessorDto> getProfesseurs(@PathVariable Long laboId) {
        return directeurLaboService.getProfesseursDuLabo(laboId);
    }

    // GET /api/directeur-labo/professeurs/all
    @GetMapping("/professeurs/all")
    public List<com.example.demo.directeur.labo.dto.ProfessorDto> getProfesseursAll() {
        return directeurLaboService.getAllProfesseurs();
    }

    // GET /api/directeur-labo/my-labo
    @GetMapping("/my-labo")
    public ResponseEntity<java.util.Map<String, Object>> getMyLabo(
            @org.springframework.security.core.annotation.AuthenticationPrincipal 
            org.springframework.security.core.userdetails.UserDetails userDetails) {
        return ResponseEntity.ok(directeurLaboService.getMyLaboInfo(userDetails.getUsername()));
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

    // ================== GESTION DES COMMISSIONS ==================
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DirecteurLaboController.class);

    @GetMapping("/{laboId}/commissions")
    public List<com.example.demo.directeur.labo.dto.CommissionDto> getCommissions(@PathVariable Long laboId) {
        logger.info("GET /api/directeur-labo/{}/commissions called", laboId);
        return directeurLaboService.getCommissionsByLabo(laboId);
    }

    @PostMapping("/{laboId}/commissions")
    public com.example.demo.directeur.labo.dto.CommissionDto createCommission(@PathVariable Long laboId, @RequestBody com.example.demo.directeur.labo.dto.CommissionDto dto) {
        logger.info("POST /api/directeur-labo/{}/commissions called", laboId);
        return directeurLaboService.createCommission(laboId, dto);
    }

    @PutMapping("/{laboId}/commissions/{id}")
    public com.example.demo.directeur.labo.dto.CommissionDto updateCommission(@PathVariable Long laboId, @PathVariable Long id, @RequestBody com.example.demo.directeur.labo.dto.CommissionDto dto) {
        logger.info("PUT /api/directeur-labo/{}/commissions/{} called", laboId, id);
        return directeurLaboService.updateCommission(laboId, id, dto);
    }

    @DeleteMapping("/{laboId}/commissions/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable Long laboId, @PathVariable Long id) {
        logger.info("DELETE /api/directeur-labo/{}/commissions/{} called", laboId, id);
        directeurLaboService.deleteCommission(laboId, id);
        return ResponseEntity.noContent().build();
    }
}
