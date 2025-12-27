package com.example.demo.directeur.labo.service;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.directeur.labo.dto.LaboCandidatDto;
import com.example.demo.directeur.labo.dto.LaboResultatDto;
import com.example.demo.directeur.labo.dto.LaboSujetDto;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.repository.InscriptionRepository;
import com.example.demo.candidat.repository.SujetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirecteurLaboServiceImpl implements DirecteurLaboService {

    private final SujetRepository sujetRepository;
    private final InscriptionRepository inscriptionRepository;

    // ================== SUJETS DU LABO ==================

    @Override
    public List<LaboSujetDto> getSujetsDuLabo(Long laboId) {
        List<Sujet> sujets = sujetRepository.findByProfesseur_Laboratoire_Id(laboId);
        return sujets.stream()
                .map(this::toSujetDto)
                .toList();
    }

    // ================== CANDIDATS DU LABO ==================

    @Override
    public List<LaboCandidatDto> getCandidatsDuLabo(Long laboId, Long sujetId) {
        List<Inscription> inscriptions;

        if (sujetId != null) {
            // candidats d’un sujet précis du labo
            inscriptions = inscriptionRepository.findBySujet_Id(sujetId);
        } else {
            // tous les candidats qui ont postulé sur les sujets de ce labo
            inscriptions = inscriptionRepository.findByLaboId(laboId);
        }

        return inscriptions.stream()
                .map(this::toCandidatDto)
                .toList();
    }

    // ================== RESULTATS ==================

    @Override
    public List<LaboResultatDto> getResultatsDuLabo(Long laboId) {
        // tous les résultats du labo (acceptés + refusés + en attente)
        List<Inscription> inscriptions = inscriptionRepository.findByLaboId(laboId);

        return inscriptions.stream()
                .map(this::toResultatDto)
                .toList();
    }

    // ================== INSCRITS ==================

    @Override
    public List<LaboResultatDto> getInscritsDuLabo(Long laboId) {
        // seulement les inscriptions validées
        List<Inscription> inscriptions = inscriptionRepository.findAcceptedByLaboId(laboId);

        return inscriptions.stream()
                .map(this::toResultatDto)
                .toList();
    }

    // ================== PV GLOBAL ==================

    @Override
    public byte[] genererPvGlobal(Long laboId) {
        // Plus tard : générer le PDF (iText / OpenPDF / JasperReports, etc.)
        // Pour l’instant, on respecte juste la signature pour que le TP compile.
        return new byte[0];
    }

    // ================== MAPPERS ==================

    private LaboSujetDto toSujetDto(Sujet sujet) {
        LaboSujetDto dto = new LaboSujetDto();
        dto.setSujetId(sujet.getId());
        dto.setSujetTitre(sujet.getTitre());

        if (sujet.getProfesseur() != null) {
            dto.setProfesseurId(sujet.getProfesseur().getId());
            dto.setProfesseurGrade(sujet.getProfesseur().getGrade());
            dto.setProfesseurNumSom(sujet.getProfesseur().getNumSOM());
        }

        return dto;
    }

    private LaboCandidatDto toCandidatDto(Inscription i) {
        LaboCandidatDto dto = new LaboCandidatDto();

        dto.setInscriptionId(i.getId());
        dto.setSujetId(i.getSujet().getId());
        dto.setSujetTitre(i.getSujet().getTitre());

        Candidat c = i.getCandidat();
        if (c != null) {
            dto.setCandidatId(c.getId());
            dto.setCandidatNomComplet(c.getNomComplet()); // tu as déjà ce getter
            dto.setCandidatCne(c.getCne());
        }

        dto.setValider(i.getValider());
        dto.setRemarque(i.getRemarque());

        return dto;
    }

    private LaboResultatDto toResultatDto(Inscription i) {
        LaboResultatDto dto = new LaboResultatDto();

        dto.setInscriptionId(i.getId());
        dto.setSujetId(i.getSujet().getId());
        dto.setSujetTitre(i.getSujet().getTitre());

        Candidat c = i.getCandidat();
        if (c != null) {
            dto.setCandidatId(c.getId());
            dto.setCandidatNomComplet(c.getNomComplet());
            dto.setCandidatCne(c.getCne());
        }

        Boolean valider = i.getValider();
        dto.setValider(valider);

        String statut;
        if (Boolean.TRUE.equals(valider)) {
            statut = "ACCEPTE";
        } else if (Boolean.FALSE.equals(valider)) {
            statut = "REFUSE";
        } else {
            statut = "EN_ATTENTE";
        }
        dto.setStatut(statut);

        dto.setRemarque(i.getRemarque());

        return dto;
    }
}
