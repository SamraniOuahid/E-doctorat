package com.example.demo.professeur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;
//import com.example.demo.professeur.repository.CommissionProfesseurRepository;
import com.example.demo.professeur.repository.InscriptionRepository;
import com.example.demo.professeur.repository.SujetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfesseurServiceImpl implements ProfesseurService {

    private final SujetRepository sujetRepository;
    private final InscriptionRepository inscriptionRepository;
    //private final CommissionProfesseurRepository commissionProfesseurRepository;

    @Override
    public List<Sujet> getSujetsDuProfesseur(Long professeurId) {
        return sujetRepository.findByProfesseur_Id(professeurId);
    }

    @Override
    public List<Inscription> getCandidatsParSujet(Long sujetId) {
        return inscriptionRepository.findBySujet_Id(sujetId);
    }

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

    @Override
    public List<Inscription> getInscriptionsFinales(Long sujetId) {
        return inscriptionRepository.findBySujet_Id(sujetId)
                .stream()
                .filter(i -> Boolean.TRUE.equals(i.getValider()))
                .toList();
    }

    @Override
    public byte[] genererPvSujet(Long sujetId) {
        // 👉 ici tu ajouteras plus tard iText / OpenPDF
        // pour le moment on retourne vide
        return new byte[0];
    }
}
