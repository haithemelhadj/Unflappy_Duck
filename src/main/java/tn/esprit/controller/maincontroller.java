package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class maincontroller {

    @FXML
    private TextField loginEmailField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField1;
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
    private Label profileUsernameLabel;
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
        Connection connection = MyDatabase.getInstance().getCnx();
        serviceUtilisateur = new ServiceUtilisateur();

        loginPane.setVisible(true);
        mainPane.setVisible(false);

        loginButton.setOnAction(e -> login());
        createAccountButton.setOnAction(e -> createAccount());
        editProfileButton.setOnAction(e -> editProfile());
        showInfoButton.setOnAction(e -> showUserInfo());
        showAllUsersButton.setOnAction(e -> showAllUsers());


        usersListView.setCellFactory(new Callback<ListView<Utilisateur>, ListCell<Utilisateur>>() {
            @Override
            public ListCell<Utilisateur> call(ListView<Utilisateur> listView) {
                return new ListCell<Utilisateur>() {
                    @Override
                    protected void updateItem(Utilisateur utilisateur, boolean empty) {
                        super.updateItem(utilisateur, empty);
                        if (empty || utilisateur == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            VBox card = new VBox(5);
                            card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #1a237e; -fx-border-width: 2;");
                            Label idLabel = new Label("ID: " + utilisateur.getId());
                            Label nameLabel = new Label("Username: " + utilisateur.getNom());
                            Label emailLabel = new Label("Email: " + utilisateur.getEmail());
                            card.getChildren().addAll(idLabel, nameLabel, emailLabel);
                            setGraphic(card);
                        }
                    }
                };
            }
        });
    }

    @FXML
    private void login() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in both email and password.");
            return;
        }
        try {
            Utilisateur user = serviceUtilisateur.loginUtilisateur(email, password);
            if (user != null) {
                currentUser = user;
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + user.getNom() + "!");
                updateProfileSection(user);
                prefillEditFields(user);
                loginPane.setVisible(false);
                mainPane.setVisible(true);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to login: " + e.getMessage());
        }
    }

    @FXML
    private void createAccount() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields.");
            return;
        }
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid email address.");
            return;
        }

        Utilisateur utilisateur = new Utilisateur(
                0,
                username,
                email,
                password,
                "utilisateur",
                "",
                "",
                0,
                null,
                100
        );

        try {
            serviceUtilisateur.ajouterUtilisateur(utilisateur);
            currentUser = utilisateur;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
            clearCreateForm();
            updateProfileSection(utilisateur);
            prefillEditFields(utilisateur);
            loginPane.setVisible(false);
            mainPane.setVisible(true);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to create account: " + e.getMessage());
        }
    }

    @FXML
    private void editProfile() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "No User", "No user loaded. Please log in or create an account first.");
            return;
        }
        String newUsername = usernameField1.getText().trim();
        String newPassword = passwordField1.getText();
        String newEmail = emailField1.getText().trim();

        if (newUsername.isEmpty() || newPassword.isEmpty() || newEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all edit fields.");
            return;
        }
        if (!newEmail.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid email address.");
            return;
        }

        currentUser.setNom(newUsername);
        currentUser.setMotDePasse(newPassword);
        currentUser.setEmail(newEmail);

        try {
            serviceUtilisateur.modifierUtilisateur(currentUser);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
            updateProfileSection(currentUser);
            clearEditForm();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update profile: " + e.getMessage());
        }
    }

    @FXML
    private void showUserInfo() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "No User", "No user loaded. Please log in or create an account first.");
            return;
        }
        updateProfileSection(currentUser);
        prefillEditFields(currentUser);
    }

    @FXML
    private void showAllUsers() {
        try {
            List<Utilisateur> users = serviceUtilisateur.getAllUtilisateurs();
            ObservableList<Utilisateur> observableUsers = FXCollections.observableArrayList(users);
            usersListView.setItems(observableUsers);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve users: " + e.getMessage());
        }
    }

    private void updateProfileSection(Utilisateur utilisateur) {
        profileUsernameLabel.setText("Username: " + utilisateur.getNom());
        profileEmailLabel.setText("Email: " + utilisateur.getEmail());
    }

    private void prefillEditFields(Utilisateur utilisateur) {
        usernameField1.setText(utilisateur.getNom());
        passwordField1.setText(utilisateur.getMotDePasse());
        emailField1.setText(utilisateur.getEmail());
    }

    private void clearCreateForm() {
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
    }

    private void clearEditForm() {
        usernameField1.clear();
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
}
