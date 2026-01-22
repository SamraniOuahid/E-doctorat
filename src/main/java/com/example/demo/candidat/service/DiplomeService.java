package com.example.demo.candidat.service;

import com.example.demo.candidat.dto.DiplomeDto;
import com.example.demo.candidat.model.Annexe;
import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.model.Diplome;
import com.example.demo.candidat.repository.AnnexeRepository;
import com.example.demo.candidat.repository.CandidatRepository;
import com.example.demo.candidat.repository.DiplomeRepository;
import com.example.demo.utils.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiplomeService {

    private final DiplomeRepository diplomeRepository;
    private final CandidatRepository candidatRepository;
    private final AnnexeRepository annexeRepository;
    private final FileStorageService fileStorageService;

    public List<Diplome> getDiplomesByCandidat(Long candidatId) {
        // Assuming we can fetch by candidate ID, if repository doesn't have method we
        // might need to add it
        // Or filter manually if list is small, but better add method to repo.
        // For now, let's assume we can filter or fetch all.
        // Better: let's add findByCandidatId to repository if not exists.
        // Checking repository... it was empty. I need to update it ideally.
        // Or find via Candidat entity.
        return diplomeRepository.findAll().stream()
                .filter(d -> d.getCandidat().getId().equals(candidatId))
                .toList();
    }

    @Transactional
    public Diplome addDiplome(Long candidatId, DiplomeDto dto, MultipartFile file) throws IOException {
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));

        Diplome diplome = new Diplome();
        diplome.setCandidat(candidat);
        diplome.setIntitule(dto.intitule());
        diplome.setType(dto.type());
        diplome.setDateCommission(dto.dateCommission());
        diplome.setMention(dto.mention());
        diplome.setPays(dto.pays());
        diplome.setEtablissement(dto.etablissement());
        diplome.setSpecialite(dto.specialite());
        diplome.setVille(dto.ville());
        diplome.setProvince(dto.province());
        diplome.setMoyenGenerale(dto.moyenGenerale());

        Diplome savedDiplome = diplomeRepository.save(diplome);

        if (file != null && !file.isEmpty()) {
            String filePath = fileStorageService.storeDiplome(file);
            Annexe annexe = new Annexe();
            annexe.setDiplome(savedDiplome);
            annexe.setTypeAnnexe("RELEVE_NOTES");
            annexe.setTitre("Relevé de notes - " + dto.intitule());
            annexe.setPathFile(filePath);
            annexeRepository.save(annexe);
        }

        return savedDiplome;
    }

    @Transactional
    public void deleteDiplome(Long diplomeId, Long candidatId) {
        Diplome diplome = diplomeRepository.findById(diplomeId)
                .orElseThrow(() -> new RuntimeException("Diplôme non trouvé"));

        if (!diplome.getCandidat().getId().equals(candidatId)) {
            throw new RuntimeException("Non autorisé");
        }

        diplomeRepository.delete(diplome);
    }
}
