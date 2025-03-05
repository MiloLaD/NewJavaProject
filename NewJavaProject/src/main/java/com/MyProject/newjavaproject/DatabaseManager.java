/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.MyProject.newjavaproject;

/**
 *
 * @author milolad
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
        
        // Vérifie si la base de données existe déjà
        if (!dbFile.exists()) {
            try (Connection conn = connect();
                 Statement stmt = conn.createStatement()) {

                // Exécuter le script SQL pour créer les tables
                String sql = new String(Files.readAllBytes(Paths.get(SQL_SCRIPT)));
                
                stmt.executeUpdate(sql);

                System.out.println("✅ Base de données créée avec succès." );
            } catch (SQLException | IOException e) {
                System.err.println("❌ Erreur lors de la création de la base de données : " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Base de données déjà existante.");
        }
    }
}
