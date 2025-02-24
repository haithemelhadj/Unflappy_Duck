package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;

import java.sql.SQLException;

public class LoginFreelance {
    @FXML
    private TextField loginEmailField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    private Utilisateur user = null;


    public boolean login(ActionEvent actionEvent) {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in both email and password.");
            return false;
        }
        try {
            user = serviceUtilisateur.loginUtilisateur(email, password);
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + user.getNom() + "!");
                return true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to login: " + e.getMessage());
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Utilisateur getUser(){return user;}
}
