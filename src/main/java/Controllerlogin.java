import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;
import tn.esprit.services.ServiceUtilisateur;

import java.sql.SQLException;

public class Controllerlogin {

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
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

    // Handle login action
    @FXML
    private void handleLogin() {
        String usernameOrEmail = loginUsernameOrEmailField.getText();
        String password = loginPasswordField.getText();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter both username/email and password.");
            return;
        }

        try {
            Utilisateur utilisateur = serviceUtilisateur.loginUtilisateur(usernameOrEmail, password);
            if (utilisateur != null) {
                showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome " + utilisateur.getNom() + "!");
                redirectToHomePage(utilisateur);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username/email or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error accessing the database.");
            e.printStackTrace();
        }
    }

    // Handle account creation action
    @FXML
    private void handleCreateAccount() {
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


    private void redirectToHomePage(Utilisateur utilisateur) {
        if (!userRoles.admin.equals(utilisateur.getRole())) {
            System.out.println("Redirecting to home page...");

        } else {
            showAlert(Alert.AlertType.WARNING, "Access Denied", "Admin access detected. Staying on login page.");
        }
    }
}
