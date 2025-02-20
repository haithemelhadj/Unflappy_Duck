-- 1. Gestion des utilisateurs ------------------------------------------------------------------------------------
CREATE TABLE utilisateur (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role ENUM('utilisateur', 'admin', 'organisateur') NOT NULL DEFAULT 'utilisateur',
    bio TEXT,
    photo_profil VARCHAR(255),
    xp INT DEFAULT 0,
    niveau INT UNSIGNED DEFAULT NULL, -- Retained to reference the level
    xp_requis INT UNSIGNED           -- Combined from niveau table
);



-- 2. Gestion des événements --------------------------------------------------------------------------------------
CREATE TABLE localisation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    adresse VARCHAR(255),
    ville VARCHAR(100),
    pays VARCHAR(100)
);

CREATE TABLE evenement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    date DATETIME NOT NULL,
    lieu VARCHAR(255),
    id_localisation INT,
    cree_par INT,
    FOREIGN KEY (id_localisation) REFERENCES localisation(id) ON DELETE SET NULL,
    FOREIGN KEY (cree_par) REFERENCES utilisateur(id) ON DELETE SET NULL
);

CREATE TABLE participant_evenement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    evenement_id INT,
    utilisateur_id INT,
    statut ENUM('en_attente', 'confirme', 'annule'),
    FOREIGN KEY (evenement_id) REFERENCES evenement(id) ON DELETE CASCADE,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- 3. Gestion des équipes -----------------------------------------------------------------------------------------
CREATE TABLE equipe (
    id INT PRIMARY KEY AUTO_INCREMENT,
    evenement_id INT,
    nom VARCHAR(255) NOT NULL,
    chef_equipe_id INT,
    FOREIGN KEY (evenement_id) REFERENCES evenement(id) ON DELETE CASCADE,
    FOREIGN KEY (chef_equipe_id) REFERENCES utilisateur(id) ON DELETE SET NULL
);

CREATE TABLE membre_equipe (
    id INT PRIMARY KEY AUTO_INCREMENT,
    equipe_id INT,
    utilisateur_id INT,
    role ENUM('chef', 'membre'),
    FOREIGN KEY (equipe_id) REFERENCES equipe(id) ON DELETE CASCADE,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE tache (
    id INT PRIMARY KEY AUTO_INCREMENT,
    equipe_id INT,
    titre VARCHAR(255) NOT NULL,
    description TEXT,
    id_assignateur INT,
    id_responsable INT,
    statut ENUM('en_attente', 'en_cours', 'termine'),
    FOREIGN KEY (equipe_id) REFERENCES equipe(id) ON DELETE CASCADE,
    FOREIGN KEY (id_assignateur) REFERENCES utilisateur(id) ON DELETE SET NULL,
    FOREIGN KEY (id_responsable) REFERENCES utilisateur(id) ON DELETE SET NULL
);

-- 4. Calendrier --------------------------------------------------------------------------------------------------
CREATE TABLE calendrier (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(255) NOT NULL,
    type ENUM('evenement', 'tache'),
    date DATETIME,
    evenement_id INT DEFAULT NULL,
    tache_id INT DEFAULT NULL,
    FOREIGN KEY (evenement_id) REFERENCES evenement(id) ON DELETE CASCADE,
    FOREIGN KEY (tache_id) REFERENCES tache(id) ON DELETE CASCADE
);

-- 5. Gestion des Q/R ---------------------------------------------------------------------------------------------
CREATE TABLE `reponses` (
  `id` int(11) NOT NULL,
  `utilisateur_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `reponse` varchar(11) NOT NULL,
  `reponse_correcte` varchar(1) NOT NULL
);


CREATE TABLE `reponse` (
  `id_reponse` int(11) NOT NULL,
  `date_reponse` varchar(20) NOT NULL,
  `description_reponse` varchar(200) NOT NULL,
  `id_reclamation` int(11) NOT NULL
);


CREATE TABLE `questions` (
  `id_question` int(11) NOT NULL,
  `numero_question` int(11) NOT NULL,
  `question` text NOT NULL,
  `reponse_1` text NOT NULL,
  `reponse_2` text NOT NULL,
  `reponse_3` text NOT NULL,
  `reponse_4` text NOT NULL
);
-- 6. Gestion de la boutique --------------------------------------------------------------------------------------
CREATE TABLE article_boutique (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    type ENUM('article', 'ticket'),
    prix DECIMAL(10,2) NOT NULL
);

CREATE TABLE panier (
    id INT PRIMARY KEY AUTO_INCREMENT,
    utilisateur_id INT NOT NULL,
    article_id INT NOT NULL,
    quantite INT UNSIGNED NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES article_boutique(id) ON DELETE CASCADE
);

-- 7. Gestion des freelances --------------------------------------------------------------------------------------
CREATE TABLE service (
    service_id INT PRIMARY KEY AUTO_INCREMENT,
    freelance_id INT NOT NULL,
    titre VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    expertise VARCHAR(50) NOT NULL,
    duree_jours SMALLINT UNSIGNED NOT NULL,
    prix DECIMAL(10,2) UNSIGNED NOT NULL,
    mode_paiement ENUM('horaire', 'forfait', 'milestone') NOT NULL,
    cree_le TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mis_a_jour_le TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (freelance_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE offre (
    offre_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    titre VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    budget DECIMAL(12,2) UNSIGNED NOT NULL,
    type_contrat ENUM('forfait', 'horaire', 'abonnement') NOT NULL,
    statut ENUM('ouvert', 'ferme', 'en_cours', 'expire') NOT NULL DEFAULT 'ouvert',
    cree_le TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expire_le DATE NOT NULL,
    FOREIGN KEY (client_id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE contrat (
    contrat_id INT PRIMARY KEY AUTO_INCREMENT,
    service_id INT,
    offre_id INT,
    date_debut DATE NOT NULL,
    date_fin DATE,
    description LONGTEXT NOT NULL,
    statut ENUM('brouillon', 'actif', 'termine', 'resilie', 'dispute') NOT NULL DEFAULT 'brouillon',
    echeancier_paiement LONGTEXT NOT NULL,
    cree_le TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mis_a_jour_le TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (service_id) REFERENCES service(service_id) ON DELETE SET NULL,
    FOREIGN KEY (offre_id) REFERENCES offre(offre_id) ON DELETE SET NULL
);