package com.example.demo.candidat.repository;

import com.example.demo.candidat.model.Annexe;
import com.example.demo.candidat.model.Diplome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiplomeRepository extends JpaRepository<Diplome, Long> {

}
