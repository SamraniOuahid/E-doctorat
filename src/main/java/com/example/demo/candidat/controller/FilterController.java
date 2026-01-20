package com.example.demo.candidat.controller;

import com.example.demo.candidat.repository.SujetRepository;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/filters")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FilterController {

    private final SujetRepository sujetRepository;
    private final UserRepository userRepository;

    /**
     * Get all filter options extracted from existing sujets
     * GET /api/filters/all (public endpoint)
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllFilters() {
        List<Sujet> allSujets = sujetRepository.findAll();

        Map<String, Object> filters = new HashMap<>();

        // 1. Extract unique professor user IDs
        Set<Long> userIds = allSujets.stream()
                .filter(s -> s.getProfesseur() != null && s.getProfesseur().getUserId() != null)
                .map(s -> s.getProfesseur().getUserId())
                .collect(Collectors.toSet());

        // 2. Fetch all user accounts for these professors
        Map<Long, String> userNames = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(UserAccount::getId, UserAccount::getFullName));

        // 3. Extract unique professors with their names
        Set<Map<String, Object>> professeurs = allSujets.stream()
                .filter(s -> s.getProfesseur() != null)
                .map(s -> {
                    Map<String, Object> prof = new HashMap<>();
                    Long profId = s.getProfesseur().getId();
                    Long userId = s.getProfesseur().getUserId();
                    String fullName = userNames.getOrDefault(userId, "Professeur " + profId);

                    prof.put("id", profId);
                    prof.put("nom", fullName);
                    prof.put("prenom", ""); // Frontend expects nom and prenom
                    return prof;
                })
                .collect(Collectors.toMap(
                        m -> (Long) m.get("id"),
                        m -> m,
                        (existing, replacement) -> existing))
                .values().stream()
                .collect(Collectors.toSet());

        // Extract unique laboratories
        Set<Map<String, Object>> laboratoires = allSujets.stream()
                .filter(s -> s.getProfesseur() != null && s.getProfesseur().getLaboratoire() != null)
                .map(s -> {
                    Map<String, Object> labo = new HashMap<>();
                    labo.put("id", s.getProfesseur().getLaboratoire().getId());
                    labo.put("nom", s.getProfesseur().getLaboratoire().getNomLaboratoire());
                    return labo;
                })
                .collect(Collectors.toMap(
                        m -> (Long) m.get("id"),
                        m -> m,
                        (existing, replacement) -> existing))
                .values().stream()
                .collect(Collectors.toSet());

        // Extract unique formations doctorales
        Set<Map<String, Object>> formations = allSujets.stream()
                .filter(s -> s.getFormationDoctorale() != null)
                .map(s -> {
                    Map<String, Object> formation = new HashMap<>();
                    formation.put("id", s.getFormationDoctorale().getId());
                    formation.put("nom", s.getFormationDoctorale().getTitre());
                    return formation;
                })
                .collect(Collectors.toMap(
                        m -> (Long) m.get("id"),
                        m -> m,
                        (existing, replacement) -> existing))
                .values().stream()
                .collect(Collectors.toSet());

        // Extract unique establishments from professors
        Set<Map<String, Object>> etablissements = allSujets.stream()
                .filter(s -> s.getProfesseur() != null && s.getProfesseur().getEtablissement() != null)
                .map(s -> {
                    Map<String, Object> etab = new HashMap<>();
                    etab.put("id", s.getProfesseur().getEtablissement().getIdEtablissement());
                    etab.put("nom", s.getProfesseur().getEtablissement().getNomEtablissement());
                    return etab;
                })
                .collect(Collectors.toMap(
                        m -> (String) m.get("id"),
                        m -> m,
                        (existing, replacement) -> existing))
                .values().stream()
                .collect(Collectors.toSet());

        filters.put("professeurs", new ArrayList<>(professeurs));
        filters.put("laboratoires", new ArrayList<>(laboratoires));
        filters.put("formations", new ArrayList<>(formations));
        filters.put("etablissements", new ArrayList<>(etablissements));
        
        // Add domaines (static for now - can be enhanced later)
        List<Map<String, Object>> domaines = new ArrayList<>();
        String[] domaineNames = {"Informatique", "Mathématiques", "Physique", "Chimie", "Biologie", "Génie", "Économie", "Droit", "Lettres", "Médecine"};
        for (int i = 0; i < domaineNames.length; i++) {
            Map<String, Object> domaine = new HashMap<>();
            domaine.put("id", (long) (i + 1));
            domaine.put("nom", domaineNames[i]);
            domaines.add(domaine);
        }
        filters.put("domaines", domaines);
        
        // Add typesThese (static)
        List<String> typesThese = Arrays.asList("THESE_DOCTORALE", "THESE_INGENIEUR", "THESE_SPECIALITE");
        filters.put("typesThese", typesThese);
        
        // Add annees (last 5 years)
        List<Integer> annees = new ArrayList<>();
        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 5; i++) {
            annees.add(currentYear - i);
        }
        filters.put("annees", annees);

        return ResponseEntity.ok(filters);
    }
}
