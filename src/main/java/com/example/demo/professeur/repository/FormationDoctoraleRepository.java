package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.FormationDoctorale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationDoctoraleRepository extends JpaRepository<FormationDoctorale, Long> {
}
