/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package com.MyProject.newjavaproject.controllers;

import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.Person;
import com.gluonhq.charm.glisten.control.Icon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HomeViewController {

    @FXML
    private ListView<String> ListContact;  // Liste des contacts

    @FXML
    private TextField searchContact; // Champ de recherche

    @FXML
    private Button goAddContact, ModifyContact, DeletContact; // Boutons d'actions

    private personDAO personDAO = new personDAO();  // DAO pour gérer les contacts
    private ObservableList<String> personNames = FXCollections.observableArrayList(); // Liste observable

    @FXML
    private HBox NickNameHomeView;
    @FXML
    private HBox NumberHomeView;
    @FXML
    private HBox NameHomeView;
    @FXML
    private HBox FirstNameHomeView;
    @FXML
    private HBox NickName2HomeView;
    @FXML
    private AnchorPane PagecacheHomeView;
    @FXML
    private ImageView imageHome; // ImageView pour la photo de profil

    // Labels pour afficher les informations dans les HBox
    private Label NickNameLabel = new Label();
    private Label NumberLabel = new Label();
    private Label NameLabel = new Label();
    private Label FirstNameLabel = new Label();
    private Label NickName2Label = new Label();

    public void initialize() {
        loadContacts(); // Charge les contacts à l'initialisation
        setupSearchFunction(); // Active la recherche dynamique
        
        // Afficher la page cache tant qu'aucun contact n'est sélectionné
        PagecacheHomeView.setVisible(true);

        // Ajouter les Labels aux HBox
        NickNameHomeView.getChildren().add(NickNameLabel);
        NumberHomeView.getChildren().add(NumberLabel);
        NameHomeView.getChildren().add(NameLabel);
        FirstNameHomeView.getChildren().add(FirstNameLabel);
        NickName2HomeView.getChildren().add(NickName2Label);

        // Écouter la sélection de la `ListView`
        ListContact.setOnMouseClicked(this::selectContact);
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

    // 🔹 Redirection vers la vue d'ajout (addView.fxml)
    @FXML
    private void gotoAddContactView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addView.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Add Contact");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) goAddContact.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Redirection vers la vue de modification (modifyView.fxml)
    @FXML
    private void goModifyContact(ActionEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();

        if (selectedContact != null) {
            try {
                //Récupérer la personne sélectionnée
                Person selectedPerson = personDAO.getAllPersons().stream()
                        .filter(p -> (p.getFirstname() + " " + p.getLastname()).equals(selectedContact))
                        .findFirst().orElse(null);

                if (selectedPerson == null) {
                    System.out.println("Erreur : Contact non trouvé !");
                    return;
                }

                // Charger la vue ModifyView.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifyView.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur ModifyViewController
                ModifyViewController modifyController = loader.getController();
                
                // Passer l'objet Person au contrôleur
                modifyController.setContactDetails(selectedPerson);

                // Ouvrir la fenêtre ModifyView
                Stage stage = new Stage();
                stage.setTitle("Modify Contact");
                stage.setScene(new Scene(root));
                stage.show();

                // Fermer la fenêtre actuelle SEULEMENT SI la nouvelle fenêtre s'ouvre correctement
                ((Stage) ModifyContact.getScene().getWindow()).close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de la page ModifyView.fxml");
            }
        } else {
            System.out.println("Aucun contact sélectionné !");
        }
    }


    @FXML
    private void DeletContact(ActionEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            List<Person> persons = personDAO.getAllPersons();
            Person personToDelete = persons.stream()
                    .filter(p -> (p.getFirstname() + " " + p.getLastname()).equals(selectedContact))
                    .findFirst().orElse(null);

            if (personToDelete != null) {
                personDAO.deletePerson(personToDelete.getIdperson());
                loadContacts();
                System.out.println("Contact supprimé : " + selectedContact);
            }
        } else {
            System.out.println("Aucun contact sélectionné !");
        }
    }
    public void updateContactList() {
        List<Person> persons = personDAO.getAllPersons();
        personNames.clear();

        for (Person person : persons) {
            personNames.add(person.getFirstname() + " " + person.getLastname());
        }

        ListContact.setItems(FXCollections.observableArrayList(personNames));
    }

    private void selectContact(MouseEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();
        
        if (selectedContact != null) {
            // Récupération des informations de la personne sélectionnée
            List<Person> persons = personDAO.getAllPersons();
            Person selectedPerson = persons.stream()
                    .filter(p -> (p.getFirstname() + " " + p.getLastname()).equals(selectedContact))
                    .findFirst().orElse(null);

            if (selectedPerson != null) {
                PagecacheHomeView.setVisible(false); // Afficher la page cache
                
                // Mettre à jour les labels dans les HBox
                ((Label) NickNameHomeView.getChildren().get(0)).setText(selectedPerson.getNickname());
                ((Label) NumberHomeView.getChildren().get(0)).setText(selectedPerson.getPhoneNumber());
                ((Label) NameHomeView.getChildren().get(0)).setText(selectedPerson.getLastname());
                ((Label) FirstNameHomeView.getChildren().get(0)).setText(selectedPerson.getFirstname());
                ((Label) NickName2HomeView.getChildren().get(0)).setText(selectedPerson.getNickname());
            }
        } else {
            PagecacheHomeView.setVisible(true);
        }
    }

    public void setContactDetails(Person person) {
        if (person != null) {
            // On suppose que le premier enfant de chaque HBox est un Label ou un TextField
            ((Label) NameHomeView.getChildren().get(0)).setText(person.getLastname());
            ((Label) FirstNameHomeView.getChildren().get(0)).setText(person.getFirstname());
            ((Label) NickNameHomeView.getChildren().get(0)).setText(person.getNickname());
            ((Label) NumberHomeView.getChildren().get(0)).setText(person.getPhoneNumber());

            // Mise à jour de l'image
            if (person.getProfilePicture() != null && !person.getProfilePicture().isEmpty()) {
                imageHome.setImage(new Image(person.getProfilePicture()));
            }
        }
    }
}

