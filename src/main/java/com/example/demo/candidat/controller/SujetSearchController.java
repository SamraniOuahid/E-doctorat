package com.example.demo.candidat.controller;

import com.example.demo.candidat.specification.SujetSpecification;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.candidat.repository.SujetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sujets")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SujetSearchController {

    private final SujetRepository sujetRepository;

    /**
     * Global search endpoint for all users
     * GET
     * /api/sujets/search?keyword=Java&laboId=1&formationId=2&etablissementId=3&professeurId=5&disponible=true&statut=PUBLIE&sort=date
     */
    @GetMapping("/search")
    public ResponseEntity<List<Sujet>> searchSujets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long laboId,
            @RequestParam(required = false) Long formationId,
            @RequestParam(required = false) Long professeurId,
            @RequestParam(required = false) Long domaineId,
            @RequestParam(required = false) String typeThese,
            @RequestParam(required = false) String annee,
            @RequestParam(required = false, defaultValue = "true") Boolean disponible,
            @RequestParam(required = false, defaultValue = "PUBLIE") String statut,
            @RequestParam(required = false, defaultValue = "date") String sort) {

        Specification<Sujet> spec = SujetSpecification.getSujetsByFilter(
                keyword, laboId, formationId, professeurId, disponible);

        List<Sujet> results = sujetRepository.findAll(spec);

        // Apply additional filters if needed (domaineId, typeThese, annee, statut,
        // sort)
        // These can be implemented later based on the data model

        return ResponseEntity.ok(results);
    }
}
