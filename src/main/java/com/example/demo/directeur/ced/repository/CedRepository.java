package com.example.demo.directeur.ced.repository;

import com.example.demo.professeur.model.Ced;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CedRepository extends JpaRepository<Ced,Long> {
    Optional<Ced> findByDirecteur_UserId(Long userId);
}
