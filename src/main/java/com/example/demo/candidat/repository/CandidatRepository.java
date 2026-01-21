package com.example.demo.candidat.repository;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.scolarite.model.EtatDossier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {

    Optional<Candidat> findByEmail(String email);

    Optional<Candidat> findByUser_Email(String email);

    List<Candidat> findByEtatDossier(EtatDossier etatDossier);

    long countByEtatDossier(EtatDossier etatDossier);
}
