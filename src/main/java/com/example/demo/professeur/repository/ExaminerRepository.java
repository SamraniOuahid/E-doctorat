package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.Examiner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminerRepository extends JpaRepository<Examiner, Long> {

    // Trouver les résultats (Examiner) pour un CED
    @Query("SELECT e FROM Examiner e WHERE e.sujet.formationDoctorale.ced.id = :cedId")
    List<Examiner> findResultsByCed(@Param("cedId") Long cedId);
    List<Examiner> findBySujet_FormationDoctorale_Ced_Id(Long cedId);
}