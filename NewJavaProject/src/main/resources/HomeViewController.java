/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package com.MyProject.controllers;

import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.Person;
import com.gluonhq.charm.glisten.control.Icon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HomeViewController {

    @FXML
    private ListView<String> ListContact;  // La ListView qui affiche les contacts

    @FXML
    private TextField searchContact; // Champ de recherche

    @FXML
    private Button goAddContact, ModifyContact, DeletContact; // Boutons d'actions

    private personDAO personDAO = new personDAO();  // DAO pour gérer les contacts
    private ObservableList<String> personNames = FXCollections.observableArrayList(); // Liste observable
    @FXML
    private HBox NickName;
    @FXML
    private HBox Numer;
    @FXML
    private HBox NickName1;
    @FXML
    private HBox FirstNamedetails;
    @FXML
    private ImageView imageHome;
    @FXML
    private Icon searchBtnHome;

    public void initialize() {
        loadContacts(); // Charge les contacts à l'initialisation
        setupSearchFunction(); // Active la recherche dynamique
    }

    private void loadContacts() {
        List<Person> persons = personDAO.getAllPersons(); // Récupère les contacts de la BDD
        personNames.clear();

        // Convertit les objets Person en texte à afficher
        for (Person person : persons) {
            personNames.add(person.getFirstname() + " " + person.getLastname());
        }

        ListContact.setItems(personNames); // Remplit la ListView
    }

    private void setupSearchFunction() {
        searchContact.textProperty().addListener((observable, oldValue, newValue) -> {
            List<String> filteredList = personDAO.getAllPersons().stream()
                    .map(person -> person.getFirstname() + " " + person.getLastname())
                    .filter(name -> name.toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());

            ListContact.setItems(FXCollections.observableArrayList(filteredList));
        });
    }

    @FXML
    private void gotoAddContactView(ActionEvent event) {
        System.out.println("Ajout d'un contact - Redirection");
        // Ici, vous pouvez ajouter le code pour ouvrir une autre fenêtre d'ajout de contact
    }

    @FXML
    private void ModifyContact(ActionEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            System.out.println("Modification du contact : " + selectedContact);
            // Implémentez la modification en récupérant l'objet correspondant
        } else {
            System.out.println("Aucun contact sélectionné !");
        }
    }

    @FXML
    private void DeletContact(ActionEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            // Récupération de l'objet Person correspondant
            List<Person> persons = personDAO.getAllPersons();
            Person personToDelete = persons.stream()
                    .filter(p -> (p.getFirstname() + " " + p.getLastname()).equals(selectedContact))
                    .findFirst().orElse(null);

            if (personToDelete != null) {
                personDAO.deletePerson(personToDelete.getIdperson()); // Suppression dans la BDD
                loadContacts(); // Rafraîchit la liste
                System.out.println("Contact supprimé : " + selectedContact);
            }
        } else {
            System.out.println("Aucun contact sélectionné !");
        }
    }

    private void selectContact(MouseEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            System.out.println("Contact sélectionné : " + selectedContact);
        }
    }

    @FXML
    private void shoFirstNamedetails(MouseEvent event) {
    }
}
