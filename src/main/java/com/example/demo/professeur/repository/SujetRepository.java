package com.example.demo.professeur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.professeur.model.Sujet;

@Repository
public interface SujetRepository extends JpaRepository<Sujet, Long> {

    // Sujets proposés ou liés à un professeur
    List<Sujet> findByProfesseur_Id(Long professeurId);
    
}
