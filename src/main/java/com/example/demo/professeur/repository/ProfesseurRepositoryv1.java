package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.ProfesseurModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesseurRepositoryv1 extends JpaRepository<ProfesseurModel, Long> {
}

