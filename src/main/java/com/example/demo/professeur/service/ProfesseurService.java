package com.example.demo.professeur.service;

import com.example.demo.professeur.dto.CandidatForProfDto;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;

import java.util.List;

public interface ProfesseurService {

    // 1) "Mes sujets" : list all subjects of a professor
    List<Sujet> getSujetsDuProfesseur(Long professeurId);

    // 2) "Mes candidats"
    List<CandidatForProfDto> getCandidatsByProf(Long profId);
    List<CandidatForProfDto> getCandidatsBySujet(Long profId, Long sujetId);

    // raw inscriptions for one sujet (used in controller)
    List<Inscription> getInscriptionsBySujet(Long sujetId);

    // 3) Examiner : accept / refuse
    void accepterCandidature(Long inscriptionId);
    void refuserCandidature(Long inscriptionId);

    // 4) Final list + PV (for later)
    List<Inscription> getInscriptionsFinales(Long sujetId);
    byte[] genererPvSujet(Long sujetId);
}
