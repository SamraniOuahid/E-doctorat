package com.example.demo.professeur.service;

import com.example.demo.professeur.dto.SujetDto;
import com.example.demo.professeur.model.ProfesseurModel;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.repository.ProfesseurRepository;
import com.example.demo.professeur.repository.SujetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SujetService {

    private final SujetRepository sujetRepository;
    private final ProfesseurRepository professeurRepository;

    // 1) list my subjects
    public List<Sujet> getSujetsByProf(Long profId) {
        return sujetRepository.findByProfesseur_Id(profId);
    }

    // 2) create subject (rule: PA cannot propose)
    public Sujet createSujet(Long profId, SujetDto dto) {
        ProfesseurModel prof = professeurRepository.findById(profId)
                .orElseThrow(() -> new EntityNotFoundException("Professeur not found"));

        if (!canProposerSujet(prof)) {
            throw new IllegalStateException(
                    "Les professeurs avec le grade PA ne peuvent pas proposer de sujet."
            );
        }

        Sujet sujet = new Sujet();
        sujet.setTitre(dto.getTitre());
        sujet.setDescription(dto.getDescription());
        sujet.setPublier(dto.isPublier());
        sujet.setProfesseur(prof);

        // TODO later: set formationDoctorale, coDirecteur, etc.

        return sujetRepository.save(sujet);
    }

    // 3) update subject (only if it belongs to this prof)
    public Sujet updateSujet(Long profId, Long sujetId, SujetDto dto) {
        Sujet sujet = sujetRepository.findById(sujetId)
                .orElseThrow(() -> new EntityNotFoundException("Sujet not found"));

        if (!sujet.getProfesseur().getId().equals(profId)) {
            throw new IllegalStateException("Sujet does not belong to this professor");
        }

        sujet.setTitre(dto.getTitre());
        sujet.setDescription(dto.getDescription());
        sujet.setPublier(dto.isPublier());

        return sujetRepository.save(sujet);
    }

    // 4) delete subject (only if it belongs to this prof)
    public void deleteSujet(Long profId, Long sujetId) {
        Sujet sujet = sujetRepository.findById(sujetId)
                .orElseThrow(() -> new EntityNotFoundException("Sujet not found"));

        if (!sujet.getProfesseur().getId().equals(profId)) {
            throw new IllegalStateException("Sujet does not belong to this professor");
        }

        sujetRepository.delete(sujet);
    }

    // ======== Business rule: PA cannot propose a subject ========
    private boolean canProposerSujet(ProfesseurModel prof) {
        // If grade == "PA" → not allowed, otherwise allowed
        return !"PA".equalsIgnoreCase(prof.getGrade());
    }
}
