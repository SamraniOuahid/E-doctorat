package com.example.demo.candidat.service;

import com.example.demo.candidat.model.*;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import  com.example.demo.candidat.repository.*;
import com.example.demo.candidat.model.CandidatChoix; // Assumed Entity for linking
import com.example.demo.candidat.model.Notification;
import com.example.demo.candidat.repository.CandidatChoixRepository;
import com.example.demo.candidat.repository.NotificationRepository;
import com.example.demo.candidat.repository.SujetRepository;
import com.example.demo.candidat.specification.SujetSpecification;
import com.example.demo.professeur.model.Sujet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatService {
    @Autowired
    private SujetRepository sujetRepository;

    @Autowired
    private CandidatChoixRepository choixRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private DiplomeRepository diplomeRepository;
    private CandidatRepository candidatRepository;
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
    // MODULE 2: POSTULER (Dynamic Max Choices)
    // ==========================================================

    @Transactional
    public void postuler(Long candidatId, List<Long> sujetIds) throws RuntimeException {
        // Validation: Empty check
        if (sujetIds == null || sujetIds.isEmpty()) {
            throw new RuntimeException("Vous devez sélectionner au moins un sujet.");
        }

        // 2. USE THE VARIABLE HERE (No more hardcoded 3)
        if (sujetIds.size() > maxChoix) {
            throw new RuntimeException("Erreur: Vous ne pouvez postuler qu'à " + maxChoix + " sujets maximum.");
        }

        // 3. Verify Subjects Exist and are Published
        List<Sujet> sujets = sujetRepository.findAllById(sujetIds);
        if (sujets.size() != sujetIds.size()) {
            throw new RuntimeException("Erreur: Certains sujets sélectionnés sont introuvables.");
        }

        // 4. Save Selection
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
        Notification notif = new Notification();
        notif.setCandidat(notif.getCandidat());
        notif.setType(type);
        notificationRepository.save(notif);
    }
    //    2) Mise à jour infos + CV + photo oo

    public Candidat updateCandidat(Long id, Candidat dto){
        Candidat c = candidatRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Candidat introuvable"));
        c.setNomCandidatAr(dto.getNomCandidatAr());
        c.setPrenomCandidatAr(dto.getPrenomCandidatAr());
        c.setAdresse(dto.getAdresse());
        c.setTelCandidat(dto.getTelCandidat());
        c.setPathCv(dto.getPathCv());
        c.setPathPhoto(dto.getPathPhoto());

        return candidatRepository.save(c);
    }
}
