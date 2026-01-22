-- ============================================================
-- TEST DATA SQL FOR E-DOCTORAT
-- ============================================================
-- This script creates comprehensive test data for:
-- 1. Inscriptions (LP and LA candidates)
-- 2. Publication results
-- 3. Notifications
-- 4. Scolarite users assigned to laboratories
-- ============================================================

-- First, ensure we have some real inscriptions
-- Clean existing test data if needed
DELETE FROM candidat_notification WHERE candidat_id IN (11, 13, 16, 17, 18);
DELETE FROM professeur_inscription WHERE id IN (100, 101, 102, 103, 104);

-- ============================================================
-- 1. CREATE TEST INSCRIPTIONS (LP and LA candidates)
-- ============================================================

-- Candidates for Liste Principale (ADMIS)
INSERT INTO professeur_inscription (id, candidat_id, sujet_id, valider, date_dipose_dossier, remarque) VALUES
(100, 11, 1, 1, '2026-01-15', 'Excellent profil - Admis'),  -- Ouahid Samrani - ADMIS LP
(101, 13, 2, 1, '2026-01-16', 'Très bon dossier - Admis'),  -- Another candidate - ADMIS LP
(102, 16, 3, 1, '2026-01-17', 'Profil solide - Admis');     -- Yassir - ADMIS LP

-- Candidates for Liste d'Attente (EN_ATTENTE)
INSERT INTO professeur_inscription (id, candidat_id, sujet_id, valider, date_dipose_dossier, remarque) VALUES
(103, 17, 4, 0, '2026-01-18', 'Bon profil - Liste d attente'),  -- Mohamed - LA
(104, 18, 5, 0, '2026-01-19', 'Profil acceptable - LA');        -- Last candidate - LA

-- ============================================================
-- 2. CREATE PUBLICATION RESULTS
-- ============================================================

-- Mark LP as published
INSERT INTO pole_publication_resultat (id, type, publiee, date_publication, published_by) VALUES
(1, 'PRINCIPALE', 1, '2026-01-21 22:00:00', 'pole.dec@edoctorat.ma');

-- LA not yet published (will test publication flow)
INSERT INTO pole_publication_resultat (id, type, publiee, date_publication, published_by) VALUES
(2, 'ATTENTE', 0, NULL, NULL);

-- ============================================================
-- 3. CREATE NOTIFICATIONS FOR LP CANDIDATES
-- ============================================================

-- Enhanced notification structure with new fields
INSERT INTO candidat_notification (id, type, candidat_id, message, lue, date_creation, type_notification) VALUES
(1, 'RESULT_LP', 11, 'Félicitations ! Vous êtes admis sur la Liste Principale pour le sujet "Application de l IA dans la médecine".', 0, '2026-01-21 22:01:00', 'RESULT_LP'),
(2, 'RESULT_LP', 13, 'Félicitations ! Vous êtes admis sur la Liste Principale pour le sujet "Optimisation des réseaux 5G".', 0, '2026-01-21 22:01:00', 'RESULT_LP'),
(3, 'RESULT_LP', 16, 'Félicitations ! Vous êtes admis sur la Liste Principale.', 0, '2026-01-21 22:01:00', 'RESULT_LP');

-- Phase change notification (example)
INSERT INTO candidat_notification (id, type, candidat_id, message, lue, date_creation, type_notification) VALUES
(4, 'PHASE_CHANGE', 11, 'La phase "Dépôt des dossiers" est maintenant active. Vous pouvez soumettre votre candidature.', 1, '2026-01-10 10:00:00', 'PHASE_CHANGE'),
(5, 'PHASE_CHANGE', 13, 'La phase "Dépôt des dossiers" est maintenant active.', 1, '2026-01-10 10:00:00', 'PHASE_CHANGE');

-- ============================================================
-- 4. CREATE SCOLARITE USERS FOR DIFFERENT LABORATORIES
-- ============================================================

-- Scolarite for LISIA (id=1 or 3)
INSERT INTO sec_user_account (id, email, full_name, password, provider, enabled, active_role) VALUES
(200, 'scolarite.lisia@usmba.ac.ma', 'Scolarité LISIA', '$2a$10$N9qo8uLOickgx2ZMD1n9K.ZzAx3VoM4f7kVnEpwBZ/1UWo1OtN.hK', 'LOCAL', 1, 'SCOLARITE');

-- Scolarite for LMA (id=4)
INSERT INTO sec_user_account (id, email, full_name, password, provider, enabled, active_role) VALUES
(201, 'scolarite.lma@usmba.ac.ma', 'Scolarité LMA', '$2a$10$N9qo8uLOickgx2ZMD1n9K.ZzAx3VoM4f7kVnEpwBZ/1UWo1OtN.hK', 'LOCAL', 1, 'SCOLARITE');

-- Scolarite for LPMM (id=5)
INSERT INTO sec_user_account (id, email, full_name, password, provider, enabled, active_role) VALUES
(202, 'scolarite.lpmm@usmba.ac.ma', 'Scolarité LPMM', '$2a$10$N9qo8uLOickgx2ZMD1n9K.ZzAx3VoM4f7kVnEpwBZ/1UWo1OtN.hK', 'LOCAL', 1, 'SCOLARITE');

-- Add roles for scolarite users
INSERT INTO sec_user_roles (user_id, role) VALUES
(200, 'SCOLARITE'),
(201, 'SCOLARITE'),
(202, 'SCOLARITE');

-- ============================================================
-- 5. UPDATE PHASES TO REALISTIC DATES FOR TESTING
-- ============================================================

-- Update phases to have some in the past, some current, some future
UPDATE pole_phase SET 
    date_debut = '2025-12-01', 
    date_fin = '2025-12-31',
    statut = 'TERMINE'
WHERE code = 'PROP_SUJETS';

UPDATE pole_phase SET 
    date_debut = '2026-01-01', 
    date_fin = '2026-01-31',
    statut = 'EN_COURS'
WHERE code = 'DEPOT_DOSSIERS';

UPDATE pole_phase SET 
    date_debut = '2026-02-01', 
    date_fin = '2026-02-15',
    statut = 'PLANIFIE'
WHERE code = 'PRESELECTION';

UPDATE pole_phase SET 
    date_debut = '2026-02-20', 
    date_fin = '2026-03-05',
    statut = 'PLANIFIE'
WHERE code = 'ENTRETIENS';

UPDATE pole_phase SET 
    date_debut = '2026-03-10', 
    date_fin = '2026-03-15',
    statut = 'PLANIFIE'
WHERE code = 'RESULTATS';

-- ============================================================
-- 6. VERIFY DATA
-- ============================================================

-- Check inscriptions
SELECT 
    i.id,
    c.nom_candidat_ar AS nom,
    c.prenom_candidat_ar AS prenom,
    s.titre AS sujet,
    i.valider AS admis,
    CASE WHEN i.valider = 1 THEN 'LP' ELSE 'LA' END AS liste
FROM professeur_inscription i
JOIN candidat_candidat c ON i.candidat_id = c.id
JOIN professeur_sujet s ON i.sujet_id = s.id
WHERE i.id >= 100;

-- Check notifications
SELECT 
    n.id,
    n.type_notification,
    c.nom_candidat_ar AS candidat,
    n.message,
    n.lue,
    n.date_creation
FROM candidat_notification n
JOIN candidat_candidat c ON n.candidat_id = c.id
ORDER BY n.date_creation DESC;

-- Check scolarite users
SELECT 
    u.id,
    u.email,
    u.full_name,
    GROUP_CONCAT(r.role) AS roles
FROM sec_user_account u
LEFT JOIN sec_user_roles r ON u.id = r.user_id
WHERE u.email LIKE 'scolarite.%'
GROUP BY u.id;

-- ============================================================
-- CREDENTIALS FOR TESTING
-- ============================================================
-- Email: scolarite.lisia@usmba.ac.ma
-- Email: scolarite.lma@usmba.ac.ma  
-- Email: scolarite.lpmm@usmba.ac.ma
-- Password (for all): password123
-- (Password hash: $2a$10$N9qo8uLOickgx2ZMD1n9K.ZzAx3VoM4f7kVnEpwBZ/1UWo1OtN.hK)
-- ============================================================

-- ============================================================
-- NOTES:
-- 1. Scolarite entity table needs to be created (see implementation plan)
-- 2. After creating scolarite table, link these users to their labs:
--    INSERT INTO scolarite (user_id, labo_id) VALUES
--    (200, 3),  -- LISIA
--    (201, 4),  -- LMA  
--    (202, 5);  -- LPMM
-- 3. For WebSocket testing, publish LA list via Directeur Pole dashboard
-- 4. Candidat profile issue: check that user_id is properly set in candidat_candidat table
-- ============================================================
