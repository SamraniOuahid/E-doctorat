package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.ProfesseurModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesseurRepository extends JpaRepository<ProfesseurModel, Long> {
    java.util.Optional<ProfesseurModel> findByUserId(Long userId);
}
