# NewJavaProject Allan IYEDI Bourdette, Chensu-babu Nandimandala, Emile Fourtané

## Description

NewJavaProject est une application Java développée avec JavaFX, permettant la gestion des personnes à l'aide d'une base de données SQLite. L'application inclut plusieurs contrôleurs pour ajouter, modifier et afficher des informations sur les personnes.

## Fonctionnalités

- **Ajout de Personnes**: Permet d'ajouter de nouvelles personnes à la base de données.
- **Modification de Personnes**: Permet de mettre à jour les informations des personnes existantes.
- **Affichage des Personnes**: Affiche la liste de toutes les personnes enregistrées dans la base de données.
- **Gestion des Images de Profil**: Permet de mettre à jour l'image de profil des personnes.

## Architecture

L'application est structurée en plusieurs composants :

- **Contrôleurs**:
  - `AddViewController`: Gère l'ajout de nouvelles personnes.
  - `ModifyController`: Gère la modification des informations d'une personne existante.
  - `HomeViewController`: Affiche la liste des personnes.

- **DAO (Data Access Object)**:
  - `PersonDAO`: Gère les opérations de base de données pour les entités `Person`.

- **Modèle**:
  - `Person`: Représente un individu avec ses attributs (nom, prénom, etc.).

- **Base de Données**:
  - Utilise SQLite pour stocker les informations des personnes.

- **Interface Utilisateur**:
  - Basée sur JavaFX, avec des fichiers FXML pour la mise en page et des fichiers CSS pour le style.

## Dépendances

Le projet utilise les dépendances suivantes, gérées par Maven :

- **JavaFX**: Framework pour créer des interfaces utilisateur.
- **SQLite JDBC**: Pour la connexion à la base de données SQLite.
- **JUnit 5**: Pour les tests unitaires.
- **Mockito**: Pour les tests avec simulation.

git clone https://github.com/MiloLaD/NewJavaProject.git

cd NewJavaproject

Puis run à l'aide de votre environment.
