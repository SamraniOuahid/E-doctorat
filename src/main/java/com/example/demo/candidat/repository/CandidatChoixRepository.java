package com.example.demo.candidat.repository;

import com.example.demo.candidat.model.CandidatChoix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatChoixRepository extends JpaRepository<CandidatChoix, Long> {

    // 1. Find all subjects chosen by a specific candidate
    List<CandidatChoix> findByCandidatId(Long candidatId);

    // 2. Count how many choices a candidate has made (Useful for validation)
    long countByCandidatId(Long candidatId);

    // 3. Clear the basket for a candidate (Useful if they want to reset their choices)
    void deleteByCandidatId(Long candidatId);

    // 4. Check if a specific choice already exists (Prevent duplicates)
    boolean existsByCandidatIdAndSujetId(Long candidatId, Long sujetId);
}