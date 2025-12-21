package com.example.demo.directeur.pole.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.directeur.pole.dto.PoleCandidatDto;
import com.example.demo.directeur.pole.dto.PoleInscriptionDto;
import com.example.demo.directeur.pole.dto.PoleResultatDto;
import com.example.demo.directeur.pole.dto.PoleSujetDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DirecteurPoleServiceImpl implements DirecteurPoleService {

    // Tu injecteras les repositories plus tard
    // private final CandidatRepository candidatRepository;
    // private final SujetRepository sujetRepository;
    // private final InscriptionRepository inscriptionRepository;

    @Override
    public List<PoleCandidatDto> getAllCandidats() {
        return List.of(); // temporaire
    }

    @Override
    public List<PoleSujetDto> getAllSujets() {
        return List.of();
    }

    @Override
    public List<PoleResultatDto> getResultats() {
        return List.of();
    }

    @Override
    public List<PoleInscriptionDto> getInscriptions() {
        return List.of();
    }

    @Override
    public byte[] telechargerRapportInscription() {
        return new byte[0]; // PDF plus tard
    }
}

