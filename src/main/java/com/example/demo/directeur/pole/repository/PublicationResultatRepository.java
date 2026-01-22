package com.example.demo.directeur.pole.repository;

import com.example.demo.directeur.pole.model.PublicationResultat;
import com.example.demo.directeur.pole.model.PublicationResultat.TypeListe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicationResultatRepository extends JpaRepository<PublicationResultat, Long> {

    Optional<PublicationResultat> findByType(TypeListe type);
    
    boolean existsByTypeAndPublieeTrue(TypeListe type);
}
