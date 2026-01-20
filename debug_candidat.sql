-- Check if your user has an associated candidat
-- Run this in your MySQL database

SELECT 
    u.id as user_id,
    u.email as user_email,
    u.enabled as user_enabled,
    c.id as candidat_id,
    c.email as candidat_email,
    c.user_id as candidat_user_link
FROM sec_user_account u
LEFT JOIN candidat_candidat c ON c.user_id = u.id
WHERE u.email = 'YOUR_EMAIL_HERE'
LIMIT 5;

-- If candidat_id is NULL, it means the candidat wasn't created during registration
-- You may need to register again or manually insert the candidat record
NULL