package com.example.demo.professeur.service;

import com.example.demo.professeur.dto.CandidatForProfDto;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.repository.InscriptionRepository;
// Le nouvel import (là où se trouve votre Repository avec Specification)
import com.example.demo.candidat.repository.SujetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfesseurServiceImpl implements ProfesseurService {

    private final SujetRepository sujetRepository;
    private final InscriptionRepository inscriptionRepository;

    // ================== 1) Mes sujets ==================

    @Override
    public List<Sujet> getSujetsDuProfesseur(Long professeurId) {
        // tous les sujets dont professeur_id = professeurId
        return sujetRepository.findByProfesseur_Id(professeurId);
    }

    // ================== 2) Mes candidats (DTO) =========

    // Tous les candidats qui ont postulé sur AU MOINS un sujet de ce prof
    @Override
    public List<CandidatForProfDto> getCandidatsByProf(Long profId) {
        List<Inscription> inscriptions =
                inscriptionRepository.findByProfesseurId(profId);

        return inscriptions.stream()
                .map(this::toDto)
                .toList();
    }

    // Candidats pour UN sujet précis de ce prof
    @Override
    public List<CandidatForProfDto> getCandidatsBySujet(Long profId, Long sujetId) {
        List<Inscription> inscriptions =
                inscriptionRepository.findBySujet_Id(sujetId);

        // sécurité : garder seulement les inscriptions du sujet appartenant à ce prof
        inscriptions = inscriptions.stream()
                .filter(i -> i.getSujet().getProfesseur().getId().equals(profId))
                .toList();

        return inscriptions.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<Inscription> getInscriptionsBySujet(Long sujetId) {
        return inscriptionRepository.findBySujet_Id(sujetId);
    }

    // ================== 3) Examiner : accepter / refuser ==============

    @Override
    public void accepterCandidature(Long inscriptionId) {
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new RuntimeException("Inscription introuvable"));

        inscription.setValider(true);
        inscriptionRepository.save(inscription);
    }

    @Override
    public void refuserCandidature(Long inscriptionId) {
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new RuntimeException("Inscription introuvable"));

        inscription.setValider(false);
        inscriptionRepository.save(inscription);
    }

    // ================== 4) Inscriptions finales + PV ==================

    @Override
    public List<Inscription> getInscriptionsFinales(Long sujetId) {
        return inscriptionRepository.findBySujet_Id(sujetId)
                .stream()
                .filter(i -> Boolean.TRUE.equals(i.getValider()))
                .toList();
    }

    @Override
    public byte[] genererPvSujet(Long sujetId) {
        // TODO: générer le PDF plus tard (iText / OpenPDF, etc.)
        return new byte[0];
    }

    // ================== Mapper entity -> DTO ==================

    private CandidatForProfDto toDto(Inscription insc) {
        CandidatForProfDto dto = new CandidatForProfDto();

        dto.setInscriptionId(insc.getId());
        dto.setCandidatId(insc.getCandidat().getId());

        // Nom complet calculé à partir de nomCandidatAr + prenomCandidatAr
        dto.setCandidatNomComplet(insc.getCandidat().getNomComplet());

        // Pas d’email dans le schéma pour l’instant → on laisse vide
        // Quand vous aurez la table User avec email, vous remplirez ça ici.
        dto.setEmail(null);

        dto.setSujetId(insc.getSujet().getId());
        dto.setSujetTitre(insc.getSujet().getTitre());

        dto.setValider(insc.getValider());
        dto.setRemarque(insc.getRemarque());

        return dto;
    }
}
