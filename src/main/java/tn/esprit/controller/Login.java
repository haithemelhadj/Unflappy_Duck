package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;

import java.sql.SQLException;

public class Login {
    @FXML
    private TextField loginEmailField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    private final Callback callback;

    public interface Callback{
        void call();
    }

    public Login(Callback call){
        this.callback = call;
    }

    public void login(ActionEvent actionEvent) {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in both email and password.");
            Controller.setUser(null);
            return;
        }
        try {
            Utilisateur user = serviceUtilisateur.loginUtilisateur(email, password);
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + user.getNom() + "!");
                Controller.setUser(user);
                callback.call();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to login: " + e.getMessage());
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
