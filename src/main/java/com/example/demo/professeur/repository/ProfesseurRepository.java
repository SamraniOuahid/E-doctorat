package com.example.demo.professeur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.professeur.model.ProfesseurModel;

@Repository
public interface ProfesseurRepository 
        extends JpaRepository<ProfesseurModel, Long> {
            
}
