package com.MyProject.newjavaproject.controllers;

import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.Person;
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

    @FXML private ListView<String> ListContact;  
    @FXML private TextField searchContact; 
    @FXML private Button goAddContact, ModifyContact, DeletContact; 

    private personDAO personDAO = new personDAO();
    private ObservableList<String> personNames = FXCollections.observableArrayList();

    @FXML private HBox NickNameHomeView, NumberHomeView, NameHomeView, FirstNameHomeView;
    @FXML private HBox NickName2HomeView, AddressHomeView, birthdateHomeView, EmailHomeView;
    @FXML private AnchorPane PagecacheHomeView;
    @FXML private ImageView imageHome;

    // Labels liés à la vue FXML
    @FXML private Label NameLabel;
    @FXML private Label FirstNameLabel;
    @FXML private Label NickNameLabel;
    @FXML private Label NickName2Label;
    @FXML private Label NumberLabel;
    @FXML private Label AddressHomeViewLabel;
    @FXML private Label EmailHomeViewLabel;
    @FXML private Label birthdateHomeViewLabel;

    public void initialize() {
        loadContacts();
        setupSearchFunction();
        PagecacheHomeView.setVisible(true);
        ListContact.setOnMouseClicked(this::selectContact);
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

    @FXML
    private void goModifyContact(ActionEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();

        if (selectedContact != null) {
            try {
                Person selectedPerson = personDAO.getAllPersons().stream()
                        .filter(p -> (p.getFirstname() + " " + p.getLastname()).equals(selectedContact))
                        .findFirst().orElse(null);

                if (selectedPerson == null) {
                    System.out.println("Erreur : Contact non trouvé !");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifyView.fxml"));
                Parent root = loader.load();

                ModifyViewController modifyController = loader.getController();
                modifyController.setContactDetails(selectedPerson);

                Stage stage = new Stage();
                stage.setTitle("Modify Contact");
                stage.setScene(new Scene(root));
                stage.show();

                ((Stage) ModifyContact.getScene().getWindow()).close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun contact sélectionné !");
        }
    }

    @FXML
    private void DeletContact(ActionEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            Person personToDelete = personDAO.getAllPersons().stream()
                    .filter(p -> (p.getFirstname() + " " + p.getLastname()).equals(selectedContact))
                    .findFirst().orElse(null);

            if (personToDelete != null) {
                personDAO.deletePerson(personToDelete.getIdperson());
                loadContacts();
                PagecacheHomeView.setVisible(true);
                resetLabels();
            }
        } else {
            System.out.println("Aucun contact sélectionné !");
        }
    }

    public void updateContactList() {
        loadContacts();
    }

    private void selectContact(MouseEvent event) {
        String selectedContact = ListContact.getSelectionModel().getSelectedItem();

        if (selectedContact != null) {
            Person selectedPerson = personDAO.getAllPersons().stream()
                    .filter(p -> (p.getFirstname() + " " + p.getLastname()).equals(selectedContact))
                    .findFirst().orElse(null);

            if (selectedPerson != null) {
                PagecacheHomeView.setVisible(false);

                NameLabel.setText(selectedPerson.getLastname());
                FirstNameLabel.setText(selectedPerson.getFirstname());
                NickNameLabel.setText(selectedPerson.getNickname());
                NickName2Label.setText(selectedPerson.getNickname());
                NumberLabel.setText(selectedPerson.getPhoneNumber());
                AddressHomeViewLabel.setText(selectedPerson.getAddress());
                EmailHomeViewLabel.setText(selectedPerson.getEmailAddress());

                if (selectedPerson.getBirthDate() != null) {
                    birthdateHomeViewLabel.setText(selectedPerson.getBirthDate().toString());
                } else {
                    birthdateHomeViewLabel.setText("Non défini");
                }

                if (selectedPerson.getProfilePicture() != null && !selectedPerson.getProfilePicture().isEmpty()) {
                    imageHome.setImage(new Image(selectedPerson.getProfilePicture()));
                } else {
                    imageHome.setImage(new Image("/styles/user.jpg"));
                }
            }
        } else {
            PagecacheHomeView.setVisible(true);
            resetLabels();
        }
    }

    private void resetLabels() {
        NameLabel.setText("");
        FirstNameLabel.setText("");
        NickNameLabel.setText("");
        NickName2Label.setText("");
        NumberLabel.setText("");
        AddressHomeViewLabel.setText("");
        EmailHomeViewLabel.setText("");
        birthdateHomeViewLabel.setText("");
        imageHome.setImage(new Image("/styles/user.jpg"));
    }

    public void setContactDetails(Person person) {
        if (person != null) {
            PagecacheHomeView.setVisible(false);

            NameLabel.setText(person.getLastname());
            FirstNameLabel.setText(person.getFirstname());
            NickNameLabel.setText(person.getNickname());
            NickName2Label.setText(person.getNickname());
            NumberLabel.setText(person.getPhoneNumber());
            AddressHomeViewLabel.setText(person.getAddress());
            EmailHomeViewLabel.setText(person.getEmailAddress());

            if (person.getBirthDate() != null) {
                birthdateHomeViewLabel.setText(person.getBirthDate().toString());
            } else {
                birthdateHomeViewLabel.setText("Non défini");
            }

            if (person.getProfilePicture() != null && !person.getProfilePicture().isEmpty()) {
                imageHome.setImage(new Image(person.getProfilePicture()));
            } else {
                imageHome.setImage(new Image("/styles/user.jpg"));
            }
        } else {
            resetLabels();
        }
    }
}
