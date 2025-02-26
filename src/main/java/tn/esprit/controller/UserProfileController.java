package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class UserProfileController {

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    private Label userXpLabel;

    @FXML
    private Button editProfileButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button backButton;

    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    private Utilisateur currentUser;

    @FXML
    public void initialize() {
        // Assume we have a method to get the logged-in user
        currentUser = serviceUtilisateur.getLoggedInUser();
        loadUserProfile(currentUser);
    }

    // Load user profile information
    private void loadUserProfile(Utilisateur user) {
        userNameLabel.setText(user.getNom());
        userEmailLabel.setText(user.getEmail());
        userRoleLabel.setText("Role: " + user.getRole().toString());
        userXpLabel.setText("XP: " + user.getXp());

        // Load user profile image (if any), otherwise set default image
        if (!user.getPhotoProfil().isEmpty()) {
            profileImageView.setImage(new Image(user.getPhotoProfil()));
        } else {
            profileImageView.setImage(new Image("/defaultProfileImage.png")); // Default profile image path
        }
    }

    // Handle Edit Profile button click
    @FXML
    private void handleEditProfile() {
        System.out.println("Edit profile button clicked");
        // Open a new window or form to edit the user's profile
        // Implement the logic to allow profile modification (name, password, profile picture, etc.)
    }

    // Handle Logout button click
    @FXML
    private void handleLogout() {
        System.out.println("Logout button clicked");
        // Implement logout logic here, such as navigating back to the login page
    }

    // Handle Back to Home button click
    @FXML
    private void handleBackToHome() {
        System.out.println("Back to home button clicked");
        // Redirect to home page
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/views/HomePage.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
