package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SujetRepository extends JpaRepository<Sujet, Long> {

    // all subjects of one professor
    List<Sujet> findByProfesseur_Id(Long professeurId);
}
