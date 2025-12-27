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
    private final CandidatRepository candidatRepository;

    // 1) FILTERS API
    // GET /api/candidats/sujets?keyword=Java&laboId=1&formationId=2&etablissementId=3
    @GetMapping("/sujets")
    public ResponseEntity<List<Sujet>> searchSujets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long laboId,
            @RequestParam(required = false) Long formationId,
            @RequestParam(required = false) Long etablissementId) {

        List<Sujet> results = candidatService.searchSujets(keyword, laboId, formationId, etablissementId);
        return ResponseEntity.ok(results);
    }

    // 2) POSTULER API (Panier)
    // POST /api/candidats/{id}/postuler
    // Body: [1,5,9]
    @PostMapping("/{id}/postuler")
    public ResponseEntity<String> postuler(@PathVariable Long id, @RequestBody List<Long> sujetIds) {
        try {
            candidatService.postuler(id, sujetIds);
            return ResponseEntity.ok("Candidature enregistrée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
        return candidatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable"));
    }

    // 5) ADD candidat
    // POST /api/candidats
    @PostMapping
    public Candidat addCandidat(@RequestBody Candidat c) {
        return candidatRepository.save(c);
    }

    // 6) UPDATE profile
    // PUT /api/candidats/{id}/profile
    @PutMapping("/{id}/profile")
    public Candidat updateProfile(@PathVariable Long id, @RequestBody CandidatProfilDto dto) {
        Candidat c = new Candidat();
        c.setNomCandidatAr(dto.nomCandidatAr());
        c.setPrenomCandidatAr(dto.prenomCandidatAr());
        c.setAdresse(dto.adresse());
        c.setTelCandidat(dto.telCandidat());
        c.setPathCv(dto.pathCv());
        c.setPathPhoto(dto.pathPhoto());

        return candidatService.updateCandidat(id, c);
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
}
