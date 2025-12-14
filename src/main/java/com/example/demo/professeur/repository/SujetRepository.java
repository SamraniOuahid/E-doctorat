package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SujetRepository extends JpaRepository<Sujet, Long> {

    // All subjects that belong to one professor
    List<Sujet> findByProfesseur_Id(Long professeurId);
}
