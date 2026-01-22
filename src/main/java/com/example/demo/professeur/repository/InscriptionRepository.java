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
    org.springframework.data.domain.Page<Inscription> findBySujet_FormationDoctorale_Ced_Id(Long cedId, org.springframework.data.domain.Pageable pageable);
    
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
    // Trouver les inscrits d'un CED spécifique
    // Inscription -> Sujet -> Formation -> Ced
    @Query("SELECT i FROM Inscription i WHERE i.sujet.formationDoctorale.ced.id = :cedId AND i.valider = true")
    List<Inscription> findInscritsByCed(@Param("cedId") Long cedId);

    // Filter by formation
    org.springframework.data.domain.Page<Inscription> findBySujet_FormationDoctorale_Id(Long formationId, org.springframework.data.domain.Pageable pageable);

    // ✅ All inscriptions for subjects of one labo
    @Query("""
           SELECT i
           FROM Inscription i
           WHERE i.sujet.professeur.laboratoire.id = :laboId
           """)
    List<Inscription> findByLaboId(@Param("laboId") Long laboId);
    
    @Query("""
           SELECT i
           FROM Inscription i
           WHERE i.sujet.professeur.laboratoire.id = :laboId
           """)
    org.springframework.data.domain.Page<Inscription> findByLaboId(@Param("laboId") Long laboId, org.springframework.data.domain.Pageable pageable);

    // ✅ Only accepted inscriptions (valider = true) for one labo
    @Query("""
           SELECT i
           FROM Inscription i
           WHERE i.sujet.professeur.laboratoire.id = :laboId
             AND i.valider = true
           """)
    List<Inscription> findAcceptedByLaboId(@Param("laboId") Long laboId);

    // Paginated version of findAcceptedByLaboId
    @Query("""
           SELECT i
           FROM Inscription i
           WHERE i.sujet.professeur.laboratoire.id = :laboId
             AND i.valider = true
           """)
    org.springframework.data.domain.Page<Inscription> findAcceptedByLaboId(@Param("laboId") Long laboId, org.springframework.data.domain.Pageable pageable);

    // Paginated findByValiderTrue
    org.springframework.data.domain.Page<Inscription> findByValiderTrue(org.springframework.data.domain.Pageable pageable);
    
    // Pour Scolarite : Recherche et filtrage global
    // Pour Scolarite : Recherche et filtrage global
    @Query("SELECT i FROM Inscription i WHERE " +
           "(:etat IS NULL OR i.candidat.etatDossier = :etat) AND " +
           "(:search IS NULL OR " +
           "LOWER(i.candidat.nomCandidatAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.candidat.prenomCandidatAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.candidat.cne) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.candidat.cin) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.candidat.email) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    org.springframework.data.domain.Page<Inscription> findByCriteria(@Param("etat") com.example.demo.scolarite.model.EtatDossier etat, @Param("search") String search, org.springframework.data.domain.Pageable pageable);

    // Paginated findByValiderFalseOrNull (for waiting list)
    @Query("SELECT i FROM Inscription i WHERE i.valider IS NULL OR i.valider = false")
    org.springframework.data.domain.Page<Inscription> findByValiderFalseOrNull(org.springframework.data.domain.Pageable pageable);
}
