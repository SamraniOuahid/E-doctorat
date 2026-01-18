# 📌 Projet Spring Boot – Sécurité OAuth2

## 🧩 Description générale

Ce projet est une application **Spring Boot** structurée par domaines métiers (candidat, professeur, scolarité, directeur, etc.) et sécurisée à l’aide de **Spring Security avec OAuth2**.

L’objectif principal est de fournir une architecture propre, modulaire et sécurisée pour la gestion d’un système académique (ou similaire), avec authentification et autorisation basées sur des rôles.

---

## 🚀 Technologies utilisées

* **Java**
* **Spring Boot**
* **Spring Web (REST API)**
* **Spring Data JPA**
* **Spring Security**
* **OAuth2 / JWT**
* **Hibernate**
* **Maven**
* **Base de données** : MySQL 

---

## 🔐 Sécurité – Spring Security & OAuth2

La sécurité de l’application est basée sur **OAuth2 avec JWT** :

* Authentification via un serveur OAuth2
* Génération de **JSON Web Token (JWT)**
* Autorisation basée sur les **rôles (ROLE_*)**
* Protection des endpoints REST

### 🎭 Exemples de rôles

* `ROLE_CANDIDAT`
* `ROLE_PROFESSEUR`
* `ROLE_SCOLARITE`
* `ROLE_DIRECTEUR`
* `ROLE_ADMIN`

Chaque rôle a accès uniquement aux ressources qui lui sont autorisées.

---

## 🏗️ Architecture du projet

L’architecture suit une **séparation claire des responsabilités** :

```
com.example.demo
│
├── candidat
│   ├── controller
│   ├── dto
│   ├── model
│   ├── repository
│   ├── service
│   └── specification
│
├── professeur
│   ├── controller
│   ├── dto
│   ├── model
│   ├── repository
│   └── service
│
├── scolarite
│   ├── controller
│   ├── dto
│   ├── model
│   └── service
│
├── directeur
│   ├── ced
│   ├── labo
│   │   ├── controller
│   │   ├── dto
│   │   └── service
│   └── pole
│
├── documentation
│
└── security
    ├── config
    ├── jwt
    └── oauth2
```

---

## 🧱 Couches de l’application

### 1️⃣ Controller

* Expose les **API REST**
* Gère les requêtes HTTP (`@RestController`)

### 2️⃣ DTO (Data Transfer Object)

* Transporte les données entre les couches
* Évite l’exposition directe des entités

### 3️⃣ Model (Entity)

* Représente les tables de la base de données
* Annotées avec `@Entity`

### 4️⃣ Repository

* Accès aux données via **Spring Data JPA**
* Interfaces `JpaRepository`

### 5️⃣ Service

* Contient la **logique métier**
* Intermédiaire entre controller et repository

### 6️⃣ Specification

* Utilisée pour les **recherches dynamiques**
* Basée sur `JpaSpecificationExecutor`

---

## 🔄 Flux d’authentification OAuth2

1. L’utilisateur s’authentifie
2. Le serveur OAuth2 valide les identifiants
3. Un **JWT** est généré
4. Le client envoie le token dans le header :

   ```
   Authorization: Bearer <token>
   ```
5. Spring Security vérifie le token
6. Accès autorisé ou refusé selon le rôle

---

## ⚙️ Configuration (exemple)

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/auth
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/auth/.well-known/jwks.json
```

---

## ▶️ Lancer le projet

```bash
mvn clean install
mvn spring-boot:run
```

Accès API :

```
http://localhost:8080/api
```

---

## 🧪 Tests

* Tests unitaires avec **JUnit**
* Tests de sécurité avec **MockMvc**

---

## 📚 Documentation

Le dossier `documentation` contient :

* Diagrammes
* Spécifications techniques
* Documentation API

---

## ✅ Bonnes pratiques appliquées

* Architecture en couches
* Séparation métier par domaine
* Sécurité centralisée
* DTO pour la communication
* Code maintenable et évolutif

---

# 📚 E-Doctorat API Documentation

Documentation complète des endpoints de l'application de gestion de doctorat.

**Base URL** : `http://localhost:9090`

## 🔐 Authentification & Sécurité

La majorité des routes sont protégées. Vous devez inclure le token JWT dans le header de chaque requête.

* **Header** : `Authorization`
* **Value** : `Bearer <votre_token_jwt>`

---

## 🚀 1. Authentification (Auth)

| Méthode | Endpoint | Description | Body Requis |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Créer un nouveau compte candidat | `{ "email": "...", "password": "...", "role": "CANDIDAT", ... }` |
| `POST` | `/api/auth/login` | Se connecter | `{ "email": "...", "password": "..." }` |

---

## 🎓 2. Espace Candidat

**Base URL** : `/api/candidats`

### Gestion du Profil
| Méthode | Endpoint | Description | Body / Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/{id}` | Récupérer les infos du profil | - |
| `PUT` | `/{id}` | Mettre à jour le profil | **DTO JSON** :<br>`{ "cne": "...", "nomCandidatAr": "...", "adresse": "...", ... }` |
| `POST` | `/{id}/diplomes` | Ajouter un diplôme | `{ "intitule": "Master", "moyenne": 14.5, "type": "MASTER", ... }` |
| `GET` | `/qui-suis-je` | Vérifier l'utilisateur connecté | - |

### Candidature aux Thèses
| Méthode | Endpoint | Description | Body / Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/sujets` | Rechercher des sujets | Query Params :<br>`?keyword=Java&laboId=1&formationId=2` |
| `POST` | `/{id}/postuler` | Postuler à une liste de sujets | Liste d'IDs : `[1, 5, 8]` |
| `GET` | `/{id}/notifications` | Voir mes notifications | - |

---

## 👨‍🏫 3. Espace Professeur

**Base URL** : `/api/professeurs`

### Gestion des Sujets & Candidats
| Méthode | Endpoint | Description | Body / Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/{profId}/sujets` | Voir mes sujets proposés | - |
| `GET` | `/{profId}/candidats` | Voir les candidats qui ont postulé | Query Param (Optionnel) :<br>`?sujetId=10` |
| `GET` | `/sujets/{sujetId}/inscriptions` | Voir les inscriptions brutes | - |

### Décision (Acceptation / Refus)
| Méthode | Endpoint | Description | Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/inscriptions/{id}/accepter` | Accepter une candidature | - |
| `POST` | `/inscriptions/{id}/refuser` | Refuser une candidature | - |

---

## 🏛️ 4. Espace Scolarité (Administration)

**Base URL** : `/api/scolarite`

| Méthode | Endpoint | Description | Body / Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/dossiers` | Lister les dossiers candidats | Query Param :<br>`?etat=EN_ATTENTE` ou `?etat=VALIDE` |
| `GET` | `/dossiers/{id}` | Voir le détail d'un dossier | - |
| `PUT` | `/dossiers/{id}/validation` | Valider ou Rejeter le dossier administratif | `{ "etat": "VALIDE", "commentaire": "Dossier complet." }` |

---

## 🔬 5. Espace Directeur Laboratoire

**Base URL** : `/api/directeur-labo`

| Méthode | Endpoint | Description | Body / Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/{laboId}/sujets` | Voir les sujets du labo | - |
| `GET` | `/{laboId}/candidats` | Voir les candidats du labo | Query Param : `?sujetId=X` |
| `GET` | `/{laboId}/resultats` | Voir les résultats (notes) | - |
| `GET` | `/{laboId}/inscrits` | Voir la liste finale des inscrits | - |
| `GET` | `/{laboId}/pv-global` | 📥 **Télécharger le PV (PDF)** | *Response: Blob/File* |

---

## 🎓 6. Espace Directeur CED (Centre Études Doctorales)

**Base URL** : `/api/ced`

| Méthode | Endpoint | Description | Body / Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/{cedId}/formations/{fId}/sujets` | Sujets par formation | - |
| `GET` | `/{cedId}/formations/{fId}/candidats` | Candidats par formation | - |
| `GET` | `/{cedId}/resultats` | Résultats globaux du CED | - |
| `GET` | `/{cedId}/inscrits` | Liste des inscrits au CED | - |
| `GET` | `/{cedId}/rapports/inscription` | 📥 **Télécharger Rapport (CSV)** | *Response: Blob/File* |

---

## 🏢 7. Espace Directeur Pôle

**Base URL** : `/api/directeur/pole`

| Méthode | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/candidats` | Vue globale de tous les candidats |
| `GET` | `/sujets` | Vue globale de tous les sujets |
| `GET` | `/resultats` | Vue globale des résultats |
| `GET` | `/inscriptions` | Vue globale des inscriptions |
| `GET` | `/rapport-inscription` | 📥 **Télécharger le Rapport Global** |

---

## 🛠️ Codes d'Erreur Courants

* **200 OK** : Requête réussie.
* **400 Bad Request** : Erreur dans les données envoyées (ex: JSON mal formé).
* **401 Unauthorized** : Token JWT manquant ou expiré.
* **403 Forbidden** : Vous êtes connecté, mais vous n'avez pas le Rôle nécessaire pour cette action.
* **404 Not Found** : L'ID (candidat, sujet, etc.) n'existe pas.
* **500 Internal Server Error** : Erreur côté serveur (Bug).

---

## 📝 Tester avec Postman

1.  Faire un `POST /api/auth/login`.
2.  Copier le `access_token` de la réponse.
3.  Pour les requêtes suivantes, aller dans l'onglet **Authorization**.
4.  Choisir **Bearer Token**.
5.  Coller le token.
6.  Pour les routes de téléchargement (PDF/CSV), utiliser le bouton **"Send and Download"**.

## 👨‍💻 Auteur

**Yassir Mrabti**

---

## 📄 Licence

Ce projet est destiné à un usage **pédagogique et académique**.
