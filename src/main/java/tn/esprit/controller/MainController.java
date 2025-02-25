package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.util.List;

public class MainController {

    @FXML
    private TextField loginEmailField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private Button loginButton;

    @FXML
    private TextField pseudoField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;

    @FXML
    private TextField pseudoField1;
    @FXML
    private PasswordField passwordField1;
    @FXML
    private TextField emailField1;

    @FXML
    private Button createAccountButton;
    @FXML
    private Button editProfileButton;
    @FXML
    private Button showInfoButton;
    @FXML
    private Button showAllUsersButton;

    @FXML
    private Label profilePseudoLabel;
    @FXML
    private Label profileEmailLabel;

    @FXML
    private ListView<Utilisateur> usersListView;

    @FXML
    private TitledPane loginPane;
    @FXML
    private TitledPane mainPane;

    private ServiceUtilisateur serviceUtilisateur;
    private Utilisateur currentUser;

    @FXML
    public void initialize() {
        serviceUtilisateur = new ServiceUtilisateur();

        loginPane.setVisible(true);
        mainPane.setVisible(false);

        loginButton.setOnAction(e -> login());
        createAccountButton.setOnAction(e -> createAccount());
        editProfileButton.setOnAction(e -> editProfile());
        showInfoButton.setOnAction(e -> showUserInfo());
        showAllUsersButton.setOnAction(e -> showAllUsers());
    }

    @FXML
    private void login() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur user = serviceUtilisateur.loginUtilisateur(email, password);
        if (user != null && user.isEstActif()) {
            currentUser = user;
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Bienvenue " + user.getPseudo() + "!");
            updateProfileSection(user);
            prefillEditFields(user);
            loginPane.setVisible(false);
            mainPane.setVisible(true);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Compte inactif ou informations incorrectes.");
        }
    }

    @FXML
    private void createAccount() {
        String pseudo = pseudoField.getText().trim();
        String password = passwordField.getText();
        String email = emailField.getText().trim();

        if (pseudo.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur utilisateur = new Utilisateur(0, pseudo, email, password, true, 2);

        serviceUtilisateur.add(utilisateur);
        currentUser = utilisateur;
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès!");
        clearCreateForm();
        updateProfileSection(utilisateur);
        loginPane.setVisible(false);
        mainPane.setVisible(true);
    }

    @FXML
    private void editProfile() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez vous connecter.");
            return;
        }
        String newPseudo = pseudoField1.getText().trim();
        String newPassword = passwordField1.getText();
        String newEmail = emailField1.getText().trim();

        if (newPseudo.isEmpty() || newPassword.isEmpty() || newEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        currentUser.setPseudo(newPseudo);
        currentUser.setMotDePasseHache(newPassword);
        currentUser.setEmail(newEmail);

        serviceUtilisateur.update(currentUser);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour!");
        updateProfileSection(currentUser);
        clearEditForm();
    }

    private void updateProfileSection(Utilisateur utilisateur) {
        profilePseudoLabel.setText("Pseudo: " + utilisateur.getPseudo());
        profileEmailLabel.setText("Email: " + utilisateur.getEmail());
    }

    private void prefillEditFields(Utilisateur utilisateur) {
        pseudoField1.setText(utilisateur.getPseudo());
        passwordField1.setText(utilisateur.getMotDePasseHache());
        emailField1.setText(utilisateur.getEmail());
    }

    private void clearCreateForm() {
        pseudoField.clear();
        passwordField.clear();
        emailField.clear();
    }

    private void clearEditForm() {
        pseudoField1.clear();
        passwordField1.clear();
        emailField1.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showUserInfo() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Aucun utilisateur connecté.");
            return;
        }
        updateProfileSection(currentUser);
        prefillEditFields(currentUser);
    }

    @FXML
    private void showAllUsers() {
        List<Utilisateur> users = serviceUtilisateur.getAll();
        ObservableList<Utilisateur> observableUsers = FXCollections.observableArrayList(users);
        usersListView.setItems(observableUsers);
    }
}
