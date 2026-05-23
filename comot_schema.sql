-- ============================================================
--  APPLICATION COMOT - Schéma MySQL
--  Généré à partir du diagramme métier
-- ============================================================

CREATE DATABASE IF NOT EXISTS comot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE comot;

-- ============================================================
-- 1. UTILISATEUR
--    Règle : deux utilisateurs ne peuvent pas avoir le même email
--    Règle : un utilisateur peut quitter une organisation
-- ============================================================
CREATE TABLE utilisateur (
    id            INT           NOT NULL AUTO_INCREMENT,
    nom           VARCHAR(100)  NOT NULL,
    prenom        VARCHAR(100)  NOT NULL,
    email         VARCHAR(255)  NOT NULL,
    mot_de_passe  VARCHAR(255)  NOT NULL,
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_utilisateur_email (email)
) ENGINE=InnoDB;

-- ============================================================
-- 2. ORGANISATION
--    Règle : deux organisations ne peuvent pas avoir le même nom
--    Règle : une organisation a obligatoirement un admin
--    Règle : la création de l'org entraîne la création des rôles dans l'org
-- ============================================================
CREATE TABLE organisation (
    id          INT          NOT NULL AUTO_INCREMENT,
    nom         VARCHAR(255) NOT NULL,
    description TEXT,
    admin_id    INT          NOT NULL COMMENT 'Obligatoire - FK vers utilisateur',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_organisation_nom (nom),
    CONSTRAINT fk_org_admin FOREIGN KEY (admin_id) REFERENCES utilisateur(id)
) ENGINE=InnoDB;

-- ============================================================
-- 3. ROLE
--    Règle : seul un admin peut créer et modifier des rôles
--    Créés automatiquement à la création de l'organisation
-- ============================================================
CREATE TABLE role (
    id              INT          NOT NULL AUTO_INCREMENT,
    nom             VARCHAR(100) NOT NULL,
    organisation_id INT          NOT NULL,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_role_org FOREIGN KEY (organisation_id) REFERENCES organisation(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 4. PERMISSION
--    Valeurs possibles : CREER_EVENT, VOIR_VENTES,
--                        VALIDER_TICKET, VENDEUR_PLACE
-- ============================================================
CREATE TABLE permission (
    id          INT          NOT NULL AUTO_INCREMENT,
    nom         VARCHAR(100) NOT NULL,
    description TEXT,
    PRIMARY KEY (id),
    UNIQUE KEY uq_permission_nom (nom)
) ENGINE=InnoDB;

-- Données initiales
INSERT INTO permission (nom, description) VALUES
('CREER_EVENT',    'Peut créer des événements'),
('VOIR_VENTES',    'Peut consulter les ventes'),
('VALIDER_TICKET', 'Peut valider des tickets à l'entrée'),
('VENDEUR_PLACE',  'Peut vendre des places sur place');

-- ============================================================
-- 5. ORG_MEMBRE  (Organisation ↔ Utilisateur)
--    Règle : on ne retire qu'un membre qui est déjà là
--    Règle : un utilisateur peut quitter une organisation
-- ============================================================
CREATE TABLE org_membre (
    id              INT      NOT NULL AUTO_INCREMENT,
    organisation_id INT      NOT NULL,
    utilisateur_id  INT      NOT NULL,
    joined_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_org_membre (organisation_id, utilisateur_id),
    CONSTRAINT fk_om_org  FOREIGN KEY (organisation_id) REFERENCES organisation(id) ON DELETE CASCADE,
    CONSTRAINT fk_om_user FOREIGN KEY (utilisateur_id)  REFERENCES utilisateur(id)  ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 6. ROLE_PERMISSION  (Rôle ↔ Permission)
--    Règle : une permission n'est disponible qu'une seule fois dans un rôle
-- ============================================================
CREATE TABLE role_permission (
    id            INT NOT NULL AUTO_INCREMENT,
    role_id       INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_role_permission (role_id, permission_id),
    CONSTRAINT fk_rp_role FOREIGN KEY (role_id)       REFERENCES role(id)       ON DELETE CASCADE,
    CONSTRAINT fk_rp_perm FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 7. UTILISATEUR_ROLE  (Utilisateur ↔ Rôle dans une org)
--    Attribuer / retirer un rôle à un membre
-- ============================================================
CREATE TABLE utilisateur_role (
    id             INT      NOT NULL AUTO_INCREMENT,
    utilisateur_id INT      NOT NULL,
    role_id        INT      NOT NULL,
    assigned_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_user_role (utilisateur_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id)        REFERENCES role(id)        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 8. EVENT
--    Règle : contient au moins une catégorie de ticket
--    Règle : peut avoir un quota total (optionnel)
--    Règle : peut avoir 0, 1 ou plusieurs schedules
-- ============================================================
CREATE TABLE event (
    id              INT          NOT NULL AUTO_INCREMENT,
    titre           VARCHAR(255) NOT NULL,
    description     TEXT,
    organisation_id INT          NOT NULL,
    quota_total     INT          NULL COMMENT 'Optionnel',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_event_org FOREIGN KEY (organisation_id) REFERENCES organisation(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 9. SCHEDULE
--    Règle : un schedule appartient à un seul event
--    Règle : doit proposer au moins une catégorie de ticket
-- ============================================================
CREATE TABLE schedule (
    id          INT          NOT NULL AUTO_INCREMENT,
    event_id    INT          NOT NULL COMMENT 'Un schedule appartient à un seul event',
    date_debut  DATETIME     NOT NULL,
    date_fin    DATETIME     NOT NULL,
    lieu        VARCHAR(255),
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_schedule_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 10. TICKET  (catégorie de ticket par event)
--     Catégories : STANDARD, STUDENT, VIP
--     Règle : un event contient au moins une catégorie de ticket
-- ============================================================
CREATE TABLE ticket (
    id             INT            NOT NULL AUTO_INCREMENT,
    event_id       INT            NOT NULL,
    categorie      ENUM('STANDARD','STUDENT','VIP') NOT NULL,
    prix           DECIMAL(10,2)  NOT NULL,
    quantite_dispo INT            NOT NULL,
    created_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_ticket_event_cat (event_id, categorie),
    CONSTRAINT fk_ticket_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 11. TRANSACTION
--     Statuts : PENDING, ERROR, SUCCESS
--     Règle : la transaction se fait sur un schedule OU un event directement
-- ============================================================
CREATE TABLE transaction (
    id             INT           NOT NULL AUTO_INCREMENT,
    utilisateur_id INT           NOT NULL,
    ticket_id      INT           NOT NULL,
    schedule_id    INT           NULL COMMENT 'Nullable - sur schedule OU event',
    event_id       INT           NULL COMMENT 'Nullable - sur schedule OU event',
    statut         ENUM('PENDING','ERROR','SUCCESS') NOT NULL DEFAULT 'PENDING',
    montant        DECIMAL(10,2) NOT NULL,
    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_tx_user     FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id),
    CONSTRAINT fk_tx_ticket   FOREIGN KEY (ticket_id)      REFERENCES ticket(id),
    CONSTRAINT fk_tx_schedule FOREIGN KEY (schedule_id)    REFERENCES schedule(id),
    CONSTRAINT fk_tx_event    FOREIGN KEY (event_id)       REFERENCES event(id),
    -- Règle : schedule_id OU event_id doit être renseigné
    CONSTRAINT chk_tx_target CHECK (
        (schedule_id IS NOT NULL AND event_id IS NULL) OR
        (schedule_id IS NULL     AND event_id IS NOT NULL)
    )
) ENGINE=InnoDB;

-- ============================================================
-- 12. SALE
--     Règle : obtenue à la suite d'une transaction réussie (SUCCESS)
-- ============================================================
CREATE TABLE sale (
    id             INT           NOT NULL AUTO_INCREMENT,
    transaction_id INT           NOT NULL COMMENT 'Transaction avec statut SUCCESS',
    utilisateur_id INT           NOT NULL,
    ticket_id      INT           NOT NULL,
    montant_final  DECIMAL(10,2) NOT NULL,
    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_sale_transaction (transaction_id),
    CONSTRAINT fk_sale_tx     FOREIGN KEY (transaction_id) REFERENCES transaction(id),
    CONSTRAINT fk_sale_user   FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id),
    CONSTRAINT fk_sale_ticket FOREIGN KEY (ticket_id)      REFERENCES ticket(id)
) ENGINE=InnoDB;

-- ============================================================
-- FIN DU SCHÉMA COMOT
-- ============================================================
