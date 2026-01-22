package com.example.demo.scolarite.service;

import com.example.demo.directeur.pole.dto.PoleCandidatDto;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.repository.InscriptionRepository;
import com.example.demo.scolarite.dto.ScolariteCandidatDto;
import com.example.demo.scolarite.dto.ScolariteDossierDto;
import com.example.demo.scolarite.model.Scolarite;
import com.example.demo.scolarite.repository.ScolariteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScolariteService {

    private final ScolariteRepository scolariteRepository;
    private final InscriptionRepository inscriptionRepository;
    private final com.example.demo.candidat.repository.CandidatChoixRepository candidatChoixRepository;
    private final com.example.demo.candidat.repository.SujetRepository sujetRepository;
    private final com.example.demo.candidat.repository.CandidatRepository candidatRepository;

    /**
     * Get current scolarite profile.
     */
    public Scolarite getCurrentScolarite() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return scolariteRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Profil scolarité non trouvé pour " + email));
    }

    /**
     * Get all candidates who applied to at least one subject in the scolarite's laboratory
     */
    /**
     * Get all candidates who applied to at least one subject in the scolarite's laboratory (Paginated)
     */
    public org.springframework.data.domain.Page<com.example.demo.candidat.model.Candidat> getMyLabDossiers(String etatStr, org.springframework.data.domain.Pageable pageable) {
        Scolarite currentScolarite = getCurrentScolarite();
        Long laboId = currentScolarite.getLaboratoire().getId();
        
        com.example.demo.scolarite.model.EtatDossier etat = null;
        if (etatStr != null && !etatStr.equalsIgnoreCase("ALL")) {
            try {
                etat = com.example.demo.scolarite.model.EtatDossier.valueOf(etatStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignore invalid status, return all or empty? Returning all for now safe fallback
            }
        }
        
        return candidatRepository.findByLaboAndEtat(laboId, etat, pageable);
    }
    
    // Kept for stats calculation - inefficient for huge datasets but OK for counts if stats service not available
    public List<com.example.demo.candidat.model.Candidat> getMyLabDossiers(String etat) {
        // ... Original implementation reused for stats internal call ...
        Scolarite currentScolarite = getCurrentScolarite();
        Long laboId = currentScolarite.getLaboratoire().getId();
        List<com.example.demo.professeur.model.Sujet> subjects = sujetRepository.findByProfesseur_Laboratoire_Id(laboId);
        List<com.example.demo.candidat.model.CandidatChoix> choices = candidatChoixRepository.findBySujetIn(subjects);
        List<Long> candidateIds = choices.stream()
                .map(com.example.demo.candidat.model.CandidatChoix::getCandidatId)
                .distinct()
                .collect(Collectors.toList());
        return candidatRepository.findAllById(candidateIds).stream()
                .filter(c -> etat == null || "ALL".equalsIgnoreCase(etat) || c.getEtatDossier().toString().equalsIgnoreCase(etat))
                .collect(Collectors.toList());
    }

    /**
     * Get dashboard statistics for the scolarite's laboratory
     */
    public java.util.Map<String, Long> getMyLabStats() {
        List<com.example.demo.candidat.model.Candidat> candidats = getMyLabDossiers(null);
        
        return java.util.Map.of(
            "totalCandidats", (long) candidats.size(),
            "enAttente", candidats.stream().filter(c -> c.getEtatDossier() == com.example.demo.scolarite.model.EtatDossier.EN_ATTENTE).count(),
            "valides", candidats.stream().filter(c -> c.getEtatDossier() == com.example.demo.scolarite.model.EtatDossier.VALIDE).count(),
            "aCorrecter", candidats.stream().filter(c -> c.getEtatDossier() == com.example.demo.scolarite.model.EtatDossier.A_CORRIGER).count(),
            "rejetes", candidats.stream().filter(c -> c.getEtatDossier() == com.example.demo.scolarite.model.EtatDossier.REJETE).count()
        );
    }

    /**
     * Get specific dossier detail, ensuring it's in the scolarite's lab scope
     */
    public com.example.demo.candidat.model.Candidat getDossierDetail(Long id) {
        Scolarite currentScolarite = getCurrentScolarite();
        Long laboId = currentScolarite.getLaboratoire().getId();
        
        com.example.demo.candidat.model.Candidat candidat = candidatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));
        
        // Verification: candidate must have at least one choice in this lab
        List<com.example.demo.candidat.model.CandidatChoix> choices = candidatChoixRepository.findByCandidatId(id);
        boolean hasChoiceInLab = choices.stream()
                .anyMatch(choice -> choice.getSujet().getProfesseur().getLaboratoire() != null &&
                                  choice.getSujet().getProfesseur().getLaboratoire().getId().equals(laboId));
        
        if (!hasChoiceInLab) {
            throw new RuntimeException("Accès refusé : Ce candidat n'appartient pas à votre laboratoire.");
        }
        
        return candidat;
    }

    /**
     * Update dossier status
     */
    public com.example.demo.candidat.model.Candidat validerDossier(Long id, java.util.Map<String, String> validationData) {
        com.example.demo.candidat.model.Candidat candidat = getDossierDetail(id); // Already checks scope
        
        String action = validationData.get("etat"); // Match frontend "etat" key
        String commentaire = validationData.get("commentaire");
        
        if ("VALIDE".equalsIgnoreCase(action)) {
            candidat.setEtatDossier(com.example.demo.scolarite.model.EtatDossier.VALIDE);
        } else if ("REJETE".equalsIgnoreCase(action)) {
            candidat.setEtatDossier(com.example.demo.scolarite.model.EtatDossier.REJETE);
        } else if ("A_CORRIGER".equalsIgnoreCase(action)) {
            candidat.setEtatDossier(com.example.demo.scolarite.model.EtatDossier.A_CORRIGER);
        }
        
        candidat.setCommentaireScolarite(commentaire);
        return candidatRepository.save(candidat);
    }

    public org.springframework.data.domain.Page<ScolariteDossierDto> getAllDossiers(String search, String etat, org.springframework.data.domain.Pageable pageable) {
        // Handle "ALL" or empty strings as null for the query
        String searchParam = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        com.example.demo.scolarite.model.EtatDossier etatEnum = null;
        
        if (etat != null && !etat.trim().isEmpty() && !etat.equals("ALL")) {
            try {
                etatEnum = com.example.demo.scolarite.model.EtatDossier.valueOf(etat.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignore invalid status
            }
        }

        org.springframework.data.domain.Page<Inscription> inscriptions = inscriptionRepository.findByCriteria(etatEnum, searchParam, pageable);
        
        return inscriptions.map(this::mapToScolariteDossierDto);
    }

    public org.springframework.data.domain.Page<ScolariteCandidatDto> getMyLabCandidats(String searchCne, Long formationId, org.springframework.data.domain.Pageable pageable) {
        Scolarite currentScolarite = getCurrentScolarite();
        Long laboId = currentScolarite.getLaboratoire().getId();
        
        // If filters are simple, we can use repository methods directly
        // Currently InscriptionRepository doesn't support complex dynamic search AND pagination easily without Specs.
        // But for common case (just labo), we have findAcceptedByLaboId.
        
        // If we have search criteria, we might need a custom query or strict spec.
        // For now, let's assuming we just want pagination for the main list, and if search is present we might fallback or implement spec.
        
        // However, I added `findBySujet_FormationDoctorale_Id`.
        // Let's implement a clean way:
        // If searchCne is present, we filter in memory (inefficient but safe for now) OR add query.
        // Given time constraints, I will use findAcceptedByLaboId(pageable) and warn if search is used.
        // BETTER: Add a search method to repository.
        
        // Let's use the list method if search params are present (preserving old behavior), 
        // AND return a PageImpl wrapper around it (so frontend always gets Page structure).
        // This is a "fake" pagination for filtered results, but real pagination for default view.
        
        if (searchCne != null && !searchCne.isEmpty()) {
             // Fallback to list filtering
             List<ScolariteCandidatDto> list = getMyLabCandidats(searchCne, formationId); // Call the legacy list method
             int start = (int) pageable.getOffset();
             int end = Math.min((start + pageable.getPageSize()), list.size());
             
             List<ScolariteCandidatDto> subList = (start > list.size()) ? java.util.Collections.emptyList() : list.subList(start, end);
             return new org.springframework.data.domain.PageImpl<>(subList, pageable, list.size());
        }
        
        // If no text search, use efficient DB pagination
        org.springframework.data.domain.Page<Inscription> page;
        if (formationId != null) {
            // We need findBySujet_FormationDoctorale_IdAndLaboId... 
            // I haven't added that specific one to InscriptionRepository. 
            // I'll fallback to findAll for labo and filter? No, inefficient.
            // I'll just use the memory filter fallback for formationId too for now to be safe and quick 
            // vs adding more repo methods.
             List<ScolariteCandidatDto> list = getMyLabCandidats(searchCne, formationId);
             int start = (int) pageable.getOffset();
             int end = Math.min((start + pageable.getPageSize()), list.size());
             List<ScolariteCandidatDto> subList = (start > list.size()) ? java.util.Collections.emptyList() : list.subList(start, end);
             return new org.springframework.data.domain.PageImpl<>(subList, pageable, list.size());
        }
        
        // Default efficient case:
        return inscriptionRepository.findAcceptedByLaboId(laboId, pageable)
                .map(this::mapToDto);
    }

    /**
     * Legacy method kept, but updated to use for filter fallback
     */
    public List<ScolariteCandidatDto> getMyLabCandidats(String searchCne, Long formationId) {
        Scolarite currentScolarite = getCurrentScolarite();
        Long laboId = currentScolarite.getLaboratoire().getId();
        
        List<Inscription> inscriptions = inscriptionRepository.findByValiderTrue();

        return inscriptions.stream()
                .filter(i -> i.getSujet().getProfesseur().getLaboratoire() != null &&
                             i.getSujet().getProfesseur().getLaboratoire().getId().equals(laboId))
                .filter(i -> searchCne == null || searchCne.isEmpty() || 
                             (i.getCandidat().getCne() != null && i.getCandidat().getCne().toLowerCase().contains(searchCne.toLowerCase())))
                .filter(i -> formationId == null || 
                             (i.getSujet().getFormationDoctorale() != null && i.getSujet().getFormationDoctorale().getId().equals(formationId)))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ScolariteCandidatDto mapToDto(Inscription i) {
        return ScolariteCandidatDto.builder()
                .id(i.getCandidat().getId())
                .cne(i.getCandidat().getCne())
                .nom(i.getCandidat().getNomCandidatAr())
                .prenom(i.getCandidat().getPrenomCandidatAr())
                .email(i.getCandidat().getEmail())
                .telephone(i.getCandidat().getTelCandidat())
                .formation(i.getSujet().getFormationDoctorale().getTitre())
                .sujet(i.getSujet().getTitre())
                .directeur(i.getSujet().getProfesseur().getNomComplet())
                .etatDossier(i.getCandidat().getEtatDossier() != null ? i.getCandidat().getEtatDossier().toString() : "N/A")
                .inscrit(i.getValider())
                .dateInscription(i.getDateDiposeDossier() != null ? i.getDateDiposeDossier().toLocalDate() : null) 
                .build();
    }

    private ScolariteDossierDto mapToScolariteDossierDto(Inscription i) {
        return ScolariteDossierDto.builder()
                .id(i.getCandidat().getId())
                .cne(i.getCandidat().getCne())
                .nomCandidatAr(i.getCandidat().getNomCandidatAr())
                .prenomCandidatAr(i.getCandidat().getPrenomCandidatAr())
                .cin(i.getCandidat().getCin())
                .email(i.getCandidat().getEmail())
                .telCandidat(i.getCandidat().getTelCandidat())
                .ville(i.getCandidat().getVille())
                .etatDossier(i.getCandidat().getEtatDossier() != null ? i.getCandidat().getEtatDossier().toString() : "EN_ATTENTE")
                .pathPhoto(i.getCandidat().getPathPhoto()) // Ensure this field exists in DTO and Entity
                .build();
    }
}
