# Système de Gestion d'un Restaurant — Java MVC + DAO

## Structure du projet

```
restaurant/
└── src/
    ├── Main.java
    ├── util/
    │   └── DatabaseConnection.java      ← Singleton connexion MySQL
    ├── model/
    │   ├── TypeUtilisateur.java         ← Enum: CLIENT, SERVEUR, CUISINIER
    │   ├── EtatCommande.java            ← Enum: EN_ATTENTE, EN_COURS, PRETE, SERVIE, ANNULEE
    │   ├── Utilisateur.java
    │   ├── Menu.java
    │   ├── Plats.java
    │   ├── Commande.java
    │   ├── LigneCommande.java
    │   └── Facture.java
    ├── dao/
    │   ├── IUtilisateurDAO.java         ← Interface DAO
    │   ├── UtilisateurDAO.java
    │   ├── MenuDAO.java
    │   ├── PlatsDAO.java
    │   ├── CommandeDAO.java
    │   ├── LigneCommandeDAO.java
    │   └── FactureDAO.java
    ├── controller/
    │   ├── AuthController.java
    │   ├── MenuController.java
    │   └── CommandeController.java
    └── view/
        ├── LoginView.java
        ├── ClientView.java
        ├── ServeurView.java
        ├── CuisinierView.java
        ├── CommanderView.java           ← Partagée par Client et Serveur
        └── GestionMenuView.java         ← Gestion menus/plats pour Cuisinier
```

## Prérequis

- Java JDK 11+
- MySQL Server
- mysql-connector-j-8.x.jar (driver JDBC)

## Configuration

1. Importez `create_restaurant_mysql.sql` dans MySQL Workbench.
2. Ouvrez `src/util/DatabaseConnection.java` et modifiez si nécessaire :
   - `URL`      → votre URL MySQL (par défaut `localhost:3306/restaurant`)
   - `USER`     → votre utilisateur MySQL
   - `PASSWORD` → votre mot de passe

## Compilation & Exécution

```bash
# Compiler (remplacez le chemin du driver selon votre installation)
javac -cp ".;mysql-connector-j-8.x.jar" -sourcepath src -d out src/Main.java

# Exécuter
java -cp ".;out;mysql-connector-j-8.x.jar" Main
```

## Comptes de test (données d'exemple)

| Username | Password   | Rôle      |
|----------|------------|-----------|
| alice    | hashed_pw_1| Client    |
| bob      | hashed_pw_2| Serveur   |
| clara    | hashed_pw_3| Cuisinier |
| david    | hashed_pw_4| Client    |

> ⚠️ Les mots de passe dans la démo sont en clair. En production, utilisez BCrypt ou SHA-256.

## Architecture MVC + DAO

```
Vue (Swing)  ←→  Contrôleur  ←→  DAO  ←→  MySQL
   view/         controller/     dao/      (JDBC)
                    ↕
                 Modèle
                 model/
```
