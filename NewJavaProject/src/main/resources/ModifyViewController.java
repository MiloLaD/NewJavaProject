/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package com.MyProject.controllers;

import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ModifyViewController {

    @FXML
    private ListView<String> ListContact;
    @FXML
    private TextField searchContact;
    private TextField NameField;

    @FXML
    private TextField FirstnameField, NicknameField, NumberField;

    @FXML
    private Button ModifyContact, DeletContact;

    private personDAO personDAO = new personDAO();  // DAO pour interagir avec la base de données
    private ObservableList<String> personNames = FXCollections.observableArrayList();
    private Person selectedPerson = null;
    @FXML
    private Button goAddContact;
    @FXML
    private HBox NickName;
    @FXML
    private HBox Numer;
    @FXML
    private HBox NickName1;
    @FXML
    private HBox FirstNamedetails;
    @FXML
    private ImageView imageModify;
    @FXML
    private Button modifBtnImgModify;

    public void initialize() {
        loadContacts();
        setupSearchFunction();
    }

    private void loadContacts() {
        List<Person> persons = personDAO.getAllPersons();
        personNames.clear();

        for (Person person : persons) {
            personNames.add(person.getFirstname() + " " + person.getLastname());
        }

        ListContact.setItems(personNames);
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

    private void selectContact(MouseEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            List<Person> persons = personDAO.getAllPersons();
            selectedPerson = persons.stream()
                    .filter(p -> (p.getFirstname() + " " + p.getLastname()).equals(selectedContact))
                    .findFirst().orElse(null);

            if (selectedPerson != null) {
                NameField.setText(selectedPerson.getLastname());
                FirstnameField.setText(selectedPerson.getFirstname());
                NicknameField.setText(selectedPerson.getNickname());
                NumberField.setText(selectedPerson.getPhoneNumber());
            }
        }
    }

    @FXML
    private void ModifyContact(ActionEvent event) {
        if (selectedPerson == null) {
            showAlert("Erreur", "Veuillez sélectionner un contact à modifier.", Alert.AlertType.ERROR);
            return;
        }

        String newName = NameField.getText().trim();
        String newFirstname = FirstnameField.getText().trim();
        String newNickname = NicknameField.getText().trim();
        String newNumber = NumberField.getText().trim();

        if (newName.isEmpty() || newFirstname.isEmpty() || newNumber.isEmpty()) {
            showAlert("Erreur", "Les champs Nom, Prénom et Numéro sont obligatoires.", Alert.AlertType.ERROR);
            return;
        }

        if (!newNumber.matches("\\d{10}")) {
            showAlert("Erreur", "Le numéro doit contenir 10 chiffres.", Alert.AlertType.ERROR);
            return;
        }

        selectedPerson.setLastname(newName);
        selectedPerson.setFirstname(newFirstname);
        selectedPerson.setNickname(newNickname);
        selectedPerson.setPhoneNumber(newNumber);

        personDAO.updatePerson(selectedPerson);
        showAlert("Succès", "Le contact a été modifié avec succès !", Alert.AlertType.INFORMATION);
        loadContacts();
    }

    @FXML
    private void DeletContact(ActionEvent event) {
        if (selectedPerson == null) {
            showAlert("Erreur", "Veuillez sélectionner un contact à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        personDAO.deletePerson(selectedPerson.getIdperson());
        showAlert("Succès", "Le contact a été supprimé.", Alert.AlertType.INFORMATION);
        selectedPerson = null;
        clearFields();
        loadContacts();
    }

    private void CancelModification(ActionEvent event) {
        selectedPerson = null;
        clearFields();
        showAlert("Info", "Modification annulée.", Alert.AlertType.INFORMATION);
    }

    private void clearFields() {
        NameField.clear();
        FirstnameField.clear();
        NicknameField.clear();
        NumberField.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void gotoAddContactView(ActionEvent event) {
    }

    @FXML
    private void shoFirstNamedetails(MouseEvent event) {
    }
}