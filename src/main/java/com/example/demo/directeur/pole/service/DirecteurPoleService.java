package com.example.demo.directeur.pole.service;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.directeur.pole.dto.*;

public interface DirecteurPoleService {

    // Dashboard stats
    PoleStatsDto getStats();

    // Candidats
    org.springframework.data.domain.Page<PoleCandidatDto> getAllCandidats(Long formationId, org.springframework.data.domain.Pageable pageable);

    // Sujets
    org.springframework.data.domain.Page<PoleSujetDto> getAllSujets(Long formationId, Long laboId, org.springframework.data.domain.Pageable pageable);

    // Resultats
    org.springframework.data.domain.Page<PoleResultatDto> getResultats(org.springframework.data.domain.Pageable pageable);
    
    // Listes
    org.springframework.data.domain.Page<PoleResultatDto> getListePrincipale(org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<PoleResultatDto> getListeAttente(org.springframework.data.domain.Pageable pageable);
    
    // Inscriptions
    org.springframework.data.domain.Page<PoleInscriptionDto> getInscriptions(org.springframework.data.domain.Pageable pageable);
    
    // Non-paginated versions for export/internal use if needed
    List<PoleResultatDto> getListePrincipale();
    List<PoleResultatDto> getListeAttente();

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

