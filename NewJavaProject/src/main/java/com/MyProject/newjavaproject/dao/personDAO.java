package com.MyProject.newjavaproject.dao;

import com.MyProject.newjavaproject.DatabaseManager;
import com.MyProject.newjavaproject.Person;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class personDAO {
    // Méthode pour lister toutes les personnes
    public static List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<>();
        String sql = "SELECT * FROM person";  // Requête SQL pour récupérer toutes les personnes

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Créer un objet Person pour chaque ligne dans le résultat
                Person person = new Person(
                    rs.getInt("idperson"),
                    rs.getString("lastname"),
                    rs.getString("firstname"),
                    rs.getString("nickname"),
                    rs.getString("phone_number"),
                    rs.getString("address"),
                    rs.getString("email_address"),
                    rs.getDate("birth_date"),
                    rs.getString("profile_picture")
                );
                persons.add(person);  // Ajouter la personne à la liste
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnes : " + e.getMessage());
        }
        return persons;
    }
    
    public void addPerson(Person person) {
        String sql = "INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date, profile_picture) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, person.getLastname());
            pstmt.setString(2, person.getFirstname());
            pstmt.setString(3, person.getNickname());
            pstmt.setString(4, person.getPhoneNumber());
            pstmt.setString(5, person.getAddress());
            pstmt.setString(6, person.getEmailAddress());
            pstmt.setDate(7, new java.sql.Date(person.getBirthDate().getTime()));
            pstmt.setString(8, person.getProfilePicture());

            pstmt.executeUpdate();
            System.out.println(" Personne ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println(" Erreur lors de l'ajout de la personne : " + e.getMessage());
        }
    }
    
    public void addNewPersonFromForm(String lastname, String firstname, String nickname, 
                                 String phone, String address, String email, java.util.Date birthDate, String profilePicture ) {
    Person newPerson = new Person(0, lastname, firstname, nickname, phone, address, email, birthDate, profilePicture);
    addPerson(newPerson);
}
    
    public void deletePerson(int id) {
    // SQL request
    String sql = "DELETE FROM person WHERE idperson = ?";

    // Database connexion and execution of the sql request
    try (Connection conn = DatabaseManager.connect(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) { 

        
        pstmt.setInt(1, id);

        // deletes and get the number of lignes affected
        int affectedRows = pstmt.executeUpdate();

        //CHeck wether person was delete or not
        if (affectedRows > 0) {
            System.out.println(" Personne supprimée avec succès !");
        } else {
            System.out.println("Aucun enregistrement trouvé avec cet ID.");
        }
    } catch (SQLException e) {
       
        System.err.println(" Erreur lors de la suppression de la personne : " + e.getMessage());
    }
}

    
    public boolean updatePerson(Person person) {
        // SQL request for updating all fields of a person.
        String sql = "UPDATE person SET lastname = ?, firstname = ?, nickname = ?, phone_number = ?, " +
                     "address = ?, email_address = ?, birth_date = ?, profile_picture = ? WHERE idperson = ?";

        try (Connection conn = DatabaseManager.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) { 

            // Remplissage des valeurs dans la requête SQL
            pstmt.setString(1, person.getLastname());
            pstmt.setString(2, person.getFirstname());
            pstmt.setString(3, person.getNickname());
            pstmt.setString(4, person.getPhoneNumber());
            pstmt.setString(5, person.getAddress());
            pstmt.setString(6, person.getEmailAddress());
            pstmt.setDate(7, new java.sql.Date(person.getBirthDate().getTime()));
            pstmt.setString(8, person.getProfilePicture()); 
            pstmt.setInt(9, person.getIdperson()); // ID of the person to update

            // Exécute la requête de mise à jour
            int affectedRows = pstmt.executeUpdate();

            // Vérifie si la mise à jour a été effectuée
            if (affectedRows > 0) {
                System.out.println("Personne mise à jour avec succès !");
                return true;
            } else {
                System.out.println("Aucun enregistrement trouvé avec cet ID.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la personne : " + e.getMessage());
            return false; // En cas d'échec
        }
    }
    
    public boolean updateProfilePicture(int id, String imagePath) {
        String sql = "UPDATE person SET profile_picture = ? WHERE idperson = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, imagePath);
            pstmt.setInt(2, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Image de profil mise à jour avec succès !");
                return true;
            } else {
                System.out.println("Aucun enregistrement trouvé avec cet ID.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'image de profil : " + e.getMessage());
            return false;
        }
    }
}



