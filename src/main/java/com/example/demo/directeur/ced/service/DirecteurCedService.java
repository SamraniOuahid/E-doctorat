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

    public List<Sujet> getSujetsByFormation(Long cedId, Long formationId) {
        // 1. Créer la spécification de base (Filtres utilisateur)
        // On met 'null' car ici le directeur ne filtre pas encore par mot-clé, juste
        // par formation
        Specification<Sujet> specCritere = SujetSpecification.getSujetsByFilter(null, null, formationId, null, true);

        // 2. Créer la spécification de SÉCURITÉ (Doit appartenir au CED du directeur)
        Specification<Sujet> specSecurite = (root, query, cb) -> cb
                .equal(root.get("formationDoctorale").get("ced").get("id"), cedId);

        // 3. Combiner les deux (WHERE formation = X AND ced = Y)
        return sujetRepository.findAll(specCritere.and(specSecurite));
    }

    // =========================================================================
    // 2. GESTION DES CANDIDATURES
    // =========================================================================

    public List<CandidatChoix> getCandidatsByFormation(Long cedId, Long formationId) {
        // Etape 1 : On récupère les sujets valides de cette formation
        List<Sujet> sujetsDeLaFormation = getSujetsByFormation(cedId, formationId);

        if (sujetsDeLaFormation.isEmpty()) {
            return List.of();
        }

        // Etape 2 : On cherche tous les choix qui pointent vers ces sujets
        // Utilise: findBySujetIn(List<Sujet> sujets) dans CandidatChoixRepository
        return candidatChoixRepository.findBySujetIn(sujetsDeLaFormation);
    }

    // =========================================================================
    // 3. GESTION DES RÉSULTATS (Examiner)
    // =========================================================================

    public List<Examiner> getResultatsByCed(Long cedId) {
        // Utilise la méthode magique dans ExaminerRepository
        return examinerRepository.findBySujet_FormationDoctorale_Ced_Id(cedId);
    }

    // =========================================================================
    // 4. GESTION DES INSCRIPTIONS + CSV
    // =========================================================================

    public List<Inscription> getInscritsByCed(Long cedId) {
        // Utilise la méthode magique dans InscriptionRepository
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