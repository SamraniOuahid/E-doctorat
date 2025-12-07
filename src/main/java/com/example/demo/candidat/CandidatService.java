package com.example.demo.candidat;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.repositrory.CandidatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatService {
    private final CandidatRepository candidatRepository;

    public CandidatService(CandidatRepository candidatRepository) {
        this.candidatRepository = candidatRepository;
    }

    public List<Candidat> getAll() {
        return candidatRepository.findAll();
    }

    public Candidat getById(int id) {
        return candidatRepository.findById(id).orElse(null);
    }

    public Candidat create(Candidat candidat) {
        // id est généré automatiquement par JPA
        return candidatRepository.save(candidat);
    }

    public List<Candidat> deleteById(int id) {
        if (!candidatRepository.existsById(id)) {
            return candidatRepository.findAll(); // ou null si tu préfères
        }
        candidatRepository.deleteById(id);
        return candidatRepository.findAll();
    }

    public Candidat update(int id, Candidat newCandidat) {
        return candidatRepository.findById(id)
                .map(old -> {
                    newCandidat.setId(id);
                    return candidatRepository.save(newCandidat);
                })
                .orElse(null);
    }


//    private List<CandidatModel> candidats = new ArrayList<>();
//    private int idCounter = 1;
//
//    public List<CandidatModel> getAll(){
//        return candidats;
//    }
//
//    public CandidatModel getById(int id) {
//        return candidats.stream()
//                .filter(c -> c.getId() == id)
//                .findFirst()
//                .orElse(null);
//    }
//
//    public CandidatModel create(CandidatModel candidat) {
//        candidat.setId(idCounter++);
//        candidats.add(candidat);
//        return candidat;
//    }
//    public boolean deleteById(int id) {
//        return candidats.removeIf(c-> c.getId() == id);
//    }
//
//    public CandidatModel update(int id, CandidatModel newCandidat) {
//
//        Optional<CandidatModel> optional = candidats.stream()
//                .filter(c -> c.getId() == id)
//                .findFirst();
//
//        if (optional.isPresent()) {
//            CandidatModel old = optional.get();
//            newCandidat.setId(id);
//            candidats.set(candidats.indexOf(old), newCandidat);
//            return newCandidat;
//        }
//
//        return null;
//    }

}
