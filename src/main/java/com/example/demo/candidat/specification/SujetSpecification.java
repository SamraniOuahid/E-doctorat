package com.example.demo.candidat.specification;

import com.example.demo.professeur.model.Sujet;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class SujetSpecification {

    public static Specification<Sujet> getSujetsByFilter(String keyword, Long laboId, Long formationId,
            Long professeurId, Boolean disponible) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filter by Keyword (Titre or Description)
            if (StringUtils.hasText(keyword)) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                // Use as(String.class) ensures Hibernate treats the field as a String for
                // functions like lower()
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("titre").as(String.class)), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description").as(String.class)),
                                pattern)));
            }

            // 2. Filter by Professeur
            if (professeurId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("professeur").get("id"),
                        professeurId));
            }

            // 3. Filter by Laboratoire
            // Relation: Sujet -> Professeur -> Laboratoire
            if (laboId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("professeur").get("laboratoire").get("id"),
                        laboId));
            }

            // 4. Filter by Formation Doctorale
            // Relation: Sujet -> FormationDoctorale
            if (formationId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("formationDoctorale").get("id"),
                        formationId));
            }

            // 6. Filter by disponibilité (publier)
            if (disponible != null) {
                if (disponible) {
                    predicates.add(criteriaBuilder.isTrue(root.get("publier")));
                } else {
                    predicates.add(criteriaBuilder.isFalse(root.get("publier")));
                }
            } else {
                // Default: only show published subjects
                predicates.add(criteriaBuilder.isTrue(root.get("publier")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}