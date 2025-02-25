package tn.esprit.controller;

import com.mysql.cj.log.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jdk.jshell.execution.Util;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;

import java.sql.SQLException;

import tn.esprit.controller.Controller;

public class Login {
    @FXML
    private TextField loginEmailField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    Callback call;

    public interface Callback{
        void call();
    }

    public Login(Callback call){
        this.call = call;
    }

    public Callback login(ActionEvent actionEvent) {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in both email and password.");
            Controller.setUser(null);
            return call;
        }
        try {
            Utilisateur user = serviceUtilisateur.loginUtilisateur(email, password);
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + user.getNom() + "!");
                Controller.setUser(user);
                return call;
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to login: " + e.getMessage());
        }
        return call;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
