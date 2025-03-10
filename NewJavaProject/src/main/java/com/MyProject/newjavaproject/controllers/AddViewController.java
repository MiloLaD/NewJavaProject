package com.MyProject.newjavaproject.controllers;

import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.Person;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddViewController {

    @FXML
    private TextField Namead, Firstnamead, Nicknamead, Numberad, mailAddView, adressAddView;

    @FXML
    private DatePicker BirthdayAddView;

    @FXML
    private Button AddContact, returnpp, AddphotoAddView;

    @FXML
    private ImageView ImageAddView; // ImageView pour la photo de profil

    private personDAO personDAO = new personDAO(); // DAO pour la base de données
    private String profilePicturePath = ""; // Chemin de l'image sélectionnée

    public void initialize() {
        // Initialisation si nécessaire
    }

    @FXML
    private void AddContact(ActionEvent event) {
        // Récupération des valeurs saisies par l'utilisateur
        String name = Namead.getText().trim();
        String firstname = Firstnamead.getText().trim();
        String nickname = Nicknamead.getText().trim();
        String number = Numberad.getText().trim();
        String email = mailAddView.getText().trim();
        String address = adressAddView.getText().trim();
        LocalDate birthDateLocal = BirthdayAddView.getValue();

        // Vérification que tous les champs obligatoires sont remplis
        if (name.isEmpty() || firstname.isEmpty() || number.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires (Nom, Prénom, Numéro).", Alert.AlertType.ERROR);
            return;
        }

        // Vérification du format du numéro (ex: 10 chiffres)
        if (!number.matches("\\d{10}")) {
            showAlert("Erreur", "Le numéro doit contenir 10 chiffres.", Alert.AlertType.ERROR);
            return;
        }

        // Conversion de LocalDate en Date pour la BDD
        Date birthDate = (birthDateLocal != null) ? Date.from(birthDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        // Création d'un nouvel objet Person avec la photo de profil
        Person newPerson = new Person(0, name, firstname, nickname, number, address, email, birthDate);
        newPerson.setProfilePicture(profilePicturePath);

        // Ajout du contact dans la base de données
        personDAO.addPerson(newPerson);

        // Affichage du message de succès
        showAlert("Succès", "Le contact a été ajouté avec succès !", Alert.AlertType.INFORMATION);

        // Réinitialisation des champs après l'ajout
        clearFields();

        // Retour automatique à la `HomeView`
        returnPagePrincipale(event);
    }

    @FXML
    private void returnPagePrincipale(ActionEvent event) {
        try {
            // Charger la page principale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homeView.fxml"));
            Parent root = loader.load();

            // Obtenir la scène et changer le contenu
            Stage stage = (Stage) returnpp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home - Contact Manager");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de retourner à la page principale", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void changeProfilePicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image newImage = new Image(selectedFile.toURI().toString());
            ImageAddView.setImage(newImage);
            profilePicturePath = selectedFile.toURI().toString(); // Stocker le chemin de l'image
        }
    }

    private void clearFields() {
        Namead.clear();
        Firstnamead.clear();
        Nicknamead.clear();
        Numberad.clear();
        mailAddView.clear();
        adressAddView.clear();
        BirthdayAddView.setValue(null);
        ImageAddView.setImage(new Image("/styles/user.jpg")); // Réinitialiser l'image de profil par défaut
        profilePicturePath = "";
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
