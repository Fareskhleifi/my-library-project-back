-- Create the 'categorie' table
CREATE TABLE categorie (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create the 'livres' table
CREATE TABLE livres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    genre VARCHAR(100),
    description TEXT,
    categorie_id INT,
    disponibilite BOOLEAN DEFAULT TRUE,
    prix_par_jour DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (categorie_id) REFERENCES categorie(id) ON DELETE SET NULL
);

-- Create the 'utilisateurs' table
CREATE TABLE utilisateurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    adresse TEXT,
    role VARCHAR(20) NOT NULL,
    account_status VARCHAR(20) DEFAULT 'Active'
);

-- Create the 'transactions' table
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    livre_id INT NOT NULL,
    utilisateur_id INT NOT NULL,
    date_emprunt DATE NOT NULL,
    date_retour DATE,
    retourne BOOLEAN DEFAULT FALSE,
    prix_total DECIMAL(10, 2) NOT NULL,
    date_retour_reel DATE,
    FOREIGN KEY (livre_id) REFERENCES livres(id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

-- Create the 'commentaires' table
CREATE TABLE commentaires (
    id INT AUTO_INCREMENT PRIMARY KEY,
    livre_id INT NOT NULL,
    utilisateur_id INT,
    date_commentaire DATE NOT NULL,
    texte_commentaire TEXT,
    evaluation INT CHECK (evaluation BETWEEN 1 AND 5),
    FOREIGN KEY (livre_id) REFERENCES livres(id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

-- Create the 'notifications' table
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    destinateur_id INT NOT NULL,
    message TEXT,
    date_notification DATE NOT NULL,
    statut_lecture BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (destinateur_id) REFERENCES utilisateurs(id)
);

-- Create the 'Evenements' table
CREATE TABLE Evenements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    date_debut DATETIME NOT NULL,
    date_fin DATETIME NOT NULL,
    lieu VARCHAR(255),
    organisateur VARCHAR(255),
    statut ENUM('PROGRAMME', 'EN_COURS', 'TERMINE') DEFAULT 'PROGRAMME'
);
