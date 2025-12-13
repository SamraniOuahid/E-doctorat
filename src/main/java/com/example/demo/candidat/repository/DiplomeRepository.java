package com.example.demo.candidat.repository;

import com.example.demo.candidat.model.Annexe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiplomeRepository extends JpaRepository<Annexe, Long> {

}
