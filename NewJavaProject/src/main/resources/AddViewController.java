/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package com.MyProject.controllers;

import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.util.Date;

public class AddViewController {

    @FXML
    private TextField Namead;

    @FXML
    private TextField Firstnamead;

    @FXML
    private TextField Nicknamead;

    @FXML
    private TextField Numberad;

    @FXML
    private Button AddContact;

    @FXML
    private Button returnpp;

    private personDAO personDAO = new personDAO(); // DAO pour la base de données

    public void initialize() {
        // Initialisation si nécessaire
    }

    @FXML
    private void AddContact(ActionEvent event) {
        // Récupération des valeurs entrées par l'utilisateur
        String name = Namead.getText().trim();
        String firstname = Firstnamead.getText().trim();
        String nickname = Nicknamead.getText().trim();
        String number = Numberad.getText().trim();

        // Vérification que tous les champs sont remplis
        if (name.isEmpty() || firstname.isEmpty() || number.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires (Nom, Prénom, Numéro).", Alert.AlertType.ERROR);
            return;
        }

        // Vérification que le numéro est valide (ex: au moins 10 chiffres)
        if (!number.matches("\\d{10}")) {
            showAlert("Erreur", "Le numéro doit contenir 10 chiffres.", Alert.AlertType.ERROR);
            return;
        }

        // Création d'un nouvel objet Person
        Person newPerson = new Person(0, name, firstname, nickname, number, "", "", new Date());

        // Ajout du contact à la base de données
        personDAO.addPerson(newPerson);

        // Affichage d'un message de succès
        showAlert("Succès", "Le contact a été ajouté avec succès !", Alert.AlertType.INFORMATION);

        // Réinitialisation des champs après ajout
        clearFields();
    }

    @FXML
    private void returnPagePrincipale(ActionEvent event) {
        System.out.println("Retour à la page principale");
        // Ici, vous pouvez ajouter le code pour changer de scène vers la page principale
    }

    private void clearFields() {
        Namead.clear();
        Firstnamead.clear();
        Nicknamead.clear();
        Numberad.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void addName(ActionEvent event) {
    }

    @FXML
    private void addFirstname(ActionEvent event) {
    }

    @FXML
    private void addNickname(ActionEvent event) {
    }

    @FXML
    private void addNumber(ActionEvent event) {
    }
}
