package com.example.demo.candidat.repositrory;

import com.example.demo.candidat.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatRepository extends JpaRepository <Candidat, Integer>{

}
