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

## 👨‍💻 Auteur

**Yassir Mrabti**

---

## 📄 Licence

Ce projet est destiné à un usage **pédagogique et académique**.
