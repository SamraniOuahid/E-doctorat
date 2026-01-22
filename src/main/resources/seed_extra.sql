-- ================================================================
-- SEED EXTRA DATA: CANDIDATS & INSCRIPTIONS
-- ================================================================

-- 1. ADD CANDIDATS (Students)
-- sec_user_account (id 200+)
INSERT INTO sec_user_account (id, email, password, full_name, enabled, provider, active_role) VALUES
(201, 'candidat1@gmail.com', '$2a$10$N.z.j.d.x.y.z.HASH', 'Yassine Mansouri', true, 'LOCAL', 'CANDIDAT'),
(202, 'candidat2@gmail.com', '$2a$10$N.z.j.d.x.y.z.HASH', 'Imane Tazi', true, 'LOCAL', 'CANDIDAT'),
(203, 'candidat3@gmail.com', '$2a$10$N.z.j.d.x.y.z.HASH', 'Omar Bennani', true, 'LOCAL', 'CANDIDAT'),
(204, 'candidat4@gmail.com', '$2a$10$N.z.j.d.x.y.z.HASH', 'Siham Lahlou', true, 'LOCAL', 'CANDIDAT'),
(205, 'candidat5@gmail.com', '$2a$10$N.z.j.d.x.y.z.HASH', 'Amine Radi', true, 'LOCAL', 'CANDIDAT');

-- sec_user_roles
INSERT INTO sec_user_roles (user_id, role) VALUES
(201, 'CANDIDAT'), (202, 'CANDIDAT'), (203, 'CANDIDAT'), (204, 'CANDIDAT'), (205, 'CANDIDAT');

-- candidat table
INSERT INTO candidat (id, user_id, cin, cne, date_naissance, lieu_naissance, ville, tel_candidat, adresse) VALUES
(1, 201, 'K123456', 'P13456789', '1998-05-15', 'Fès', 'Fès', '0601010101', 'Adresse Fès 1'),
(2, 202, 'L987654', 'R19876543', '1999-02-20', 'Meknès', 'Meknès', '0602020202', 'Adresse Meknès 2'),
(3, 203, 'M112233', 'S11223344', '1997-11-10', 'Casablanca', 'Casablanca', '0603030303', 'Adresse Casa 3'),
(4, 204, 'N445566', 'T44556677', '1998-08-05', 'Rabat', 'Rabat', '0604040404', 'Adresse Rabat 4'),
(5, 205, 'O778899', 'U77889900', '1999-01-25', 'Tanger', 'Tanger', '0605050505', 'Adresse Tanger 5');

-- 2. ADD INSCRIPTIONS (Candidatures to specific subjects)
-- Schema: id, valide_labo (bool), score, candidat_id, sujet_id
-- Subject IDs are usually assigned from 1, 2, ...
-- Let's assume subjects from seed_usmba.sql:
-- 1: Deep Learning (Prof 101)
-- 2: Optimisation (Prof 101)
-- 3: Sécurité (Prof 108)

INSERT INTO professeur_inscription (valider, remarque, candidat_id, sujet_id) VALUES
(NULL, 'En attente de revue', 1, 1), -- Yassine -> Deep Learning
(true, 'Excellent dossier master', 2, 1), -- Imane -> Deep Learning (PRESELECTED)
(NULL, 'A vérifier', 3, 2), -- Omar -> Optimisation
(false, 'Moyenne insuffisante', 4, 3), -- Siham -> Sécurité (REJECTED)
(true, 'Profil adéquat', 5, 1); -- Amine -> Deep Learning (PRESELECTED)

-- Extra for other subjects
INSERT INTO professeur_inscription (valider, remarque, candidat_id, sujet_id) VALUES
(NULL, NULL, 1, 2),
(NULL, NULL, 2, 3);
