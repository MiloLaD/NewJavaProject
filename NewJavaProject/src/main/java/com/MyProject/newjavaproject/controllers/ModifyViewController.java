package com.MyProject.newjavaproject.controllers;

import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ModifyViewController {

    @FXML
    private ListView<String> ListContact;
    
    @FXML
    private TextField searchContact, NameField, FirstnameField, NicknameField, NumberField;

    @FXML
    private Button ModifyContact, CancelModifContactModifView, modifImgModifyView, goAddContact;

    @FXML
    private HBox NickNameModifView, NumberModifView, FirstNameModifView,NameModifView;
    
    @FXML
    private ImageView imageModify;

    private personDAO personDAO = new personDAO();  
    private ObservableList<String> personNames = FXCollections.observableArrayList();
    private Person selectedPerson = null;

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

    @FXML
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

                // Mettre à jour les valeurs des HBox
                ((TextField) NickNameModifView.getChildren().get(0)).setText(selectedPerson.getNickname());
                ((TextField) NumberModifView.getChildren().get(0)).setText(selectedPerson.getPhoneNumber());
                ((TextField) FirstNameModifView.getChildren().get(0)).setText(selectedPerson.getFirstname());
                ((TextField)NameModifView.getChildren().get(0)).setText(selectedPerson.getLastname());

                //Mettre à jour l'image si elle existe
                if (selectedPerson.getProfilePicture() != null && !selectedPerson.getProfilePicture().isEmpty()) {
                    imageModify.setImage(new Image(selectedPerson.getProfilePicture()));
                }
            }
        }
    }

    @FXML
    private void ModifyContact(ActionEvent event) {
        if (selectedPerson == null) {
            showAlert("Erreur", "Veuillez sélectionner un contact à modifier.", Alert.AlertType.ERROR);
            return;
        }

        // Récupération des nouvelles valeurs
        String newName = NameField.getText().trim();
        String newFirstname = FirstnameField.getText().trim();
        String newNickname = NicknameField.getText().trim();
        String newNumber = NumberField.getText().trim();

        // Vérifications des champs obligatoires
        if (newName.isEmpty() || newFirstname.isEmpty() || newNumber.isEmpty()) {
            showAlert("Erreur", "Les champs Nom, Prénom et Numéro sont obligatoires.", Alert.AlertType.ERROR);
            return;
        }

        // Vérification du format du numéro de téléphone
        if (!newNumber.matches("\\d{10}")) {
            showAlert("Erreur", "Le numéro doit contenir 10 chiffres.", Alert.AlertType.ERROR);
            return;
        }

        // Mise à jour des informations du contact sélectionné
        selectedPerson.setLastname(newName);
        selectedPerson.setFirstname(newFirstname);
        selectedPerson.setNickname(newNickname);
        selectedPerson.setPhoneNumber(newNumber);

        // Mise à jour dans la base de données
        boolean updateSuccess = personDAO.updatePerson(selectedPerson);

        if (updateSuccess) {
            showAlert("Succès", "Le contact a été modifié avec succès !", Alert.AlertType.INFORMATION);

            // Retour à la page de détails du contact modifié
            try {
                // Fermer la fenêtre actuelle (ModifyView)
                Stage stage = (Stage) ModifyContact.getScene().getWindow();
                stage.close();

                // Charger la vue précédente (HomeView.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homeView.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur de la page HomeView
                HomeViewController homeController = loader.getController();

                // Mettre à jour la page avec les nouvelles informations du contact
                homeController.setContactDetails(selectedPerson);

                // Ouvrir la nouvelle fenêtre avec les informations mises à jour
                Stage homeStage = new Stage();
                homeStage.setTitle("Home - Contact Manager");
                homeStage.setScene(new Scene(root, 800, 600));
                homeStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de revenir à la page précédente.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de la modification du contact.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void CancelModifContactModifView(ActionEvent event) {
        try {
            // Fermer la fenêtre actuelle (ModifyView)
            Stage stage = (Stage) CancelModifContactModifView.getScene().getWindow();
            stage.close();

            // Charger la vue précédente (HomeView.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homeView.fxml"));
            Parent root = loader.load();

            Stage homeStage = new Stage();
            homeStage.setTitle("Home - Contact Manager");
            homeStage.setScene(new Scene(root));
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de revenir à la page précédente.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void modifImageMoView(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une nouvelle photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image newImage = new Image(selectedFile.toURI().toString());
            imageModify.setImage(newImage);

            if (selectedPerson != null) {
                selectedPerson.setProfilePicture(selectedFile.toURI().toString());
                personDAO.updatePerson(selectedPerson);
            }
        }
    }

    @FXML
    private void gotoAddContactView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Contact");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) goAddContact.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page d'ajout.", Alert.AlertType.ERROR);
        }
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

    public void setContactDetails(Person person) {
        if (person != null) {
            this.selectedPerson = person;

            // Mettre à jour les champs avec les informations de la personne sélectionnée
            NameField.setText(person.getLastname());
            FirstnameField.setText(person.getFirstname());
            NicknameField.setText(person.getNickname());
            NumberField.setText(person.getPhoneNumber());

            // Charger la photo de profil si elle existe
            if (person.getProfilePicture() != null && !person.getProfilePicture().isEmpty()) {
                imageModify.setImage(new Image(person.getProfilePicture()));
            } else {
                imageModify.setImage(new Image("/styles/user.jpg")); // Image par défaut
            }
        } else {
            System.out.println("⚠️ Erreur : Aucune personne sélectionnée.");
        }
    }

}
