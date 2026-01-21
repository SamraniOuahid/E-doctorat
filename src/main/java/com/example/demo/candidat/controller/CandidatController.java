package com.example.demo.candidat.controller;

import com.example.demo.candidat.dto.*;
import com.example.demo.candidat.model.*;
import com.example.demo.candidat.repository.*;
import com.example.demo.candidat.service.*;
import com.example.demo.professeur.model.Sujet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidats")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CandidatController {

    private final CandidatService candidatService;

    // 1) FILTERS API
    // GET
    // /api/candidats/sujets?keyword=Java&laboId=1&formationId=2&etablissementId=3&professeurId=5
    @GetMapping("/sujets")
    public ResponseEntity<List<Sujet>> searchSujets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long laboId,
            @RequestParam(required = false) Long formationId,
            @RequestParam(required = false) Long etablissementId,
            @RequestParam(required = false) Long professeurId,
            @RequestParam(required = false) Boolean disponible) {

        List<Sujet> results = candidatService.searchSujets(keyword, laboId, formationId, etablissementId, professeurId,
                disponible);
        return ResponseEntity.ok(results);
    }

    // 2) POSTULER API (Panier)
    // POST /api/candidats/{id}/postuler
    // Body: { "choix": [ { "sujetId": 1, "pathRecherche": "..." }, ... ] }
    @PostMapping("/{id}/postuler")
    public ResponseEntity<String> postuler(@PathVariable Long id, @RequestBody CandidatureDTO dto) {
        try {
            candidatService.postuler(id, dto);
            return ResponseEntity.ok("Candidature enregistrée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/choix")
    public ResponseEntity<List<CandidatChoix>> getChoix(@PathVariable Long id) {
        return ResponseEntity.ok(candidatService.getChoixByCandidatId(id));
    }

    // 3) NOTIFICATIONS API
    // GET /api/candidats/{id}/notifications
    @GetMapping("/{id}/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(candidatService.getMyNotifications(id));
    }

    // 4) GET candidat
    // GET /api/candidats/{id}
    @GetMapping("/{id}")
    public Candidat getCandidat(@PathVariable Long id) {
        return candidatService.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable"));
    }

    // 6) UPDATE profile
    // PUT /api/candidats/{id}/profile
    // import com.example.demo.candidat.dto.CandidatUpdateDTO;

    @PutMapping("/{id}")
    public ResponseEntity<Candidat> updateProfil(
            @PathVariable Long id,
            @RequestBody CandidatUpdateDTO candidatDto // <--- Ici aussi, utilise le DTO
    ) {
        System.out.println("DTO REÇU : " + candidatDto);
        Candidat updatedCandidat = candidatService.updateCandidat(id, candidatDto);
        return ResponseEntity.ok(updatedCandidat);
    }

    // 7) ADD diplome
    // POST /api/candidats/{id}/diplomes
    @PostMapping("/{id}/diplomes")
    public Diplome addDiplome(@PathVariable Long id, @RequestBody DiplomeDto dto) {
        Diplome d = new Diplome();
        d.setIntitule(dto.intitule());
        d.setType(dto.type());
        d.setDateCommission(dto.dateCommission());
        d.setMention(dto.mention());
        d.setPays(dto.pays());
        d.setEtablissement(dto.etablissement());
        d.setSpecialite(dto.specialite());
        d.setVille(dto.ville());
        d.setProvince(dto.province());
        d.setMoyenGenerale(dto.moyenGenerale());

        return candidatService.addDiplome(id, d);
    }

    // --- UPLOAD ROUTES ---
    @PostMapping("/upload-projet")
    public ResponseEntity<String> uploadProjet(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            String path = candidatService.saveProjetRecherche(file);
            return ResponseEntity.ok(path);
        } catch (java.io.IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'upload: " + e.getMessage());
        }
    }

    // GET current user's candidat profile
    @GetMapping("/me")
    public ResponseEntity<Candidat> getCurrentProfile() {
        Candidat candidat = candidatService.getCurrentCandidat();
        return ResponseEntity.ok(candidat);
    }
}
