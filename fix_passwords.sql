-- Script to fix passwords for all users (set to 'password123')
-- BCrypt hash for 'password123'
SET @pass = '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2';

-- Update all users
UPDATE sec_user_account SET password = @pass, enabled = true;

-- Show updated users
SELECT id, email, full_name, enabled FROM sec_user_account;
