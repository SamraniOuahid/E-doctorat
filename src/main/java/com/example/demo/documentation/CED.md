📊 Module : Tableau de Bord Directeur CED & Recherche Avancée
1. 🧭 Vue d’ensemble

Cette mise à jour introduit le module Directeur CED, permettant :

La gestion des formations doctorales

La validation des sujets de thèse

Le suivi des candidats

La consultation des résultats et inscrits

Elle intègre également un Moteur de Recherche Dynamique pour les sujets de thèse (Sujet) basé sur les JPA Specifications, offrant un filtrage avancé et performant.

2. 🔧 Modifications Majeures & Refactoring
A. Refactoring du Repository

Déplacement de SujetRepository
Déplacé du package professeur vers candidat pour une meilleure cohérence logique.

Extension des capacités
Le repository étend désormais :

JpaSpecificationExecutor<Sujet>


permettant l’utilisation de l’API Criteria pour un filtrage dynamique.

B. Spécification de Recherche (SujetSpecification)

Implémentation d’une stratégie de filtrage flexible permettant de rechercher les sujets selon :

🔍 Mots-clés
Recherche dans le titre et la description.

🏛️ Relations
Filtrage par :

Laboratoire

Formation doctorale

Établissement

🔐 Sécurité
Application automatique du critère :

publier = true

C. Logique Service (DirecteurCedService)

🔗 Combinaison sécurisée des filtres
Utilisation de Specification.and() pour combiner :

Les filtres saisis par l’utilisateur

Les règles de sécurité (un Directeur ne voit que les sujets de son CED)

⚡ Optimisation des performances

Remplacement du filtrage Java (Stream) par des requêtes SQL optimisées

Utilisation de :

findBySujetIn(List<Sujet>)


pour récupérer efficacement les candidatures

📈 Reporting

Ajout de la génération d’un rapport CSV pour les doctorants inscrits

3. ⚙️ Mises à jour de la Configuration

🔍 Scan Spring Boot

Simplification de la configuration @EnableJpaRepositories

Garantie de la détection correcte de tous les modules :

ced

candidat

professeur

🗄️ Initialisation des données

Recommandation d’utiliser data.sql pour les données statiques
(ex. : noms des CED)

Éviter l’utilisation de application.properties pour ces données

4. 🌐 Endpoints API – DirecteurCedController

Base URL : /api/ced

Port : 9090

| Méthode | Endpoint                                 | Description                                            |
| ------- | ---------------------------------------- | ----------------------------------------------------   |
| GET     | `/{cedId}/formations/{formId}/sujets`    | Récupérer les sujets d’une formation (filtrés par CED) |
| GET     | `/{cedId}/formations/{formId}/candidats` | Voir les candidats ayant postulé à ces sujets          |
| GET     | `/{cedId}/resultats`                     | Consulter les résultats globaux du CED                 |
| GET     | `/{cedId}/inscrits/csv`                  | Télécharger le rapport CSV des inscrits                |
