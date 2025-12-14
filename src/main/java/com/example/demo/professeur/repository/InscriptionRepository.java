package com.example.demo.professeur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.professeur.model.Inscription;

@Repository
public interface InscriptionRepository 
        extends JpaRepository<Inscription, Long> {

    List<Inscription> findBySujet_Id(Long sujetId);

    List<Inscription> findByValideTrue();
}

