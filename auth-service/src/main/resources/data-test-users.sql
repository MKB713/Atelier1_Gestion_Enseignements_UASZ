-- ========================================
-- Fichier SQL - Utilisateurs de test
-- Base de données: daos_auth_db
-- Mot de passe pour tous les comptes: password123
-- ========================================

-- Suppression des données existantes (optionnel, décommenter si nécessaire)
-- DELETE FROM utilisateur;

-- ========================================
-- 1. ADMIN - Accès complet à tout
-- ========================================
INSERT INTO utilisateur (nom, prenom, telephone, email, password, role, etat)
VALUES (
    'Admin',
    'Système',
    '221771234567',
    'admin@uasz.sn',
    '$2a$10$FlySioELwHXuiZz9/xfn5uE4Cw3cK57zgVIN5yUptHridm/SZgQv2',  -- password123
    'ADMIN',
    'ACTIF'
);

-- ========================================
-- 2. CHEF DE DÉPARTEMENT - Accès administratif
-- ========================================
INSERT INTO utilisateur (nom, prenom, telephone, email, password, role, etat)
VALUES (
    'Diallo',
    'Mamadou',
    '221771234568',
    'chef.departement@uasz.sn',
    '$2a$10$FlySioELwHXuiZz9/xfn5uE4Cw3cK57zgVIN5yUptHridm/SZgQv2',  -- password123
    'CHEF_DE_DEPARTEMENT',
    'ACTIF'
);

-- ========================================
-- 3. COORDINATEUR DES LICENCES - Gestion complète des licences
-- ========================================
INSERT INTO utilisateur (nom, prenom, telephone, email, password, role, etat)
VALUES (
    'Sarr',
    'Fatou',
    '221771234569',
    'coordinateur@uasz.sn',
    '$2a$10$FlySioELwHXuiZz9/xfn5uE4Cw3cK57zgVIN5yUptHridm/SZgQv2',  -- password123
    'COORDONATEUR_DES_LICENCES',
    'ACTIF'
);

-- ========================================
-- 4. RESPONSABLE MASTER - Gestion complète des masters
-- ========================================
INSERT INTO utilisateur (nom, prenom, telephone, email, password, role, etat)
VALUES (
    'Ndiaye',
    'Ousmane',
    '221771234570',
    'responsable.master@uasz.sn',
    '$2a$10$FlySioELwHXuiZz9/xfn5uE4Cw3cK57zgVIN5yUptHridm/SZgQv2',  -- password123
    'RESPONSABLE_MASTER',
    'ACTIF'
);

-- ========================================
-- 5. ÉTUDIANT - Consultation EDT uniquement
-- ========================================
INSERT INTO utilisateur (nom, prenom, telephone, email, password, role, etat)
VALUES (
    'Sow',
    'Aminata',
    '221771234571',
    'etudiant@uasz.sn',
    '$2a$10$FlySioELwHXuiZz9/xfn5uE4Cw3cK57zgVIN5yUptHridm/SZgQv2',  -- password123
    'ETUDIANT',
    'ACTIF'
);

-- ========================================
-- Utilisateurs supplémentaires pour tests
-- ========================================

-- Autre étudiant
INSERT INTO utilisateur (nom, prenom, telephone, email, password, role, etat)
VALUES (
    'Ba',
    'Ibrahima',
    '221771234572',
    'etudiant2@uasz.sn',
    '$2a$10$FlySioELwHXuiZz9/xfn5uE4Cw3cK57zgVIN5yUptHridm/SZgQv2',  -- password123
    'ETUDIANT',
    'ACTIF'
);

-- Compte inactif pour test
INSERT INTO utilisateur (nom, prenom, telephone, email, password, role, etat)
VALUES (
    'Cissé',
    'Marie',
    '221771234573',
    'inactif@uasz.sn',
    '$2a$10$FlySioELwHXuiZz9/xfn5uE4Cw3cK57zgVIN5yUptHridm/SZgQv2',  -- password123
    'ETUDIANT',
    'INACTIF'
);

-- ========================================
-- Vérification
-- ========================================
SELECT
    id,
    CONCAT(prenom, ' ', nom) AS nom_complet,
    email,
    role,
    etat
FROM utilisateur
ORDER BY
    FIELD(role, 'ADMIN', 'CHEF_DE_DEPARTEMENT', 'COORDONATEUR_DES_LICENCES', 'RESPONSABLE_MASTER', 'ETUDIANT');
