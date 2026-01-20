-- Script pour créer un utilisateur admin
-- Remplacez 'votre.email@example.com' par votre email réel

-- 1. Créer l'utilisateur admin (si pas encore créé)
-- Le mot de passe est 'admin123' (hashé avec BCrypt)
INSERT INTO sec_user_account (email, password, full_name, enabled, verification_token)
VALUES ('admin@edoctorat.ma', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIrsON2eCGJqLYqKqYqKqYqKqYqKqYqK', 'Administrateur', true, null)
ON DUPLICATE KEY UPDATE enabled = true;

-- 2. Récupérer l'ID de l'utilisateur
SET @admin_user_id = (SELECT id FROM sec_user_account WHERE email = 'admin@edoctorat.ma');

-- 3. Ajouter le rôle ADMIN
INSERT INTO user_roles (user_id, roles)
VALUES (@admin_user_id, 'ADMIN')
ON DUPLICATE KEY UPDATE roles = 'ADMIN';

-- 4. Vérifier que ça a fonctionné
SELECT u.id, u.email, u.full_name, u.enabled, r.roles
FROM sec_user_account u
LEFT JOIN user_roles r ON u.id = r.user_id
WHERE u.email = 'admin@edoctorat.ma';

-- OU pour utiliser votre email existant:
-- UPDATE user_roles SET roles = 'ADMIN' WHERE user_id = (SELECT id FROM sec_user_account WHERE email = 'ouahidsamrani@gmail.com');
