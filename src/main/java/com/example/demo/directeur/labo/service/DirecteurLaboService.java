package com.example.demo.directeur.labo.service;

import com.example.demo.directeur.labo.dto.LaboCandidatDto;
import com.example.demo.directeur.labo.dto.LaboResultatDto;
import com.example.demo.directeur.labo.dto.LaboSujetDto;

import java.util.List;

public interface DirecteurLaboService {

    // --- Consulter les sujets du labo ---
    List<LaboSujetDto> getSujetsDuLabo(Long laboId);

    // --- Consulter les candidats du labo (optionnel : filtrer par sujet) ---
    List<LaboCandidatDto> getCandidatsDuLabo(Long laboId, Long sujetId);

    // --- Voir les résultats (acceptés + refusés + en attente) ---
    List<LaboResultatDto> getResultatsDuLabo(Long laboId);

    // --- Voir uniquement les inscrits (acceptés) ---
    List<LaboResultatDto> getInscritsDuLabo(Long laboId);

    // --- Télécharger le PV global (PDF) ---
    byte[] genererPvGlobal(Long laboId);

    // --- Importer des sujets via CSV ---
    void importSujetsCsv(Long laboId, java.io.InputStream inputStream);

    // --- Créer un sujet manuellement ---
    LaboSujetDto createSujet(Long laboId, LaboSujetDto sujetDto);
}
