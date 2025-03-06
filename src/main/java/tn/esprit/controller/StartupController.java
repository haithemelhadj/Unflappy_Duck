package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;
import tn.esprit.services.ServiceUtilisateur;
import tn.esprit.utils.Session;

import java.sql.SQLException;

public class StartupController {
    @FXML
    private TextField loginUsernameOrEmailField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private TextField createNameField;

    @FXML
    private TextField createEmailField;

    @FXML
    private PasswordField createPasswordField;

    @FXML
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    private final Callback callback;



    public interface Callback{
        void call();
    }

    public StartupController(Callback call){
        this.callback = call;
    }

    public void login(ActionEvent actionEvent) {
        String email = loginUsernameOrEmailField.getText().trim();
        String password = loginPasswordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in both email and password.");
            return;
        }
        try {
            Utilisateur user = serviceUtilisateur.loginUtilisateur(email, password);
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "StartupController Successful", "Welcome " + user.getNom() + "!");
                Session.start(user);
                callback.call();
            } else {
                showAlert(Alert.AlertType.ERROR, "StartupController Failed", "Invalid email or password.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to login: " + e.getMessage());
        }
    }

    public void createAccount(ActionEvent actionEvent) {
        String name = createNameField.getText();
        String email = createEmailField.getText();
        String password = createPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Account Creation Error", "Please fill out all fields.");
            return;
        }

        Utilisateur newUser = new Utilisateur();
        newUser.setNom(name);
        newUser.setEmail(email);
        newUser.setMotDePasse(password);
        newUser.setRole(userRoles.utilisateur);
        newUser.setBio("");
        newUser.setPhotoProfil("");
        newUser.setXp(0);
        newUser.setNiveau(null);
        newUser.setXpRequis(100);

        try {
            serviceUtilisateur.ajouterUtilisateur(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Account Created", "Your account has been created successfully!");
            Session.start(newUser);
            callback.call();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error while creating the account.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
