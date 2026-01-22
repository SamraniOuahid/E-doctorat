-- Migration: Increase role column size to accommodate DIRECTEUR_POLE
-- Run this before restarting the backend after adding DIRECTEUR_POLE role

ALTER TABLE sec_user_roles MODIFY COLUMN role VARCHAR(20) NOT NULL;
