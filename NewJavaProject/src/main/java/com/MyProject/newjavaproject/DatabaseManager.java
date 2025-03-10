package com.MyProject.newjavaproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/person.db";
    private static final String SQL_SCRIPT = "src/main/resources/personDatabase.sql";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        File dbFile = new File("src/main/resources/person.db");

        if (!dbFile.exists()) {
            try (Connection conn = connect();
                 Statement stmt = conn.createStatement()) {

                // Charger et exécuter le script SQL
                String sql = new String(Files.readAllBytes(Paths.get(SQL_SCRIPT)));
                stmt.executeUpdate(sql);
                
                System.out.println("Base de données créée avec succès.");
                
                // Mise à jour de la base de données pour ajouter la colonne profile_picture
                updateDatabase(conn);

            } catch (SQLException | IOException e) {
                System.err.println("Erreur lors de la création de la base de données : " + e.getMessage());
            }
        } else {
            System.out.println("Base de données déjà existante. Vérification des mises à jour...");
            try (Connection conn = connect()) {
                updateDatabase(conn);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la connexion pour la mise à jour : " + e.getMessage());
            }
        }
    }

    public static void updateDatabase(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Vérifier si la colonne 'profile_picture' existe
            boolean columnExists = false;
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, "person", "profile_picture");

            if (rs.next()) {
                columnExists = true;
            }

            // Ajouter la colonne si elle n'existe pas
            if (!columnExists) {
                String alterTableSQL = "ALTER TABLE person ADD COLUMN profile_picture TEXT NULL;";
                stmt.executeUpdate(alterTableSQL);
                System.out.println("Colonne 'profile_picture' ajoutée avec succès !");
            } else {
                System.out.println("La colonne 'profile_picture' existe déjà.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la base de données : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}
