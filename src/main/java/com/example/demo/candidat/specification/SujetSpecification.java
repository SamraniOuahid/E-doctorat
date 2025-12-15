package com.example.demo.candidat.specification;

import com.example.demo.professeur.model.Sujet;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class SujetSpecification {

    public static Specification<Sujet> getSujetsByFilter(String keyword, Long laboId, Long formationId, Long etablissementId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filter by Keyword (Titre or Description)
            if (StringUtils.hasText(keyword)) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("titre")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
                ));
            }

            // 2. Filter by Laboratoire
            // Relation: Sujet -> Professeur  -> Laboratoire
            if (laboId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("professeur").get("laboratoire").get("id"),
                        laboId
                ));
            }

            // 3. Filter by Formation Doctorale
            // Relation: Sujet -> FormationDoctorale
            if (formationId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("formationDoctorale").get("id"),
                        formationId
                ));
            }

            // 4. Filter by Etablissement (Departement)
            // Relation: Sujet -> Professeur -> Etablissement
            if (etablissementId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("professeur").get("etablissement").get("id"),
                        etablissementId
                ));
            }

            // Always filter only "Publier = true"
            predicates.add(criteriaBuilder.isTrue(root.get("publier")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}