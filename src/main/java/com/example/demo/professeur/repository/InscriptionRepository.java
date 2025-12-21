package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findBySujet_FormationDoctorale_Ced_Id(Long cedId);
    // All inscriptions for one subject
    List<Inscription> findBySujet_Id(Long sujetId);

    // All inscriptions that are validated (anywhere)
    List<Inscription> findByValiderTrue();

    // All inscriptions for subjects that belong to one professor
    @Query("""
           SELECT i
           FROM Inscription i
           WHERE i.sujet.professeur.id = :profId
           """)
    List<Inscription> findByProfesseurId(@Param("profId") Long profId);

<<<<<<< HEAD
    // Trouver les inscrits d'un CED spécifique
    // Inscription -> Sujet -> Formation -> Ced
    @Query("SELECT i FROM Inscription i WHERE i.sujet.formationDoctorale.ced.id = :cedId AND i.valider = true")
    List<Inscription> findInscritsByCed(@Param("cedId") Long cedId);

=======
    // ✅ All inscriptions for subjects of one labo
    @Query("""
           SELECT i
           FROM Inscription i
           WHERE i.sujet.professeur.laboratoire.id = :laboId
           """)
    List<Inscription> findByLaboId(@Param("laboId") Long laboId);

    // ✅ Only accepted inscriptions (valider = true) for one labo
    @Query("""
           SELECT i
           FROM Inscription i
           WHERE i.sujet.professeur.laboratoire.id = :laboId
             AND i.valider = true
           """)
    List<Inscription> findAcceptedByLaboId(@Param("laboId") Long laboId);
>>>>>>> 66e17cff59cd77ce3bbc14d448b3d334d2331f24
}
