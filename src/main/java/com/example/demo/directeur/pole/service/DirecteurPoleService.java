package com.example.demo.directeur.pole.service;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.directeur.pole.dto.*;

public interface DirecteurPoleService {

    // Dashboard stats
    PoleStatsDto getStats();

    // Candidats with optional filter
    List<PoleCandidatDto> getAllCandidats(Long formationId);

    // Sujets with optional filters
    List<PoleSujetDto> getAllSujets(Long formationId, Long laboId);

    // Resultats
    List<PoleResultatDto> getResultats();
    List<PoleResultatDto> getListePrincipale();
    List<PoleResultatDto> getListeAttente();

    // Inscriptions
    List<PoleInscriptionDto> getInscriptions();

    // Formations doctorales
    List<PoleFormationDto> getFormations();

    // Laboratoires
    List<PoleLaboratoireDto> getLaboratoires();

    // Rapport PDF
    byte[] telechargerRapportInscription();

    // ========== Calendar Management ==========
    
    // Get all phases of the campaign
    List<PhaseDto> getCalendrier();
    
    // Get the currently active phase
    PhaseDto getPhaseActive();
    
    // Update a phase's dates
    PhaseDto updatePhase(Long phaseId, LocalDate dateDebut, LocalDate dateFin);
    
    // Initialize default phases if none exist
    void initializeDefaultPhases();

    // ========== Publication Management ==========
    
    // Get publication status for both LP and LA
    java.util.Map<String, PublicationStatusDto> getPublicationStatus();
    
    // Publish a list (IRREVERSIBLE!)
    PublicationStatusDto publishList(String type, String publishedBy);

    // ========== CSV Export ==========
    
    // Export LP to CSV
    byte[] exportListePrincipaleCSV();
    
    // Export LA to CSV
    byte[] exportListeAttenteCSV();
    
    // Export inscriptions to CSV
    byte[] exportInscriptionsCSV();
}

