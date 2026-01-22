package com.example.demo.directeur.ced.service;

import com.example.demo.candidat.model.CandidatChoix;
import com.example.demo.candidat.repository.CandidatChoixRepository;
import com.example.demo.candidat.repository.SujetRepository;
import com.example.demo.candidat.specification.SujetSpecification;
import com.example.demo.professeur.model.*;
import com.example.demo.professeur.repository.*;

import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import com.example.demo.directeur.ced.repository.CedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

@Service
@Transactional
public class DirecteurCedService {

    @Autowired
    private SujetRepository sujetRepository;

    @Autowired
    private CandidatChoixRepository candidatChoixRepository;

    @Autowired
    private ExaminerRepository examinerRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CedRepository cedRepository;

    @Autowired
    private CommissionRepository commissionRepository;

    // =========================================================================
    // 1. GESTION DES SUJETS (Avec Specification)
    // =========================================================================

    // =========================================================================
    // 1. GESTION DES SUJETS (Avec Specification)
    // =========================================================================

    public org.springframework.data.domain.Page<Sujet> getSujetsByFormation(Long cedId, Long formationId, org.springframework.data.domain.Pageable pageable) {
        Specification<Sujet> specCritere = SujetSpecification.getSujetsByFilter(null, null, formationId, null, true);
        Specification<Sujet> specSecurite = (root, query, cb) -> cb
                .equal(root.get("formationDoctorale").get("ced").get("id"), cedId);
        return sujetRepository.findAll(specCritere.and(specSecurite), pageable);
    }

    public List<Sujet> getSujetsByFormation(Long cedId, Long formationId) {
        // ... kept for internal use ...
        Specification<Sujet> specCritere = SujetSpecification.getSujetsByFilter(null, null, formationId, null, true);
        Specification<Sujet> specSecurite = (root, query, cb) -> cb
                .equal(root.get("formationDoctorale").get("ced").get("id"), cedId);
        return sujetRepository.findAll(specCritere.and(specSecurite));
    }

    // =========================================================================
    // 2. GESTION DES CANDIDATURES
    // =========================================================================

    public org.springframework.data.domain.Page<CandidatChoix> getCandidatsByFormation(Long cedId, Long formationId, org.springframework.data.domain.Pageable pageable) {
        // Etape 1 : On récupère les sujets valides de cette formation (Without pagination for the filter list)
        List<Sujet> sujetsDeLaFormation = getSujetsByFormation(cedId, formationId);

        if (sujetsDeLaFormation.isEmpty()) {
            return org.springframework.data.domain.Page.empty(pageable);
        }

        // Etape 2 : On cherche tous les choix qui pointent vers ces sujets (Paginated)
        return candidatChoixRepository.findBySujetIn(sujetsDeLaFormation, pageable);
    }
    
    public List<CandidatChoix> getCandidatsByFormation(Long cedId, Long formationId) {
        List<Sujet> sujetsDeLaFormation = getSujetsByFormation(cedId, formationId);
        if (sujetsDeLaFormation.isEmpty()) {
            return List.of();
        }
        return candidatChoixRepository.findBySujetIn(sujetsDeLaFormation);
    }

    // =========================================================================
    // 3. GESTION DES RÉSULTATS (Examiner)
    // =========================================================================

    public org.springframework.data.domain.Page<Examiner> getResultatsByCed(Long cedId, org.springframework.data.domain.Pageable pageable) {
        return examinerRepository.findBySujet_FormationDoctorale_Ced_Id(cedId, pageable);
    }

    public List<Examiner> getResultatsByCed(Long cedId) {
        return examinerRepository.findBySujet_FormationDoctorale_Ced_Id(cedId);
    }

    // =========================================================================
    // 4. GESTION DES INSCRIPTIONS + CSV
    // =========================================================================

    public org.springframework.data.domain.Page<Inscription> getInscritsByCed(Long cedId, org.springframework.data.domain.Pageable pageable) {
        return inscriptionRepository.findBySujet_FormationDoctorale_Ced_Id(cedId, pageable);
    }

    public List<Inscription> getInscritsByCed(Long cedId) {
        return inscriptionRepository.findBySujet_FormationDoctorale_Ced_Id(cedId);
    }

    public ByteArrayInputStream generateRapportInscriptionCsv(Long cedId) {
        List<Inscription> inscrits = getInscritsByCed(cedId);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                PrintWriter writer = new PrintWriter(out)) {

            // En-tête CSV
            writer.println("ID Inscription,Candidat ID,Sujet,Laboratoire,Etablissement,Date");

            for (Inscription insc : inscrits) {
                // Gestion sécurisée pour éviter NullPointerException si des données manquent
                String titreSujet = (insc.getSujet() != null) ? insc.getSujet().getTitre() : "Inconnu";

                String nomLabo = "N/A";
                if (insc.getSujet() != null && insc.getSujet().getProfesseur() != null
                        && insc.getSujet().getProfesseur().getLaboratoire() != null) {
                    nomLabo = insc.getSujet().getProfesseur().getLaboratoire().getNomLaboratoire();
                }

                String nomEtab = "N/A";
                if (insc.getSujet() != null && insc.getSujet().getProfesseur() != null
                        && insc.getSujet().getProfesseur().getEtablissement() != null) {
                    nomEtab = insc.getSujet().getProfesseur().getEtablissement().getNomEtablissement();
                }

                // Écriture de la ligne (Attention aux virgules dans les titres)
                writer.printf("%d,%d,\"%s\",\"%s\",\"%s\",%s\n",
                        insc.getId(),
                        insc.getCandidat().getId(),
                        titreSujet.replace("\"", "'"), // Nettoyage titre
                        nomLabo,
                        nomEtab,
                        insc.getDateDiposeDossier());
            }

            writer.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erreur CSV: " + e.getMessage());
        }
    }

    // =========================================================================
    // 5. GESTION DES COMMISSIONS
    // =========================================================================

    public org.springframework.data.domain.Page<Commission> getCommissionsByCed(Long cedId, org.springframework.data.domain.Pageable pageable) {
        return commissionRepository.findByLaboratoire_Ced_Id(cedId, pageable);
    }

    public List<Commission> getCommissionsByCed(Long cedId) {
        return commissionRepository.findByLaboratoire_Ced_Id(cedId);
    }

    // =========================================================================
    // 6. RÉSOLUTION CED ID PAR EMAIL
    // =========================================================================

    public Long getCedIdByEmail(String email) {
        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + email));

        Ced ced = cedRepository.findByDirecteur_UserId(user.getId())
                .orElseThrow(() -> new RuntimeException("CED non trouvé pour le directeur: " + email));

        return ced.getId();
    }
}