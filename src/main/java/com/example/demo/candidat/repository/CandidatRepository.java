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

    // Efficient paginated query for Scolarite dashboard
    // Joins conceptually via subquery on ID since CandidatChoix has candidatId but no direct relation
    @org.springframework.data.jpa.repository.Query("""
        SELECT c FROM Candidat c 
        WHERE c.id IN (
            SELECT ch.candidatId FROM CandidatChoix ch 
            WHERE ch.sujet.professeur.laboratoire.id = :laboId
        )
        AND (:etat IS NULL OR c.etatDossier = :etat)
    """)
    org.springframework.data.domain.Page<Candidat> findByLaboAndEtat(
        @org.springframework.data.repository.query.Param("laboId") Long laboId, 
        @org.springframework.data.repository.query.Param("etat") EtatDossier etat, 
        org.springframework.data.domain.Pageable pageable
    );
}
