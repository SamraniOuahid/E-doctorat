package com.example.demo.candidat.controller;

import com.example.demo.candidat.dto.CandidatProfilDto;
import com.example.demo.candidat.dto.DiplomeDto;
import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.model.Diplome;
import com.example.demo.candidat.service.CandidatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidats")
@RequiredArgsConstructor
public class CandidatController {
    private final CandidatService candidatService;

    @PutMapping("/{id}/profile")
    public Candidat updateProfile(@PathVariable Long id, @RequestBody CandidatProfilDto dto){
        Candidat c = new Candidat();
        c.setNomCandidatAr(dto.nomCandidatAr());
        c.setPrenomCandidatAr(dto.prenomCandidatAr());
        c.setAdresse(dto.adresse());
        c.setTelCandidat(dto.telCandidat());
        c.setPathCv(dto.pathCv());
        c.setPathPhoto(dto.pathPhoto());

        return candidatService.updateCandidat(id, c);
    }

    @PostMapping("/{id}/diplomes")
    public Diplome addDiplome(@PathVariable Long id,
                              @RequestBody DiplomeDto dto) {

        Diplome d = new Diplome();
        d.setIntitule(dto.intitule());
        d.setType(dto.type());
        d.setDateCommission(dto.dateCommission());
        d.setMention(dto.mention());
        d.setPays(dto.pays());
        d.setEtablissement(dto.etablissement());
        d.setSpecialite(dto.specialite());
        d.setVille(dto.ville());
        d.setProvince(dto.province());
        d.setMoyenGenerale(dto.moyenGenerale());

        return candidatService.addDiplome(id, d);
    }
}
