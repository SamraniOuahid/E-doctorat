package com.example.demo.candidat.service;

import com.example.demo.candidat.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import  com.example.demo.candidat.repository.*;
@Service
@RequiredArgsConstructor
public class CandidatService {
    private DiplomeRepository diplomeRepository;
    private CandidatRepository candidatRepository;
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

    public Diplome addDiplome(Long candidatId, Diplome d) {
        Candidat c = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable"));
        d.setCandidat(c);
        return diplomeRepository.save(d);
    }
}
