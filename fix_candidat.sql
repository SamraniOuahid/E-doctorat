-- Fix missing candidat profile for existing user
-- Replace 'ouahidsamrani@gmail.com with your actual email

-- First, check what user_id we need
SELECT id, email, full_name FROM sec_user_account WHERE email = 'ouahidsamrani@gmail.com';

-- Then insert the candidat record (replace USER_ID with the id from above)
INSERT INTO candidat_candidat (email, password, user_id)
SELECT email, password, id 
FROM sec_user_account 
WHERE email = 'ouahidsamrani@gmail.com'
AND NOT EXISTS (
    SELECT 1 FROM candidat_candidat WHERE user_id = sec_user_account.id
);

-- Verify it worked
SELECT 
    u.id as user_id,
    u.email as user_email,
    c.id as candidat_id,
    c.user_id
FROM sec_user_account u
LEFT JOIN candidat_candidat c ON c.user_id = u.id
WHERE u.email = 'ouahidsamrani@gmail.com';
