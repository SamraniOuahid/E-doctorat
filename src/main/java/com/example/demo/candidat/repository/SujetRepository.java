package com.example.demo.candidat.repository;

import com.example.demo.professeur.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SujetRepository extends JpaRepository<Sujet, Long>, JpaSpecificationExecutor<Sujet> {
    // JpaSpecificationExecutor enables the .findAll(Specification) method
}