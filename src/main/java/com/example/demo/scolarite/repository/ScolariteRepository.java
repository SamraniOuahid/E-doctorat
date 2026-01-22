package com.example.demo.scolarite.repository;

import com.example.demo.scolarite.model.Scolarite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScolariteRepository extends JpaRepository<Scolarite, Long> {
    Optional<Scolarite> findByUser_Email(String email);
    Optional<Scolarite> findByUser_Id(Long userId);
}
