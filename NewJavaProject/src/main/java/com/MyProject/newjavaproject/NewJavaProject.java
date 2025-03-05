/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.MyProject.newjavaproject;

import java.util.Date;
import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.Person;
import java.util.List;

/**
 *
 * @author milolad
 */
public class NewJavaProject {

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase(); 
        System.out.println("Base de données initialisée !");
        
         Person newPerson = new Person(
            0,
            "DIDI",
            "DODO",
            "Johnny",
            "0601020304",
            "123 rue de Paris",
            "john.doe@example.com",
            new Date() // Date actuelle comme exemple
        );

        // Ajout de la personne à la base de données
        personDAO personDAO = new personDAO();  // Instanciation du DAO
        personDAO.addPerson(newPerson);
        
        List<Person> persons = personDAO.getAllPersons();

        // Vérifier si la liste n'est pas vide
        if (persons.isEmpty()) {
            System.out.println("Aucune personne dans la base de données.");
        } else {
            // Afficher les informations de chaque personne
            for (Person person : persons) {
                System.out.println("ID: " + person.getIdperson() +
                        ", Nom: " + person.getLastname() +
                        ", Prénom: " + person.getFirstname() +
                        ", Surnom: " + person.getNickname());
            }
        }
    }
}
