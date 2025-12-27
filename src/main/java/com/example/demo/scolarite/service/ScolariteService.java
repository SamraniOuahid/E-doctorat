package com.example.demo.scolarite.service;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.repository.CandidatRepository;
import com.example.demo.scolarite.dto.ValidationDossierDto;
import com.example.demo.scolarite.model.EtatDossier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScolariteService {

    private final CandidatRepository candidatRepository;

    // 1. Recevoir les dossiers (optionnellement filtrés par état)
    public List<Candidat> getAllDossiers(EtatDossier etatFilter) {
        if (etatFilter != null) {
            return candidatRepository.findByEtatDossier(etatFilter);
        }
        return candidatRepository.findAll();
    }

    // 2. Récupérer un dossier spécifique
    public Candidat getDossier(Long candidatId) {
        return candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable"));
    }

    // 3. Valider ou Commenter le dossier
    @Transactional
    public Candidat validerDossier(Long candidatId, ValidationDossierDto dto) {
        Candidat candidat = getDossier(candidatId);

        // Mise à jour du statut
        if (dto.getEtat() != null) {
            candidat.setEtatDossier(dto.getEtat());
        }

        // Mise à jour du commentaire (si fourni)
        if (dto.getCommentaire() != null) {
            candidat.setCommentaireScolarite(dto.getCommentaire());
        }

        // Ici, on pourrait ajouter une notification par email au candidat
        // notificationService.sendStatusUpdate(candidat);

        return candidatRepository.save(candidat);
    }
}
