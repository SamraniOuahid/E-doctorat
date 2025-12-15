package com.example.demo.professeur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.professeur.model.CommissionProfesseur;

@Repository
public interface CommissionProfesseurRepository 
        extends JpaRepository<CommissionProfesseur, Long> {

    List<CommissionProfesseur> findByProfesseur_Id(Long professeurId);
}
