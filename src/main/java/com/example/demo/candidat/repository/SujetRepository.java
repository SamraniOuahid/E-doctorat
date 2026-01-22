package com.example.demo.candidat.repository;

import com.example.demo.professeur.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SujetRepository extends JpaRepository<Sujet, Long>, JpaSpecificationExecutor<Sujet> {
    // JpaSpecificationExecutor enables the .findAll(Specification) method
    List<Sujet> findByProfesseur_Id(Long professeurId);

    // All subjects of a labo (via professeur.laboratoire.id)
    List<Sujet> findByProfesseur_Laboratoire_Id(Long laboId);

    // All subjects of a formation doctorale
    List<Sujet> findByFormationDoctorale_Id(Long formationId);

    // Combined filter: formation + labo
    List<Sujet> findByFormationDoctorale_IdAndProfesseur_Laboratoire_Id(Long formationId, Long laboId);
}
