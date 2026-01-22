package com.example.demo.directeur.pole.repository;

import com.example.demo.directeur.pole.model.Phase;
import com.example.demo.directeur.pole.model.PhaseStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {

    /**
     * Find all phases ordered by their sequence
     */
    List<Phase> findAllByOrderByOrdreAsc();

    /**
     * Find the currently active phase
     */
    @Query("SELECT p FROM Phase p WHERE :date BETWEEN p.dateDebut AND p.dateFin")
    Optional<Phase> findActivePhase(LocalDate date);

    /**
     * Find phase by code
     */
    Optional<Phase> findByCode(String code);

    /**
     * Find phases by status
     */
    List<Phase> findByStatut(PhaseStatut statut);
}
