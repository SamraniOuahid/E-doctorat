package com.example.demo.directeur.pole.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.demo.directeur.pole.dto.*;
import com.example.demo.directeur.pole.service.DirecteurPoleService;

import java.util.List;

@RestController
@RequestMapping("/api/directeur/pole")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DIRECTEUR_POLE')")
@Slf4j
public class DirecteurPoleController {

    private final DirecteurPoleService service;

    // Dashboard Statistics
    @GetMapping("/stats")
    public ResponseEntity<PoleStatsDto> getStats() {
        log.debug("Directeur Pole requesting dashboard stats");
        return ResponseEntity.ok(service.getStats());
    }

    // Candidats
    @GetMapping("/candidats")
    public ResponseEntity<List<PoleCandidatDto>> getCandidats(
            @RequestParam(required = false) Long formationId) {
        log.debug("Directeur Pole requesting candidats, formationId={}", formationId);
        return ResponseEntity.ok(service.getAllCandidats(formationId));
    }

    // Sujets
    @GetMapping("/sujets")
    public ResponseEntity<List<PoleSujetDto>> getSujets(
            @RequestParam(required = false) Long formationId,
            @RequestParam(required = false) Long laboId) {
        log.debug("Directeur Pole requesting sujets, formationId={}, laboId={}", formationId, laboId);
        return ResponseEntity.ok(service.getAllSujets(formationId, laboId));
    }

    // Resultats
    @GetMapping("/resultats")
    public ResponseEntity<List<PoleResultatDto>> getResultats() {
        log.debug("Directeur Pole requesting resultats");
        return ResponseEntity.ok(service.getResultats());
    }

    // Liste Principale (admis)
    @GetMapping("/resultats/principales")
    public ResponseEntity<List<PoleResultatDto>> getListePrincipale() {
        log.debug("Directeur Pole requesting liste principale");
        return ResponseEntity.ok(service.getListePrincipale());
    }

    // Liste d'attente
    @GetMapping("/resultats/attente")
    public ResponseEntity<List<PoleResultatDto>> getListeAttente() {
        log.debug("Directeur Pole requesting liste attente");
        return ResponseEntity.ok(service.getListeAttente());
    }

    // Inscriptions
    @GetMapping("/inscriptions")
    public ResponseEntity<List<PoleInscriptionDto>> getInscriptions() {
        log.debug("Directeur Pole requesting inscriptions");
        return ResponseEntity.ok(service.getInscriptions());
    }

    // Télécharger rapport inscription
    @GetMapping("/rapport-inscription")
    public ResponseEntity<byte[]> downloadRapport() {
        log.debug("Directeur Pole downloading rapport inscription");
        byte[] pdfBytes = service.telechargerRapportInscription();
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=rapport_inscriptions.pdf")
                .body(pdfBytes);
    }

    // Formations doctorales
    @GetMapping("/formations")
    public ResponseEntity<List<PoleFormationDto>> getFormations() {
        log.debug("Directeur Pole requesting formations doctorales");
        return ResponseEntity.ok(service.getFormations());
    }

    // Laboratoires
    @GetMapping("/laboratoires")
    public ResponseEntity<List<PoleLaboratoireDto>> getLaboratoires() {
        log.debug("Directeur Pole requesting laboratoires");
        return ResponseEntity.ok(service.getLaboratoires());
    }

    // ========== Calendar Management ==========

    // Get all phases
    @GetMapping("/calendrier")
    public ResponseEntity<List<PhaseDto>> getCalendrier() {
        log.debug("Directeur Pole requesting calendar");
        return ResponseEntity.ok(service.getCalendrier());
    }

    // Get active phase
    @GetMapping("/phase-active")
    public ResponseEntity<PhaseDto> getPhaseActive() {
        log.debug("Directeur Pole requesting active phase");
        PhaseDto phase = service.getPhaseActive();
        if (phase == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(phase);
    }

    // Update a phase's dates
    @PutMapping("/calendrier/{phaseId}")
    public ResponseEntity<PhaseDto> updatePhase(
            @PathVariable Long phaseId,
            @RequestBody PhaseUpdateDto dto) {
        log.debug("Directeur Pole updating phase {}: {} to {}", phaseId, dto.getDateDebut(), dto.getDateFin());
        return ResponseEntity.ok(service.updatePhase(phaseId, dto.getDateDebut(), dto.getDateFin()));
    }

    // ========== Results Publication ==========

    // Get publication status for both lists
    @GetMapping("/resultats/status")
    public ResponseEntity<java.util.Map<String, PublicationStatusDto>> getPublicationStatus() {
        log.debug("Directeur Pole requesting publication status");
        return ResponseEntity.ok(service.getPublicationStatus());
    }

    // Publish a list (IRREVERSIBLE!)
    @PostMapping("/resultats/publier/{type}")
    public ResponseEntity<?> publishList(
            @PathVariable String type,
            @org.springframework.security.core.annotation.AuthenticationPrincipal 
            org.springframework.security.core.userdetails.UserDetails user) {
        log.info("Directeur Pole {} publishing list: {}", user.getUsername(), type);
        
        try {
            PublicationStatusDto result = service.publishList(type, user.getUsername());
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                java.util.Map.of("error", e.getMessage())
            );
        }
    }

    // ========== CSV Export ==========

    // Export Liste Principale as CSV
    @GetMapping("/resultats/principales/csv")
    public ResponseEntity<byte[]> exportListePrincipaleCSV() {
        log.debug("Directeur Pole exporting LP to CSV");
        try {
            byte[] csvData = service.exportListePrincipaleCSV();
            return ResponseEntity.ok()
                    .header("Content-Type", "text/csv; charset=UTF-8")
                    .header("Content-Disposition", "attachment; filename=\"Liste_Principale_" + 
                            java.time.LocalDate.now() + ".csv\"")
                    .body(csvData);
        } catch (Exception e) {
            log.error("Error exporting LP CSV", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Export Liste d'Attente as CSV
    @GetMapping("/resultats/attente/csv")
    public ResponseEntity<byte[]> exportListeAttenteCSV() {
        log.debug("Directeur Pole exporting LA to CSV");
        try {
            byte[] csvData = service.exportListeAttenteCSV();
            return ResponseEntity.ok()
                    .header("Content-Type", "text/csv; charset=UTF-8")
                    .header("Content-Disposition", "attachment; filename=\"Liste_Attente_" + 
                            java.time.LocalDate.now() + ".csv\"")
                    .body(csvData);
        } catch (Exception e) {
            log.error("Error exporting LA CSV", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Export all inscriptions as CSV
    @GetMapping("/inscriptions/csv")
    public ResponseEntity<byte[]> exportInscriptionsCSV() {
        log.debug("Directeur Pole exporting inscriptions to CSV");
        try {
            byte[] csvData = service.exportInscriptionsCSV();
            return ResponseEntity.ok()
                    .header("Content-Type", "text/csv; charset=UTF-8")
                    .header("Content-Disposition", "attachment; filename=\"Inscriptions_" + 
                            java.time.LocalDate.now() + ".csv\"")
                    .body(csvData);
        } catch (Exception e) {
            log.error("Error exporting inscriptions CSV", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}


