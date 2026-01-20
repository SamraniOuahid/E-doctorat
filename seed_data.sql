-- 1. Insert Etablissements
INSERT INTO professeur_etablissement (id_etablissement, nom_etablissement) VALUES 
('FST', 'Faculté des Sciences et Techniques'),
('ENSA', 'Ecole Nationale des Sciences Appliquées');

-- 2. Insert User Accounts for Professors
-- Using hash for 'password123'
INSERT INTO sec_user_account (email, password, full_name, enabled, provider, verification_token) VALUES 
('prof1@uae.ac.ma', '$2a$10$MOTj65wd0m2Z9PtcJh8HFOeOh.1R1QJyUYvc2IrTw.S...', 'Professeur Un', 1, 'LOCAL', NULL),
('prof2@uae.ac.ma', '$2a$10$MOTj65wd0m2Z9PtcJh8HFOeOh.1R1QJyUYvc2IrTw.S...', 'Professeur Deux', 1, 'LOCAL', NULL);

INSERT INTO sec_user_roles (user_id, role) 
SELECT id, 'PROFESSEUR' FROM sec_user_account WHERE email IN ('prof1@uae.ac.ma', 'prof2@uae.ac.ma');

-- 3. Insert Professeurs (Initially without Labo)
INSERT INTO professeur (cin, grade, nombre_encadrer, nombre_proposer, numsom, tel_professeur, user_id, etablissement_id, labo_id, path_photo) 
SELECT 'KA12345', 'PES', 5, 3, '11111', '0600000001', id, 'FST', NULL, NULL FROM sec_user_account WHERE email = 'prof1@uae.ac.ma';

INSERT INTO professeur (cin, grade, nombre_encadrer, nombre_proposer, numsom, tel_professeur, user_id, etablissement_id, labo_id, path_photo) 
SELECT 'KB54321', 'PH', 4, 2, '22222', '0600000002', id, 'ENSA', NULL, NULL FROM sec_user_account WHERE email = 'prof2@uae.ac.ma';

-- 4. Insert CED (Centre d'Etudes Doctorales)
INSERT INTO professeur_ced (titre, initiale, description, directeur_id, path_image) 
SELECT 'Sciences et Techniques de l Ingénieur', 'STI', 'CED pour les sciences exactes', id, NULL FROM professeur WHERE cin = 'KA12345';

-- 5. Insert Laboratoires
-- Check schema for existing columns only
INSERT INTO professeur_laboratoire (nom_laboratoire, initial, description, ced_id, directeur_id, etablissement_id, path_image) 
SELECT 'Laboratoire d Informatique, Systèmes et Télécommunications', 'LIST', 'Recherche en IT', 
(SELECT id FROM professeur_ced WHERE initiale = 'STI'), 
(SELECT id FROM professeur WHERE cin = 'KA12345'), 
'FST', NULL;

INSERT INTO professeur_laboratoire (nom_laboratoire, initial, description, ced_id, directeur_id, etablissement_id, path_image) 
SELECT 'Laboratoire de Génie Civil', 'LGC', 'Recherche en GC', 
(SELECT id FROM professeur_ced WHERE initiale = 'STI'), 
(SELECT id FROM professeur WHERE cin = 'KB54321'), 
'ENSA', NULL;

-- 6. Update Professeurs with Labo ID
UPDATE professeur SET labo_id = (SELECT id FROM professeur_laboratoire WHERE initial = 'LIST') WHERE cin = 'KA12345';
UPDATE professeur SET labo_id = (SELECT id FROM professeur_laboratoire WHERE initial = 'LGC') WHERE cin = 'KB54321';

-- 7. Insert Formation Doctorale
INSERT INTO professeur_formationdoctorale (titre, initiale, axe_de_recherche, date_accreditation, ced_id, etablissement_id, path_image) 
SELECT 'Informatique et Mathématiques', 'IM', 'IA, Big Data, Sécurité', '2020-01-01', 
(SELECT id FROM professeur_ced WHERE initiale = 'STI'), 
'FST', NULL;

-- 8. Insert Sujets (Thesis Topics)
INSERT INTO professeur_sujet (titre, description, publier, professeur_id, formation_doctorale_id, co_directeur_id) 
SELECT 'Application de l IA dans la médecine', 'Sujet de thèse portant sur...', 1, 
(SELECT id FROM professeur WHERE cin = 'KA12345'), 
(SELECT id FROM professeur_formationdoctorale WHERE initiale = 'IM'), 
NULL;

INSERT INTO professeur_sujet (titre, description, publier, professeur_id, formation_doctorale_id, co_directeur_id) 
SELECT 'Optimisation des réseaux 5G', 'Sujet sur les réseaux...', 1, 
(SELECT id FROM professeur WHERE cin = 'KA12345'), 
(SELECT id FROM professeur_formationdoctorale WHERE initiale = 'IM'), 
NULL;
