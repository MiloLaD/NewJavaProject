package com.MyProject.newjavaproject.controllers;

import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.Person;
import javafx.scene.control.DatePicker;
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
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ModifyViewController {

    @FXML
    private ListView<String> ListContact;

    @FXML
    private TextField searchContact, NameField, FirstnameField, NicknameField, NumberField, EmailField, AddressField;

    @FXML
    private DatePicker BirthDateModifyView;

    @FXML
    private Button ModifyContact, CancelModifContactModifView, modifImgModifyView, goAddContact;

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
                // Remplir les champs avec les données actuelles du contact
                NameField.setText(selectedPerson.getLastname());
                FirstnameField.setText(selectedPerson.getFirstname());
                NicknameField.setText(selectedPerson.getNickname());
                NumberField.setText(selectedPerson.getPhoneNumber());
                EmailField.setText(selectedPerson.getEmailAddress());
                AddressField.setText(selectedPerson.getAddress());

                // Correction de la conversion de la date
                if (selectedPerson.getBirthDate() != null) {
                    BirthDateModifyView.setValue(
                        selectedPerson.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    );
                } else {
                    BirthDateModifyView.setValue(null);
                }

                // Mettre à jour l'image si elle existe
                if (selectedPerson.getProfilePicture() != null && !selectedPerson.getProfilePicture().isEmpty()) {
                    imageModify.setImage(new Image(selectedPerson.getProfilePicture()));
                } else {
                    imageModify.setImage(new Image("/styles/user.jpg")); // Image par défaut
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

        // Récupération des nouvelles valeurs saisies
        String newName = NameField.getText().trim();
        String newFirstname = FirstnameField.getText().trim();
        String newNickname = NicknameField.getText().trim();
        String newNumber = NumberField.getText().trim();
        String newEmail = EmailField.getText().trim();
        String newAddress = AddressField.getText().trim();
        LocalDate birthDateLocal = BirthDateModifyView.getValue();

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

        // Mise à jour des informations du contact
        selectedPerson.setLastname(newName);
        selectedPerson.setFirstname(newFirstname);
        selectedPerson.setNickname(newNickname);
        selectedPerson.setPhoneNumber(newNumber);
        selectedPerson.setEmailAddress(newEmail);
        selectedPerson.setAddress(newAddress);
        if (birthDateLocal != null) {
            selectedPerson.setBirthDate(Date.valueOf(birthDateLocal)); // FIX !
        }

        // Mise à jour dans la base de données
        boolean updateSuccess = personDAO.updatePerson(selectedPerson);

        if (updateSuccess) {
            showAlert("Succès", "Le contact a été modifié avec succès !", Alert.AlertType.INFORMATION);

            //  Retourner à HomeView après la modification
            try {
                Stage stage = (Stage) ModifyContact.getScene().getWindow();
                stage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homeView.fxml"));
                Parent root = loader.load();
                HomeViewController homeController = loader.getController();
                homeController.updateContactList();
                homeController.setContactDetails(selectedPerson);

                Stage homeStage = new Stage();
                homeStage.setTitle("Home - Contact Manager");
                homeStage.setScene(new Scene(root));
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
                personDAO.updateProfilePicture(selectedPerson.getIdperson(), selectedFile.toURI().toString());
            }
        }
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
    public void setContactDetails(Person person) {
        if (person != null) {
            this.selectedPerson = person; // Stocke la personne sélectionnée

            //  Remplissage des champs avec les informations existantes
            NameField.setText(person.getLastname());
            FirstnameField.setText(person.getFirstname());
            NicknameField.setText(person.getNickname());
            NumberField.setText(person.getPhoneNumber());
            EmailField.setText(person.getEmailAddress());
            AddressField.setText(person.getAddress());

            //  Vérification si la date de naissance est définie avant de l'afficher
            if (person.getBirthDate() != null) {
                java.sql.Date sqlDate = new java.sql.Date(person.getBirthDate().getTime());
                BirthDateModifyView.setValue(sqlDate.toLocalDate()); // ✅ Correction ici !
            } else {
                BirthDateModifyView.setValue(null);
            }

            //  Mise à jour de l'image si disponible
            if (person.getProfilePicture() != null && !person.getProfilePicture().isEmpty()) {
                imageModify.setImage(new Image(person.getProfilePicture()));
            } else {
                imageModify.setImage(new Image("/styles/user.jpg")); // Image par défaut
            }
        } else {
            System.out.println("Erreur : Aucune personne sélectionnée.");
        }
    }
    @FXML
    private void CancelModifContactModifView(ActionEvent event) {
        try {
            //  Fermer la fenêtre actuelle (ModifyView)
            Stage stage = (Stage) CancelModifContactModifView.getScene().getWindow();
            stage.close();

            //  Charger la page HomeView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homeView.fxml"));
            Parent root = loader.load();

            //Récupérer le contrôleur HomeViewController
            HomeViewController homeController = loader.getController();
            homeController.updateContactList(); // Mettre à jour la liste des contacts

            // Ouvrir la fenêtre HomeView
            Stage homeStage = new Stage();
            homeStage.setTitle("Home - Contact Manager");
            homeStage.setScene(new Scene(root));
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de revenir à la page précédente.", Alert.AlertType.ERROR);
        }
    }


}
