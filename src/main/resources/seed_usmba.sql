-- ================================================================
-- SEED DATA FOR USMBA (FÈS) - E-DOCTORAT
-- ================================================================

-- 1. ETABLISSEMENT (USMBA FÈS)
-- Schema: id_etablissement (String PK), nom_etablissement (snake_case default)
INSERT INTO professeur_etablissement (id_etablissement, nom_etablissement) VALUES
('FSDM', 'Faculté des Sciences Dhar El Mahraz'),
('FST', 'Faculté des Sciences et Techniques'),
('FLSH', 'Faculté des Lettres et des Sciences Humaines'),
('FSJES', 'Faculté des Sciences Juridiques, Economiques et Sociales'),
('ENSA', 'Ecole Nationale des Sciences Appliquées'),
('EST', 'Ecole Supérieure de Technologie');

-- 2. CED (Centre d'Etudes Doctorales)
INSERT INTO professeur_ced (titre, description) VALUES
('Sciences et Techniques et Sciences Médicales', 'CED couvrant les sciences exactes, ingénierie et médecine'),
('Lettres, Sciences Humaines et Arts', 'CED pour les humanités et sciences sociales'),
('Droit, Economie et Gestion', 'CED pour les sciences juridiques et économiques');

-- 3. LABORATOIRES
-- Schema: nom_laboratoire, etablissement_id, ced_id, directeur_id, initial
INSERT INTO professeur_laboratoire (nom_laboratoire, description, initial, etablissement_id, ced_id, directeur_id, path_image) VALUES
('Laboratoire d''Informatique, Systèmes et Intelligence Artificielle', 'Recherche en IA et Systèmes', 'LISIA', 'FSDM', 1, 1, NULL),
('Laboratoire de Mathématiques et Applications', 'Analyse, Algèbre et Géométrie', 'LMA', 'FSDM', 1, 1, NULL),
('Laboratoire de Physique des Matériaux et Microélectronique', 'Physique du solide', 'LPMM', 'FSDM', 1, 1, NULL);

-- FST Labs
INSERT INTO professeur_laboratoire (nom_laboratoire, description, initial, etablissement_id, ced_id, directeur_id, path_image) VALUES
('Laboratoire de Systèmes Intelligents et Energie', 'Energy management and AI', 'LSIE', 'FST', 1, 1, NULL),
('Laboratoire de Biotechnologie et Préservation des Ressources Naturelles', 'BioTech', 'LBPRN', 'FST', 1, 1, NULL),
('Laboratoire de Modélisation et Calcul Scientifique', 'Maths appliqués', 'LMCS', 'FST', 1, 1, NULL);

-- FLSH Labs
INSERT INTO professeur_laboratoire (nom_laboratoire, description, initial, etablissement_id, ced_id, directeur_id, path_image) VALUES
('Laboratoire de Recherche sur les Langues et la Communication', 'Linguistique', 'LRLC', 'FLSH', 2, 1, NULL),
('Laboratoire Histoire et Patrimoine', 'Histoire du Maroc', 'LHP', 'FLSH', 2, 1, NULL);

-- 4. FORMATION DOCTORALE
-- Schema: titre, initiale, ced_id, etablissement_id
INSERT INTO professeur_formationdoctorale (titre, initiale, ced_id, etablissement_id) VALUES
('Mathématiques et Informatique', 'MATH-INFO', 1, 'FSDM'),
('Physique et Sciences de l''Ingénieur', 'PHY-ING', 1, 'FST'),
('Chimie et Environnement', 'CHIM-ENV', 1, 'FST'),
('Biologie et Santé', 'BIO-SANTE', 1, 'FSDM'),
('Langues, Littératures et Communication', 'LANG-LIT', 2, 'FLSH'),
('Sociologie et Psychologie', 'SOC-PSY', 2, 'FLSH'),
('Droit Privé et Sciences Criminelles', 'DROIT-PRIVE', 3, 'FSJES'),
('Economie et Gestion des Organisations', 'ECO-GEST', 3, 'FSJES');

-- 5. USERS & ROLES
-- Table: 'sec_user_account' for Users (id, email, password, full_name, enabled, provider, active_role)
-- Table: 'sec_user_roles' for Roles (user_id, role)
-- Assuming password '$2a$10$N.z.j.d.x.y.z.HASH' is a valid BCrypt hash

-- Users
INSERT INTO sec_user_account (id, email, password, full_name, enabled, provider, active_role) VALUES
(101, 'prof1@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Mohammed El Amrani', true, 'LOCAL', 'PROFESSEUR'),
(102, 'prof2@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Samira Benkirane', true, 'LOCAL', 'PROFESSEUR'),
(103, 'prof3@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Youssef Chami', true, 'LOCAL', 'PROFESSEUR'),
(104, 'prof4@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Fatima Ouazzani', true, 'LOCAL', 'PROFESSEUR'),
(105, 'prof5@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Hassan Tahiri', true, 'LOCAL', 'PROFESSEUR'),
(106, 'prof6@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Karim Berrada', true, 'LOCAL', 'PROFESSEUR'),
(107, 'prof7@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Driss Alaoui', true, 'LOCAL', 'PROFESSEUR'),
(108, 'prof8@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Noura Kadiri', true, 'LOCAL', 'PROFESSEUR'),
(109, 'prof9@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Ahmed Fassi', true, 'LOCAL', 'PROFESSEUR'),
(110, 'prof10@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Meryem Zerouali', true, 'LOCAL', 'PROFESSEUR');

-- Roles
INSERT INTO sec_user_roles (user_id, role) VALUES
(101, 'PROFESSEUR'), (102, 'PROFESSEUR'), (103, 'PROFESSEUR'), (104, 'PROFESSEUR'),
(105, 'PROFESSEUR'), (106, 'PROFESSEUR'), (107, 'PROFESSEUR'), (108, 'PROFESSEUR'),
(109, 'PROFESSEUR'), (110, 'PROFESSEUR');


-- 6. PROFESSEURS DETAILS
-- Table: 'professeur' (linked to sec_user_account via user_id)
INSERT INTO professeur (id, grade, tel_professeur, user_id, etablissement_id, labo_id, nombre_encadrer, nombre_proposer) VALUES
(101, 'PES', '0661111111', 101, 'FSDM', 1, 0, 0), -- Mohammed El Amrani
(102, 'PH', '0662222222', 102, 'FSDM', 2, 0, 0), -- Samira Benkirane
(103, 'PES', '0663333333', 103, 'FST', 4, 0, 0), -- Youssef Chami
(104, 'PA', '0664444444', 104, 'FST', 5, 0, 0), -- Fatima Ouazzani
(105, 'PES', '0665555555', 105, 'FLSH', 7, 0, 0), -- Hassan Tahiri
(106, 'PH', '0666666666', 106, 'FLSH', 8, 0, 0), -- Karim Berrada
(107, 'PES', '0667777777', 107, 'FSJES', NULL, 0, 0), -- Driss Alaoui
(108, 'PA', '0668888888', 108, 'FSDM', 1, 0, 0), -- Noura Kadiri
(109, 'PES', '0669999999', 109, 'FST', 4, 0, 0), -- Ahmed Fassi
(110, 'PH', '0660000000', 110, 'FSDM', 3, 0, 0); -- Meryem Zerouali

-- 7. SUJETS
-- Sujets Informatique (FSDM, LISIA)
INSERT INTO professeur_sujet (titre, description, publier, professeur_id, formation_doctorale_id) VALUES
('Deep Learning appliqué à la reconnaissance d''images médicales', 'Ce projet vise à développer des algorithmes pour...', true, 101, 1),
('Optimisation des réseaux de capteurs sans fil poyr l''agriculture intelligente', 'Etude des protocoles de communication...', true, 101, 1),
('Sécurité des données dans le Cloud Computing', 'Analyse des vulnérabilités et propositions...', true, 108, 1),
('Traitement automatique du langage naturel pour l''arabe dialectal', 'Construction de corpus et modèles...', true, 108, 1);

-- Sujets Maths (FSDM, LMA)
INSERT INTO professeur_sujet (titre, description, publier, professeur_id, formation_doctorale_id) VALUES
('Analyse harmonique sur les groupes de Lie', 'Etude approfondie des structures...', true, 102, 1),
('Modélisation stochastique des épidémies', 'Application aux maladies infectieuses...', true, 102, 1);

-- Sujets FST (LSIE - Info/Elec)
INSERT INTO professeur_sujet (titre, description, publier, professeur_id, formation_doctorale_id) VALUES
('Systèmes embarqués pour le suivi de la qualité de l''air', 'Conception de capteurs intelligents...', true, 103, 2),
('Contrôle commande des éoliennes offshore', 'Optimisation de la production énergétique...', true, 103, 2),
('Big Data Analytics pour la maintenance prédictive', 'Industrie 4.0 et maintenance...', true, 109, 1);

-- Sujets Bio (FST - LBPRN)
INSERT INTO professeur_sujet (titre, description, publier, professeur_id, formation_doctorale_id) VALUES
('Valorisation des plantes aromatiques et médicinales du Moyen Atlas', 'Extraction d''huiles essentielles...', true, 109, 4),
('Etude de la biodiversité dans les zones humides', 'Impact du changement climatique...', true, 109, 4);

-- Sujets SHS (FLSH)
INSERT INTO professeur_sujet (titre, description, publier, professeur_id, formation_doctorale_id) VALUES
('Le patrimoine architectural de la médina de Fès', 'Conservation et restauration...', true, 105, 5),
('Digital Humanities et historiographie marocaine', 'Nouvelles méthodes de recherche...', true, 106, 5);

-- More fake data to fill volume
INSERT INTO professeur_sujet (titre, description, publier, professeur_id, formation_doctorale_id) VALUES
('Sujet Test 1', 'Description long text...', true, 101, 1),
('Sujet Test 2', 'Description long text...', true, 101, 1),
('Sujet Test 3', 'Description long text...', true, 101, 1),
('Sujet Test 4', 'Description long text...', true, 102, 1),
('Sujet Test 5', 'Description long text...', true, 103, 2),
('Sujet Test 6', 'Description long text...', true, 105, 5),
('Sujet Test 7', 'Description long text...', true, 106, 5),
('Sujet Test 8', 'Description long text...', true, 107, 8),
('Sujet Test 9', 'Description long text...', true, 107, 8),
('Sujet Test 10', 'Description long text...', true, 110, 2);
