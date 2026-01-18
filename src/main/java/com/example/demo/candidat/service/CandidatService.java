package com.example.demo.candidat.service;

import com.example.demo.candidat.dto.CandidatUpdateDTO;
import com.example.demo.candidat.model.*;
import com.example.demo.candidat.repository.*;
import com.example.demo.candidat.specification.SujetSpecification;
import com.example.demo.professeur.model.Sujet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidatService {

    private final PasswordEncoder passwordEncoder;
    private final SujetRepository sujetRepository;
    private final CandidatChoixRepository choixRepository;
    private final NotificationRepository notificationRepository;
    private final DiplomeRepository diplomeRepository;
    private final CandidatRepository candidatRepository;


    @Value("${edoctorat.candidat.max-choix:3}")
    private int maxChoix;

    // ==========================================================
    // MODULE 1: FILTERS
    // ==========================================================
    public List<Sujet> searchSujets(String keyword, Long laboId, Long formationId, Long etablissementId) {
        return sujetRepository.findAll(
                SujetSpecification.getSujetsByFilter(keyword, laboId, formationId, etablissementId)
        );
    }

    // ==========================================================
    // MODULE 2: POSTULER
    // ==========================================================
    @Transactional
    public void postuler(Long candidatId, List<Long> sujetIds) throws RuntimeException {
        if (sujetIds == null || sujetIds.isEmpty()) {
            throw new RuntimeException("Vous devez sélectionner au moins un sujet.");
        }

        if (sujetIds.size() > maxChoix) {
            throw new RuntimeException("Erreur: Vous ne pouvez postuler qu'à " + maxChoix + " sujets maximum.");
        }

        List<Sujet> sujets = sujetRepository.findAllById(sujetIds);
        if (sujets.size() != sujetIds.size()) {
            throw new RuntimeException("Erreur: Certains sujets sélectionnés sont introuvables.");
        }

        for (Sujet sujet : sujets) {
            if (!sujet.isPublier()) {
                throw new RuntimeException("Le sujet '" + sujet.getTitre() + "' n'est plus disponible.");
            }

            CandidatChoix choix = new CandidatChoix();
            choix.setCandidatId(candidatId);
            choix.setSujet(sujet);
            choixRepository.save(choix);
        }
    }

    // ==========================================================
    // MODULE 3: NOTIFICATIONS
    // ==========================================================
    public List<Notification> getMyNotifications(Long candidatId) {
        return notificationRepository.findByCandidatIdOrderByIdDesc(candidatId);
    }

    public void createNotification(Long candidatId, String type) {
        Candidat candidat = candidatRepository.findById(candidatId).orElse(null);
        if (candidat != null) {
            Notification notif = new Notification();
            notif.setCandidat(candidat); // Correction ici : on associe le candidat trouvé
            notif.setType(type);
            notificationRepository.save(notif);
        }
    }

    // ==========================================================
    // MODULE 4: GESTION PROFIL (UPDATE & DIPLOMES)
    // ==========================================================

    /**
     * Met à jour les informations du profil candidat via un DTO sécurisé.
     */
    public Candidat updateCandidat(Long id, CandidatUpdateDTO dto) { // <--- Changement ici : Type CandidatUpdateDTO
        Candidat c = candidatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable"));

        // Mise à jour conditionnelle (seulement si le champ n'est pas null dans le DTO)

        // Identifiants
        if (dto.getCne() != null) c.setCne(dto.getCne());
        if (dto.getCin() != null) c.setCin(dto.getCin());

        // Infos perso
        if (dto.getNomCandidatAr() != null) c.setNomCandidatAr(dto.getNomCandidatAr());
        if (dto.getPrenomCandidatAr() != null) c.setPrenomCandidatAr(dto.getPrenomCandidatAr());
        if (dto.getSexe() != null) c.setSexe(dto.getSexe());
        if (dto.getDateDeNaissance() != null) c.setDateDeNaissance(dto.getDateDeNaissance());
        if (dto.getSituationFamiliale() != null) c.setSituationFamiliale(dto.getSituationFamiliale());
        if (dto.getTypeDeHandiCape() != null) c.setTypeDeHandiCape(dto.getTypeDeHandiCape());

        // Coordonnées
        if (dto.getTelCandidat() != null) c.setTelCandidat(dto.getTelCandidat());
        if (dto.getAdresse() != null) c.setAdresse(dto.getAdresse());
        if (dto.getVilleDeNaissance() != null) c.setVilleDeNaissance(dto.getVilleDeNaissance());
        if (dto.getPays() != null) c.setPays(dto.getPays());

        return candidatRepository.save(c);
    }

    public Diplome addDiplome(Long candidatId, Diplome d) {
        Candidat c = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable"));
        d.setCandidat(c);
        return diplomeRepository.save(d);
    }

    // Auth Register (interne)
    public Candidat register(Candidat c) {
        c.setPassword(passwordEncoder.encode(c.getPassword()));
        return candidatRepository.save(c);
    }

    // fing by id
    public Optional<Candidat> findById(Long candidatId){
        return candidatRepository.findById(candidatId);
    }
}