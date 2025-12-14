package com.example.demo.professeur.repository;

 feature/professeur-models-ayyoub
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.professeur.model.Sujet;

@Repository
public interface SujetRepository extends JpaRepository<Sujet, Long> {

    // Sujets proposés ou liés à un professeur
    List<Sujet> findByProfesseur_Id(Long professeurId);
    

import com.example.demo.professeur.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SujetRepository extends JpaRepository<Sujet, Long> {

    // all subjects of one professor
    List<Sujet> findByProfesseur_Id(Long professeurId);
   main
}
