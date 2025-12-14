package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscriptionRepositoryv1 extends JpaRepository<Inscription, Long> {

    // all inscriptions for one subject
    List<Inscription> findBySujet_Id(Long sujetId);

    // all inscriptions for subjects that belong to one professor
    @Query("""
           SELECT i
           FROM Inscription i
           WHERE i.sujet.professeur.id = :profId
           """)
    List<Inscription> findByProfesseurId(@Param("profId") Long profId);
}
