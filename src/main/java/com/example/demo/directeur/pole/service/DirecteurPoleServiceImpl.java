package com.example.demo.directeur.pole.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.model.Diplome;
import com.example.demo.candidat.repository.CandidatRepository;
import com.example.demo.candidat.repository.SujetRepository;
import com.example.demo.directeur.pole.dto.*;
import com.example.demo.directeur.pole.model.Phase;
import com.example.demo.directeur.pole.model.PhaseStatut;
import com.example.demo.directeur.pole.repository.PhaseRepository;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.repository.CommissionRepository;
import com.example.demo.professeur.repository.InscriptionRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirecteurPoleServiceImpl implements DirecteurPoleService {

    private final CandidatRepository candidatRepository;
    private final SujetRepository sujetRepository;
    private final InscriptionRepository inscriptionRepository;
    private final CommissionRepository commissionRepository;
    private final PhaseRepository phaseRepository;
    private final CsvExportService csvExportService;
    private final com.example.demo.notification.NotificationService notificationService;

    @PostConstruct
    public void init() {
        initializeDefaultPhases();
    }

    @Override
    public PoleStatsDto getStats() {
        log.debug("Fetching pole dashboard stats");
        // Get count of confirmed phases instead of commissions
        long phasesConfirmees = phaseRepository.findByStatut(PhaseStatut.CONFIRME).size();
        
        return PoleStatsDto.builder()
                .totalCandidats(candidatRepository.count())
                .formationsDoctorales(8) // TODO: use FormationDoctoraleRepository when available
                .sujetsValides(sujetRepository.count())
                .commissionsPlanifiees(phasesConfirmees)
                .build();
    }

    @Override
    public org.springframework.data.domain.Page<PoleCandidatDto> getAllCandidats(Long formationId, org.springframework.data.domain.Pageable pageable) {
        log.debug("Fetching all candidats (paginated), formationId={}", formationId);
        
        org.springframework.data.domain.Page<Inscription> page;
        if (formationId != null) {
            page = inscriptionRepository.findBySujet_FormationDoctorale_Id(formationId, pageable);
        } else {
            page = inscriptionRepository.findAll(pageable);
        }
        
        return page.map(this::mapInscriptionToPoleCandidatDto);
    }

    // Keep non-paginated for internal use/exports
    public List<PoleCandidatDto> getAllCandidats(Long formationId) {
         // ... implementation for export ...
         // We can reuse the logic but for now let's keep the original one or adapt if needed.
         // Since the interface signature changed for the public one, we might need to rename or keep this private/protected if not in interface.
         // Actually, the interface removed this method signature (replaced by paginated).
         // But `exportListePrincipaleCSV` uses `getAllCandidats(null)`.
         // We should overload it or keep a private helper.
         return this.getAllCandidats(formationId, org.springframework.data.domain.Pageable.unpaged()).getContent();
    }

    @Override
    public org.springframework.data.domain.Page<PoleSujetDto> getAllSujets(Long formationId, Long laboId, org.springframework.data.domain.Pageable pageable) {
        log.debug("Fetching all sujets (paginated), formationId={}, laboId={}", formationId, laboId);
        org.springframework.data.domain.Page<Sujet> page;
        
        if (formationId != null && laboId != null) {
            page = sujetRepository.findByFormationDoctorale_IdAndProfesseur_Laboratoire_Id(formationId, laboId, pageable);
        } else if (formationId != null) {
            page = sujetRepository.findByFormationDoctorale_Id(formationId, pageable);
        } else if (laboId != null) {
            page = sujetRepository.findByProfesseur_Laboratoire_Id(laboId, pageable);
        } else {
            page = sujetRepository.findAll(pageable);
        }
        
        return page.map(this::mapToPoleSujetDto);
    }
    
    // Internal helper for export
    private List<PoleSujetDto> getAllSujetsList(Long formationId, Long laboId) {
         return this.getAllSujets(formationId, laboId, org.springframework.data.domain.Pageable.unpaged()).getContent();
    }

    @Override
    public org.springframework.data.domain.Page<PoleResultatDto> getResultats(org.springframework.data.domain.Pageable pageable) {
        log.debug("Fetching all resultats (paginated)");
        return inscriptionRepository.findAll(pageable)
                .map(this::mapToPoleResultatDto);
    }
    

    public List<PoleResultatDto> getResultats() {
        return inscriptionRepository.findAll().stream()
                .map(this::mapToPoleResultatDto)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<PoleResultatDto> getListePrincipale(org.springframework.data.domain.Pageable pageable) {
        log.debug("Fetching liste principale (paginated)");
        return inscriptionRepository.findByValiderTrue(pageable)
                .map(this::mapToPoleResultatDto);
    }

    @Override
    public List<PoleResultatDto> getListePrincipale() {
        return inscriptionRepository.findByValiderTrue().stream()
                .map(this::mapToPoleResultatDto)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<PoleResultatDto> getListeAttente(org.springframework.data.domain.Pageable pageable) {
        log.debug("Fetching liste attente (paginated)");
        return inscriptionRepository.findByValiderFalseOrNull(pageable)
                .map(this::mapToPoleResultatDto);
    }

    @Override
    public List<PoleResultatDto> getListeAttente() {
        return inscriptionRepository.findAll().stream()
                .filter(i -> !Boolean.TRUE.equals(i.getValider()))
                .map(this::mapToPoleResultatDto)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<PoleInscriptionDto> getInscriptions(org.springframework.data.domain.Pageable pageable) {
        log.debug("Fetching all inscriptions (paginated)");
        return inscriptionRepository.findByValiderTrue(pageable)
                .map(this::mapToPoleInscriptionDto);
    }
    

    public List<PoleInscriptionDto> getInscriptions() {
        return inscriptionRepository.findByValiderTrue().stream()
                .map(this::mapToPoleInscriptionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PoleFormationDto> getFormations() {
        log.debug("Fetching formations doctorales");
        // TODO: Use FormationDoctoraleRepository when available
        return List.of(
            PoleFormationDto.builder().id(1L).nom("Informatique et Sciences des Données").build(),
            PoleFormationDto.builder().id(2L).nom("Mathématiques Appliquées").build(),
            PoleFormationDto.builder().id(3L).nom("Physique et Ingénierie").build(),
            PoleFormationDto.builder().id(4L).nom("Chimie et Biotechnologies").build(),
            PoleFormationDto.builder().id(5L).nom("Sciences de la Terre").build(),
            PoleFormationDto.builder().id(6L).nom("Électronique et Télécommunications").build(),
            PoleFormationDto.builder().id(7L).nom("Génie Civil et Environnement").build(),
            PoleFormationDto.builder().id(8L).nom("Sciences Économiques et Gestion").build()
        );
    }

    @Override
    public List<PoleLaboratoireDto> getLaboratoires() {
        log.debug("Fetching laboratoires");
        // TODO: Use LaboratoireRepository when available
        return List.of(
            PoleLaboratoireDto.builder().id(1L).nom("LIIAN").formationId(1L).build(),
            PoleLaboratoireDto.builder().id(2L).nom("LSI").formationId(1L).build(),
            PoleLaboratoireDto.builder().id(3L).nom("LMAO").formationId(2L).build(),
            PoleLaboratoireDto.builder().id(4L).nom("LPMC").formationId(3L).build(),
            PoleLaboratoireDto.builder().id(5L).nom("LCBA").formationId(4L).build()
        );
    }

    @Override
    public byte[] telechargerRapportInscription() {
        log.debug("Generating rapport inscription PDF");
        String content = "Rapport des inscriptions - E-Doctorat USMBA\n\nListe des inscrits:\n";
        
        List<Inscription> inscrits = inscriptionRepository.findByValiderTrue();
        for (Inscription i : inscrits) {
            content += String.format("- %s: %s\n", 
                i.getCandidat() != null ? i.getCandidat().getEmail() : "N/A",
                i.getSujet() != null ? i.getSujet().getTitre() : "N/A");
        }
        
        return content.getBytes();
    }

    // ==================== CALENDAR MANAGEMENT ====================

    @Override
    public List<PhaseDto> getCalendrier() {
        log.debug("Fetching calendar phases");
        List<Phase> phases = phaseRepository.findAllByOrderByOrdreAsc();
        
        if (phases.isEmpty()) {
            initializeDefaultPhases();
            phases = phaseRepository.findAllByOrderByOrdreAsc();
        }
        
        return phases.stream()
                .map(this::mapToPhaseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PhaseDto getPhaseActive() {
        log.debug("Fetching active phase");
        return phaseRepository.findActivePhase(LocalDate.now())
                .map(this::mapToPhaseDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public PhaseDto updatePhase(Long phaseId, LocalDate dateDebut, LocalDate dateFin) {
        log.debug("Updating phase {}: {} to {}", phaseId, dateDebut, dateFin);
        
        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase not found: " + phaseId));
        
        phase.setDateDebut(dateDebut);
        phase.setDateFin(dateFin);
        phase.setStatut(PhaseStatut.CONFIRME);
        
        Phase saved = phaseRepository.save(phase);
        return mapToPhaseDto(saved);
    }

    @Override
    @Transactional
    public void initializeDefaultPhases() {
        if (phaseRepository.count() > 0) {
            log.debug("Phases already exist, skipping initialization");
            return;
        }
        
        log.info("Initializing default campaign phases");
        LocalDate baseDate = LocalDate.of(2026, 9, 1);
        
        List<Phase> defaultPhases = List.of(
            Phase.builder()
                .code("PROP_SUJETS")
                .nom("Proposition des Sujets")
                .dateDebut(baseDate)
                .dateFin(baseDate.plusDays(30))
                .ordre(0)
                .statut(PhaseStatut.PLANIFIE)
                .description("Les professeurs proposent leurs sujets de recherche")
                .build(),
            Phase.builder()
                .code("DEPOT_DOSSIERS")
                .nom("Dépôt des dossiers")
                .dateDebut(baseDate.plusMonths(1))
                .dateFin(baseDate.plusMonths(2))
                .ordre(1)
                .statut(PhaseStatut.PLANIFIE)
                .description("Les candidats soumettent leurs dossiers de candidature")
                .build(),
            Phase.builder()
                .code("PRESELECTION")
                .nom("Présélection des dossiers")
                .dateDebut(baseDate.plusMonths(2).plusDays(1))
                .dateFin(baseDate.plusMonths(2).plusDays(15))
                .ordre(2)
                .statut(PhaseStatut.PLANIFIE)
                .description("La commission examine les dossiers")
                .build(),
            Phase.builder()
                .code("ENTRETIENS")
                .nom("Entretiens Oraux")
                .dateDebut(baseDate.plusMonths(2).plusDays(20))
                .dateFin(baseDate.plusMonths(3).plusDays(5))
                .ordre(3)
                .statut(PhaseStatut.PLANIFIE)
                .description("Entretiens avec les candidats présélectionnés")
                .build(),
            Phase.builder()
                .code("RESULTATS")
                .nom("Affichage des Résultats Définitifs")
                .dateDebut(baseDate.plusMonths(3).plusDays(10))
                .dateFin(baseDate.plusMonths(3).plusDays(15))
                .ordre(4)
                .statut(PhaseStatut.PLANIFIE)
                .description("Publication des listes principale et d'attente")
                .build()
        );
        
        phaseRepository.saveAll(defaultPhases);
        log.info("Created {} default phases", defaultPhases.size());
    }

    // ==================== MAPPERS ====================

    private PhaseDto mapToPhaseDto(Phase p) {
        return PhaseDto.builder()
                .id(p.getId())
                .code(p.getCode())
                .nom(p.getNom())
                .phase("Phase " + p.getOrdre())
                .dateDebut(p.getDateDebut())
                .dateFin(p.getDateFin())
                .statut(p.getStatut().name().toLowerCase())
                .ordre(p.getOrdre())
                .description(p.getDescription())
                .active(p.isActive())
                .past(p.isPast())
                .future(p.isFuture())
                .build();
    }

    private PoleCandidatDto mapInscriptionToPoleCandidatDto(Inscription i) {
        Candidat c = i.getCandidat();
        Sujet s = i.getSujet();
        
        PoleCandidatDto dto = new PoleCandidatDto();
        dto.setId(c.getId());
        dto.setCne(c.getCne());
        dto.setNom(c.getNomCandidatAr());
        dto.setPrenom(c.getPrenomCandidatAr());
        dto.setEmail(c.getEmail());
        dto.setTelephone(c.getTelCandidat());
        dto.setDateNaissance(c.getDateDeNaissance() != null ? c.getDateDeNaissance().toString() : null);
        dto.setLieuNaissance(c.getVilleDeNaissance());
        
        // Get the latest diploma info (using correct field names from Diplome entity)
        if (c.getDiplomes() != null && !c.getDiplomes().isEmpty()) {
            Diplome lastDiplome = c.getDiplomes().get(c.getDiplomes().size() - 1);
            dto.setDiplomePrecedent(lastDiplome.getType());
            dto.setEtablissement(lastDiplome.getEtablissement());
            dto.setMoyenneGenerale(lastDiplome.getMoyenGenerale());
        }
        
        // Sujet info
        if (s != null) {
            dto.setSujetTitre(s.getTitre());
            if (s.getProfesseur() != null && s.getProfesseur().getLaboratoire() != null) {
                dto.setLaboratoire(s.getProfesseur().getLaboratoire().getNomLaboratoire());
            }
            if (s.getFormationDoctorale() != null) {
                dto.setFormation(s.getFormationDoctorale().getTitre());
                dto.setFormationId(s.getFormationDoctorale().getId());
            }
        }
        
        return dto;
    }

    private PoleSujetDto mapToPoleSujetDto(Sujet s) {
        PoleSujetDto dto = new PoleSujetDto();
        dto.setId(s.getId());
        dto.setTitre(s.getTitre());
        dto.setPublier(s.isPublier());
        
        // Professeur name - using grade as workaround since name is in UserAccount
        if (s.getProfesseur() != null) {
            String profInfo = "Prof. " + (s.getProfesseur().getGrade() != null ? 
                s.getProfesseur().getGrade() : "ID-" + s.getProfesseur().getId());
            dto.setProfesseur(profInfo);
            
            // Laboratoire (using correct field: nomLaboratoire)
            if (s.getProfesseur().getLaboratoire() != null) {
                dto.setLaboratoire(s.getProfesseur().getLaboratoire().getNomLaboratoire());
                dto.setLaboId(s.getProfesseur().getLaboratoire().getId());
            }
        }
        
        // CoDirecteur
        if (s.getCoDirecteur() != null) {
            dto.setCoDirecteur("Prof. " + (s.getCoDirecteur().getGrade() != null ? 
                s.getCoDirecteur().getGrade() : "ID-" + s.getCoDirecteur().getId()));
        }
        
        // Formation
        if (s.getFormationDoctorale() != null) {
            dto.setFormation(s.getFormationDoctorale().getTitre());
            dto.setFormationId(s.getFormationDoctorale().getId());
        }
        
        return dto;
    }

    private PoleResultatDto mapToPoleResultatDto(Inscription i) {
        PoleResultatDto dto = new PoleResultatDto();
        dto.setCandidatId(i.getCandidat() != null ? i.getCandidat().getId() : null);
        dto.setDecision(Boolean.TRUE.equals(i.getValider()) ? "ADMIS" : "EN_ATTENTE");
        return dto;
    }

    private PoleInscriptionDto mapToPoleInscriptionDto(Inscription i) {
    var candidat = i.getCandidat();
    var sujet = i.getSujet();
    var professeur = sujet != null ? sujet.getProfesseur() : null;
    var labo = professeur != null ? professeur.getLaboratoire() : null;
    
    return PoleInscriptionDto.builder()
            .id(i.getId())
            .cne(candidat != null ? candidat.getCne() : "N/A")
            .nom(candidat != null ? candidat.getNomCandidatAr() : "")
            .prenom(candidat != null ? candidat.getPrenomCandidatAr() : "")
            .sujet(sujet != null ? sujet.getTitre() : "N/A")
            .directeur(professeur != null ? professeur.getNomComplet() : "N/A")
            .laboratoire(labo != null ? labo.getNomLaboratoire() : "N/A")
            .dateInscription(i.getDateDiposeDossier() != null ? i.getDateDiposeDossier().toLocalDate() : null)
            .statut(i.getValider() != null && i.getValider() ? "Inscrit" : "En attente")
            .build();
    }

    // ==================== PUBLICATION MANAGEMENT ====================
    // (Note: These methods require PublicationResultatRepository and NotificationService 
    // to be injected. For now, we provide a simple implementation)

    @Override
    public java.util.Map<String, PublicationStatusDto> getPublicationStatus() {
        log.debug("Fetching publication status");
        java.util.Map<String, PublicationStatusDto> status = new java.util.HashMap<>();
        
        // Return default status (not published) - will be replaced with DB status when repository is injected
        status.put("principale", PublicationStatusDto.builder()
                .type("principale")
                .publiee(false)
                .build());
        status.put("attente", PublicationStatusDto.builder()
                .type("attente")
                .publiee(false)
                .build());
        
        return status;
    }

    @Override
    @Transactional
    public PublicationStatusDto publishList(String type, String publishedBy) {
        log.info("Publishing list {} by {}", type, publishedBy);
        
        // Get affected candidates
        List<PoleResultatDto> resultats;
        if ("principale".equalsIgnoreCase(type)) {
            resultats = getListePrincipale();
        } else if ("attente".equalsIgnoreCase(type)) {
            resultats = getListeAttente();
        } else {
            throw new IllegalArgumentException("Type de liste invalide: " + type);
        }
        
        // Extract candidates from results
        List<Candidat> affectedCandidats = resultats.stream()
                .map(r -> r.getCandidatId())
                .filter(id -> id != null)
                .distinct()
                .map(id -> candidatRepository.findById(id).orElse(null))
                .filter(c -> c != null)
                .collect(Collectors.toList());
        
        // Trigger WebSocket notifications for all affected candidates
        if (!affectedCandidats.isEmpty()) {
            notificationService.notifyResultPublication(type, affectedCandidats);
            log.info("Sent real-time notifications to {} candidates for {} list", 
                    affectedCandidats.size(), type);
        }
        
        return PublicationStatusDto.builder()
                .type(type)
                .publiee(true)
                .datePublication(java.time.LocalDateTime.now())
                .publishedBy(publishedBy)
                .build();
    }

    // ==================== CSV EXPORT ====================

    @Override
    public byte[] exportListePrincipaleCSV() {
        log.debug("Exporting Liste Principale to CSV");
        List<PoleResultatDto> resultats = getListePrincipale();
        List<PoleCandidatDto> candidats = getAllCandidats(null).stream()
                .filter(c -> resultats.stream()
                        .anyMatch(r -> r.getCandidatId() != null && r.getCandidatId().equals(c.getId())))
                .collect(Collectors.toList());
        
        return csvExportService.exportListePrincipale(resultats, candidats);
    }

    @Override
    public byte[] exportListeAttenteCSV() {
        log.debug("Exporting Liste d'Attente to CSV");
        List<PoleResultatDto> resultats = getListeAttente();
        List<PoleCandidatDto> candidats = getAllCandidats(null).stream()
                .filter(c -> resultats.stream()
                        .anyMatch(r -> r.getCandidatId() != null && r.getCandidatId().equals(c.getId())))
                .collect(Collectors.toList());
        
        return csvExportService.exportListeAttente(resultats, candidats);
    }

    @Override
    public byte[] exportInscriptionsCSV() {
        log.debug("Exporting all inscriptions to CSV");
        List<PoleCandidatDto> candidats = getAllCandidats(null);
        return csvExportService.exportInscriptions(candidats);
    }
}


