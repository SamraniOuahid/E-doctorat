package com.example.demo.candidat.repository;

import com.example.demo.candidat.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
}
