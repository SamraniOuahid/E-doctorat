package com.example.demo.candidat.repository;

import com.example.demo.candidat.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {

    Optional<Candidat> findByEmail(String email);
}
