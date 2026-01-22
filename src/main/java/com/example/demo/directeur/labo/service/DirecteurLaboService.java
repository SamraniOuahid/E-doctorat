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
    // --- Créer un sujet manuellement ---
    LaboSujetDto createSujet(Long laboId, LaboSujetDto sujetDto);

    // --- Modifier un sujet ---
    LaboSujetDto updateSujet(Long laboId, Long sujetId, LaboSujetDto sujetDto);

    // --- Supprimer un sujet ---
    void deleteSujet(Long laboId, Long sujetId);



    // --- Liste des professeurs du labo avec stats ---
    java.util.List<com.example.demo.directeur.labo.dto.ProfessorDto> getProfesseursDuLabo(Long laboId);

    List<com.example.demo.directeur.labo.dto.ProfessorDto> getAllProfesseurs();

    // --- Get laboratory info for authenticated director ---
    java.util.List<java.util.Map<String, Object>> getFormationsByLabo(Long laboId);
    
    java.util.Map<String, Object> getMyLaboInfo(String email);

    // ================== COMMISSIONS ==================
    List<com.example.demo.directeur.labo.dto.CommissionDto> getCommissionsByLabo(Long laboId);
    com.example.demo.directeur.labo.dto.CommissionDto createCommission(Long laboId, com.example.demo.directeur.labo.dto.CommissionDto dto);
    com.example.demo.directeur.labo.dto.CommissionDto updateCommission(Long laboId, Long id, com.example.demo.directeur.labo.dto.CommissionDto dto);
    void deleteCommission(Long laboId, Long id);
}
