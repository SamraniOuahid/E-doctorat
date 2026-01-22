-- ============================================================
-- SCOLARITE TABLE SCHEMA & TEST DATA
-- ============================================================
-- This creates the scolarite table and test data for:
-- 1. Scolarite table linking users to laboratories
-- 2. Candidat choices to enable lab-specific filtering
-- ============================================================

-- ============================================================
-- 1. CREATE SCOLARITE TABLE (if not exists)
-- ============================================================
CREATE TABLE IF NOT EXISTS scolarite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    labo_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES sec_user_account(id) ON DELETE CASCADE,
    FOREIGN KEY (labo_id) REFERENCES professeur_laboratoire(id) ON DELETE CASCADE
);

-- ============================================================
-- 2. CREATE SCOLARITE TEST USERS
-- ============================================================

-- Scolarite users (reusing IDs from test_data.sql if they exist)
INSERT INTO sec_user_account (id, email, full_name, password, provider, enabled, active_role) VALUES
(200, 'scolarite.lisia@usmba.ac.ma', 'Scolarité LISIA', '$2a$10$N9qo8uLOickgx2ZMD1n9K.ZzAx3VoM4f7kVnEpwBZ/1UWo1OtN.hK', 'LOCAL', 1, 'SCOLARITE')
ON DUPLICATE KEY UPDATE email = email;

INSERT INTO sec_user_account (id, email, full_name, password, provider, enabled, active_role) VALUES
(201, 'scolarite.lma@usmba.ac.ma', 'Scolarité LMA', '$2a$10$N9qo8uLOickgx2ZMD1n9K.ZzAx3VoM4f7kVnEpwBZ/1UWo1OtN.hK', 'LOCAL', 1, 'SCOLARITE')
ON DUPLICATE KEY UPDATE email = email;

INSERT INTO sec_user_account (id, email, full_name, password, provider, enabled, active_role) VALUES
(202, 'scolarite.lpmm@usmba.ac.ma', 'Scolarité LPMM', '$2a$10$N9qo8uLOickgx2ZMD1n9K.ZzAx3VoM4f7kVnEpwBZ/1UWo1OtN.hK', 'LOCAL', 1, 'SCOLARITE')
ON DUPLICATE KEY UPDATE email = email;

-- Add roles
INSERT INTO sec_user_roles (user_id, role) VALUES (200, 'SCOLARITE') ON DUPLICATE KEY UPDATE role = role;
INSERT INTO sec_user_roles (user_id, role) VALUES (201, 'SCOLARITE') ON DUPLICATE KEY UPDATE role = role;
INSERT INTO sec_user_roles (user_id, role) VALUES (202, 'SCOLARITE') ON DUPLICATE KEY UPDATE role = role;

-- ============================================================
-- 3. LINK SCOLARITE USERS TO LABORATORIES
-- ============================================================

INSERT INTO scolarite (user_id, labo_id) VALUES
(200, 3),  -- scolarite.lisia@usmba.ac.ma -> LISIA (lab id=3)
(201, 4),  -- scolarite.lma@usmba.ac.ma -> LMA (lab id=4)
(202, 5)   -- scolarite.lpmm@usmba.ac.ma -> LPMM (lab id=5)
ON DUPLICATE KEY UPDATE labo_id = VALUES(labo_id);

-- ============================================================
-- 4. CREATE CANDIDATE CHOICES FOR LAB FILTERING
-- ============================================================

-- Candidates applying to LISIA subjects (lab_id=3)
INSERT INTO candidat_choix (candidat_id, sujet_id, date_choix) VALUES
(11, 1, '2026-01-10 14:30:00'),  -- Ouahid choosing LISIA subject
(13, 2, '2026-01-11 09:15:00'),  -- Candidate 13 choosing LISIA subject
(16, 3, '2026-01-12 16:45:00')   -- Yassir choosing LISIA subject
ON DUPLICATE KEY UPDATE date_choix = VALUES(date_choix);

-- Candidates applying to LMA subjects (lab_id=4)
INSERT INTO candidat_choix (candidat_id, sujet_id, date_choix) VALUES
(17, 4, '2026-01-13 10:00:00'),  -- Mohamed choosing LMA subject
(18, 5, '2026-01-14 11:30:00')   -- Candidate 18 choosing LMA subject
ON DUPLICATE KEY UPDATE date_choix = VALUES(date_choix);

-- Note: For LPMM testing, you can manually assign existing candidates to LPMM subjects
-- or create candidates through the application UI to avoid foreign key constraint issues

-- ============================================================
-- 5. VERIFY SCOLARITE LINKS
-- ============================================================

-- Check scolarite user assignments
SELECT 
    u.id AS user_id,
    u.email,
    u.full_name,
    s.labo_id,
    l.nom_laboratoire AS laboratoire
FROM scolarite s
JOIN sec_user_account u ON s.user_id = u.id
LEFT JOIN professeur_laboratoire l ON s.labo_id = l.id;

-- ============================================================
-- TEST CREDENTIALS
-- ============================================================
-- All scolarite accounts use password: password123
--
-- LISIA Scolarite (lab_id=3):
--   Email: scolarite.lisia@usmba.ac.ma
--   Should see: 3 candidates (ids: 11, 13, 16)
--
-- LMA Scolarite (lab_id=4):
--   Email: scolarite.lma@usmba.ac.ma
--   Should see: 2 candidates (ids: 17, 18)
--
-- LPMM Scolarite (lab_id=5):
--   Email: scolarite.lpmm@usmba.ac.ma
--   Create test candidates via UI or manually insert with proper user_id
-- ============================================================
