package com.example.demo.scolarite.service;

import com.example.demo.directeur.pole.dto.PoleCandidatDto;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.repository.InscriptionRepository;
import com.example.demo.scolarite.dto.ScolariteCandidatDto;
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
    public List<com.example.demo.candidat.model.Candidat> getMyLabDossiers(String etat) {
        Scolarite currentScolarite = getCurrentScolarite();
        Long laboId = currentScolarite.getLaboratoire().getId();
        
        // 1. Find all subjects of this lab
        List<com.example.demo.professeur.model.Sujet> subjects = sujetRepository.findByProfesseur_Laboratoire_Id(laboId);
        
        // 2. Find all choices for these subjects
        List<com.example.demo.candidat.model.CandidatChoix> choices = candidatChoixRepository.findBySujetIn(subjects);
        
        // 3. Get unique candidate IDs
        List<Long> candidateIds = choices.stream()
                .map(com.example.demo.candidat.model.CandidatChoix::getCandidatId)
                .distinct()
                .collect(Collectors.toList());
        
        // 4. Fetch candidates and filter by status if provided
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

    /**
     * Legacy method kept for compatibility if needed, but updated to use new logic
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
}
