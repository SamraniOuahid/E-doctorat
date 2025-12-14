package com.example.demo.professeur.service;

import com.example.demo.professeur.dto.CandidatForProfDto;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.repository.InscriptionRepositoryv1;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesseurServicev1 {

    private final InscriptionRepositoryv1 inscriptionRepositoryv1;

    public ProfesseurServicev1(InscriptionRepositoryv1 inscriptionRepositoryv1) {
        this.inscriptionRepositoryv1 = inscriptionRepositoryv1;
    }

    // 1) all candidates who applied on ANY subject of this professor
    public List<CandidatForProfDto> getCandidatsByProf(Long profId) {
        List<Inscription> inscriptions =
                inscriptionRepositoryv1.findByProfesseurId(profId);

        return inscriptions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 2) candidates for ONE specific subject
    public List<CandidatForProfDto> getCandidatsBySujet(Long profId, Long sujetId) {
        // Optionally: verify that the subject belongs to this professor
        List<Inscription> inscriptions =
                inscriptionRepositoryv1.findBySujet_Id(sujetId);

        return inscriptions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CandidatForProfDto toDto(Inscription insc) {
        var dto = new CandidatForProfDto();
        dto.setInscriptionId(insc.getId());
        dto.setCandidatId(insc.getCandidat().getId());

        // adapt getter names to your CandidatModel
        dto.setCandidatNomComplet(insc.getCandidat().getNomComplet());
        dto.setEmail(insc.getCandidat().getEmail());

        dto.setSujetId(insc.getSujet().getId());
        dto.setSujetTitre(insc.getSujet().getTitre());

        dto.setValider(insc.getValider());
        dto.setRemarque(insc.getRemarque());
        return dto;
    }
}
