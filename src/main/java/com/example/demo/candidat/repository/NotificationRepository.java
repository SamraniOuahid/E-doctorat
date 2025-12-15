package com.example.demo.candidat.repository;

import com.example.demo.candidat.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // « Rechercher par CandidatId »
    // « Trier les résultats afin que les plus récents (ID les plus élevés) apparaissent en premier »
    List<Notification> findByCandidatIdOrderByIdDesc(Long candidatId);
}