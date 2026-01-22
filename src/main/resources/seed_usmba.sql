-- ================================================================
-- SEED DATA FOR USMBA (FÈS) - E-DOCTORAT
-- ================================================================

-- 1. ETABLISSEMENT
INSERT INTO professeur_etablissement (id_etablissement, nom_etablissement) VALUES
('FSDM', 'Faculté des Sciences Dhar El Mahraz'),
('FST', 'Faculté des Sciences et Techniques'),
('FLSH', 'Faculté des Lettres et des Sciences Humaines'),
('FSJES', 'Faculté des Sciences Juridiques, Economiques et Sociales'),
('ENSA', 'Ecole Nationale des Sciences Appliquées'),
('EST', 'Ecole Supérieure de Technologie')
ON CONFLICT (id_etablissement) DO NOTHING;

-- 2. CED
INSERT INTO professeur_ced (id, titre, description) VALUES
(1, 'Sciences et Techniques et Sciences Médicales', 'CED couvrant les sciences exactes, ingénierie et médecine'),
(2, 'Lettres, Sciences Humaines et Arts', 'CED pour les humanités et sciences sociales'),
(3, 'Droit, Economie et Gestion', 'CED pour les sciences juridiques et économiques')
ON CONFLICT (id) DO NOTHING;

-- 3. USERS & ROLES
INSERT INTO sec_user_account (id, email, password, full_name, enabled, provider, active_role, verification_token, reset_password_token, reset_password_token_expiry) VALUES
(1, 'ayoub@gmail.com', '$2a$10$xhSeZkNI7/YGrPi5/G1Kx.h9tTqFWHtuzkCme5Pv8OqMpXZQ1ljxe', 'ayoub amad', true, 'LOCAL', 'CANDIDAT', 'f14125d1-f808-46a1-a57b-2792327009c1', '6a670787-b642-46b1-8693-a0dc1ef43575', '2026-01-21 18:30:40.848093'),
(2, 'ahmadayoube123@gmail.com', '$2a$10$NN4cxg1/MsQVFtvkuuoYOOuafQf9.HhtMrkLVgMFqsloZ6EYgpnuq', 'amad ayoub', true, 'LOCAL', 'DIRECTEUR_LABO', NULL, NULL, NULL),
(3, 'akinkoafu@gmail.com', '$2a$10$N.z.j.d.x.y.z.HASH', 'John Doe', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL),
(101, 'prof1@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Mohammed El Amrani', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL),
(102, 'prof2@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Samira Benkirane', true, 'LOCAL', 'DIRECTEUR_LABO', NULL, NULL, NULL),
(103, 'prof3@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Youssef Chami', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL),
(104, 'prof4@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Fatima Ouazzani', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL),
(105, 'prof5@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Hassan Tahiri', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL),
(106, 'prof6@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Karim Berrada', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL),
(107, 'prof7@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Driss Alaoui', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL),
(108, 'prof8@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Noura Kadiri', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL),
(109, 'prof9@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Ahmed Fassi', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL),
(110, 'prof10@usmba.ac.ma', '$2a$10$N.z.j.d.x.y.z.HASH', 'Meryem Zerouali', true, 'LOCAL', 'PROFESSEUR', NULL, NULL, NULL)
ON CONFLICT (id) DO UPDATE SET email = EXCLUDED.email, password = EXCLUDED.password, active_role = EXCLUDED.active_role;

-- Roles
INSERT INTO sec_user_roles (user_id, role) VALUES 
(1, 'CANDIDAT'), (2, 'DIRECTEUR_LABO'), (3, 'PROFESSEUR'),
(101, 'PROFESSEUR'), (102, 'DIRECTEUR_LABO'), (103, 'PROFESSEUR'), (104, 'PROFESSEUR'),
(105, 'PROFESSEUR'), (106, 'PROFESSEUR'), (107, 'PROFESSEUR'), (108, 'PROFESSEUR'),
(109, 'PROFESSEUR'), (110, 'PROFESSEUR')
ON CONFLICT DO NOTHING;

-- 4. LABORATOIRES
INSERT INTO professeur_laboratoire (id, nom_laboratoire, description, initial, etablissement_id, ced_id, directeur_id) VALUES
(22, 'Laboratoire Informatique, Systèmes et Intelligence Artificielle', 'Recherche en IA et Systèmes', 'LISIA', 'FSDM', 1, 2),
(23, 'Laboratoire de Mathématiques et Applications', 'Analyse, Algèbre et Géométrie', 'LMA', 'FSDM', 1, 102),
(24, 'Laboratoire de Physique des Matériaux et Microélectronique', 'Physique du solide', 'LPMM', 'FSDM', 1, 110),
(28, 'Laboratoire de Systèmes Intelligents et Energie', 'Energy management and AI', 'LSIE', 'FST', 1, 103),
(29, 'Laboratoire de Biotechnologie et Préservation des Ressources Naturelles', 'BioTech', 'LBPRN', 'FST', 1, 104),
(30, 'Laboratoire de Modélisation et Calcul Scientifique', 'Maths appliqués', 'LMCS', 'FST', 1, 109),
(33, 'Laboratoire de Recherche sur les Langues et la Communication', 'Linguistique', 'LRLC', 'FLSH', 2, 105),
(34, 'Laboratoire Histoire et Patrimoine', 'Histoire du Maroc', 'LHP', 'FLSH', 2, 106)
ON CONFLICT (id) DO UPDATE SET nom_laboratoire = EXCLUDED.nom_laboratoire, directeur_id = EXCLUDED.directeur_id;

-- 5. PROFESSEURS DETAILS
INSERT INTO professeur (id, grade, nombre_encadrer, nombre_proposer, tel_professeur, user_id, etablissement_id, labo_id, nom, prenom) VALUES
(2, 'PES', 0, 0, '0661234567', 2, 'FSDM', 22, 'amad', 'ayoub'),
(101, 'PES', 0, 0, '0661111111', 101, 'FSDM', 22, 'El Amrani', 'Mohammed'),
(102, 'PH', 0, 0, '0662222222', 102, 'FSDM', 23, 'Benkirane', 'Samira'),
(103, 'PES', 0, 0, '0663333333', 103, 'FST', 28, 'Chami', 'Youssef'),
(104, 'PA', 0, 0, '0664444444', 104, 'FST', 29, 'Ouazzani', 'Fatima'),
(105, 'PES', 0, 0, '0665555555', 105, 'FLSH', 33, 'Tahiri', 'Hassan'),
(106, 'PH', 0, 0, '0666666666', 106, 'FLSH', 34, 'Berrada', 'Karim'),
(107, 'PES', 0, 0, '0667777777', 107, 'FLSH', 33, 'Alaoui', 'Driss'),
(108, 'PA', 0, 0, '0668888888', 108, 'FSDM', 22, 'Kadiri', 'Noura'),
(109, 'PES', 0, 0, '0669999999', 109, 'FST', 28, 'Fassi', 'Ahmed'),
(110, 'PH', 0, 0, '0660000000', 110, 'FSDM', 24, 'Zerouali', 'Meryem')
ON CONFLICT (id) DO UPDATE SET grade = EXCLUDED.grade, labo_id = EXCLUDED.labo_id;

-- 6. FORMATION DOCTORALE
INSERT INTO professeur_formationdoctorale (id, titre, initiale, ced_id, etablissement_id) VALUES
(9, 'Mathématiques et Informatique', 'MATH-INFO', 1, 'FSDM'),
(10, 'Physique et Sciences de l''Ingénieur', 'PHY-ING', 1, 'FST'),
(11, 'Chimie et Environnement', 'CHIM-ENV', 1, 'FST'),
(12, 'Biologie et Santé', 'BIO-SANTE', 1, 'FSDM'),
(13, 'Langues, Littératures et Communication', 'LANG-LIT', 2, 'FLSH'),
(14, 'Sociologie et Psychologie', 'SOC-PSY', 2, 'FLSH'),
(15, 'Droit Privé et Sciences Criminelles', 'DROIT-PRIVE', 3, 'FSJES'),
(16, 'Economie et Gestion des Organisations', 'ECO-GEST', 3, 'FSJES')
ON CONFLICT (id) DO NOTHING;

-- 7. SUJETS DE RECHERCHE
INSERT INTO professeur_sujet (titre, description, publier, professeur_id, formation_doctorale_id) VALUES
('Intelligence Artificielle pour le Diagnostic Médical', 'Modèles de Deep Learning pour l''imagerie médicale.', true, 2, 9),
('Blockchain et Sécurité des Données Cloud', 'Décentralisation de la sécurité en environnements distribués.', true, 101, 9),
('Algorithmes d''Optimisation pour la Logistique Urbaine', 'Modélisation mathématique du transport en ville.', true, 102, 9),
('Systèmes de Gestion d''Énergie pour Smart Cities', 'Contrôle intelligent de la consommation connectée.', true, 103, 10),
('Valorisation des Bio-ressources pour la Cosmétique', 'Extraction de principes actifs de la flore locale.', true, 104, 11),
('Analyse Socio-linguistique des réseaux sociaux au Maroc', 'Étude des nouveaux codes de communication chez les jeunes.', true, 105, 13),
('Patrimoine et Tourisme Culturel à Fès', 'Préservation des monuments et impact économique.', true, 106, 13),
('Modélisation Stochastique des Marchés Financiers', 'Gestion de portefeuille via processus de Lévy.', true, 102, 9),
('Traitement du Signal pour les Radars Mobiles', 'Techniques avancées de filtrage et détection d''obstacles.', true, 109, 10),
('Synthèse de Nanomatériaux pour le Photovoltaïque', 'Élaboration de couches minces à haut rendement.', true, 110, 10),
('Les manuscrits de la Quaraouiyine : Étude Historique', 'Analyse codicologique des fonds anciens.', true, 106, 13),
('Big Data Analytics pour l''Industrie 4.0', 'Maintenance prédictive et optimisation industrielle.', true, 101, 9);
