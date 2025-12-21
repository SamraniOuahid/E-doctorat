package com.example.demo.directeur.pole.service;

import java.util.List;

import com.example.demo.directeur.pole.dto.PoleCandidatDto;
import com.example.demo.directeur.pole.dto.PoleInscriptionDto;
import com.example.demo.directeur.pole.dto.PoleResultatDto;
import com.example.demo.directeur.pole.dto.PoleSujetDto;

public interface DirecteurPoleService {

    List<PoleCandidatDto> getAllCandidats();
    List<PoleSujetDto> getAllSujets();
    List<PoleResultatDto> getResultats();
    List<PoleInscriptionDto> getInscriptions();

    byte[] telechargerRapportInscription();
}
