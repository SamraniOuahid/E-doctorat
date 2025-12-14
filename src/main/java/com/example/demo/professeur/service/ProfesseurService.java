package com.example.demo.professeur.service;

import java.util.List;

import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;

public interface ProfesseurService {

    List<Sujet> getSujetsDuProfesseur(Long professeurId);

    List<Inscription> getCandidatsParSujet(Long sujetId);

    void accepterCandidature(Long inscriptionId);

    void refuserCandidature(Long inscriptionId);

    List<Inscription> getInscriptionsFinales(Long sujetId);

    byte[] genererPvSujet(Long sujetId);
    
}
