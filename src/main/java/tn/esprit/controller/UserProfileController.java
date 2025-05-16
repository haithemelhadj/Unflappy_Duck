package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.utils.Session;

import java.io.IOException;

public class UserProfileController {

    // Profile components
    @FXML
    private ImageView profileAvatar;
    
    @FXML
    private Circle avatarCircle;
    
    @FXML
    private StackPane flashMessageContainer;
    
    @FXML
    private Label flashMessageText;
    
    // Avatar control buttons
    @FXML
    private Button generateAvatarBtn;
    
    @FXML
    private Button saveAvatarBtn;
    
    @FXML
    private Button revertAvatarBtn;
    
    @FXML
    private Label avatarSaveMsg;
    
    // User info components
    @FXML
    private Label nameValue;
    
    @FXML
    private Label emailValue;
    
    @FXML
    private Label statusValue;
    
    @FXML
    private Label bioValue;
    
    @FXML
    private Label levelValue;
    
    @FXML
    private ProgressBar xpProgress;
    
    @FXML
    private Label xpValue;
    
    // Form fields
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextArea bioField;
    
    @FXML
    private PasswordField newPasswordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private PasswordField currentPasswordField;
    
    @FXML
    private Button saveChangesBtn;

    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    private String currentAvatarUrl;
    private String originalAvatarUrl;

    @FXML
    public void initialize() {
        loadUserProfile();
        setupEventHandlers();
    }

    // Load user profile information
    private void loadUserProfile() {
        Utilisateur user = Session.getActiveUser();
        
        // Set user info values
        nameValue.setText(user.getNom());
        emailValue.setText(user.getEmail());
        statusValue.setText(user.getStatut() ? "Active" : "Inactive");
        bioValue.setText(user.getBio() != null && !user.getBio().isEmpty() ? user.getBio() : "No bio yet");
        
        // Set level and XP
        levelValue.setText(String.valueOf(user.getNiveau() != null ? user.getNiveau() : "1"));
        
        int xp = user.getXp();
        int xpRequired = user.getXpRequis();
        xpValue.setText(xp + "/" + xpRequired);
        xpProgress.setProgress((double) xp / xpRequired);
        
        // Set form field values
        nameField.setText(user.getNom());
        emailField.setText(user.getEmail());
        bioField.setText(user.getBio());
        
        // Load user profile image
        if (user.getPhotoProfil() != null && !user.getPhotoProfil().isEmpty()) {
            try {
                profileAvatar.setImage(new Image(user.getPhotoProfil()));
                currentAvatarUrl = user.getPhotoProfil();
                originalAvatarUrl = user.getPhotoProfil(); // Store original for reverting
            } catch (Exception e) {
                profileAvatar.setImage(new Image("/images/default-avatar.png"));
                currentAvatarUrl = "/images/default-avatar.png";
                originalAvatarUrl = "/images/default-avatar.png";
            }
        } else {
            // Generate a default avatar using the user's name as seed
            String seed = user.getNom() + "_default";
            currentAvatarUrl = generateAvatarUrl(seed);
            originalAvatarUrl = currentAvatarUrl; // Store original for reverting
            profileAvatar.setImage(new Image(currentAvatarUrl));
        }
        
        // Configure status badge style
        if (user.getStatut()) {
            statusValue.getStyleClass().add("status-active");
        } else {
            statusValue.getStyleClass().add("status-inactive");
        }
    }
    
    private void setupEventHandlers() {
        // Setup avatar buttons
        generateAvatarBtn.setOnAction(e -> generateRandomAvatar());
        saveAvatarBtn.setOnAction(e -> saveCurrentAvatar());
        revertAvatarBtn.setOnAction(e -> revertToOriginalAvatar());
        
        // Setup form event handlers
        nameField.setOnKeyTyped(e -> validateForm());
        emailField.setOnKeyTyped(e -> validateForm());
        newPasswordField.setOnKeyTyped(e -> validatePasswordMatch());
        confirmPasswordField.setOnKeyTyped(e -> validatePasswordMatch());
        
        // Setup save changes button
        saveChangesBtn.setOnAction(e -> saveProfileChanges());
    }
    
    // Generate avatar URL using DiceBear API
    private String generateAvatarUrl(String seed) {
        return "https://api.dicebear.com/7.x/adventurer/svg?seed=" + seed;
    }
    
    // Generate random avatar
    private void generateRandomAvatar() {
        String randomSeed = "avatar_" + Math.random() + "_" + System.currentTimeMillis();
        currentAvatarUrl = generateAvatarUrl(randomSeed);
        profileAvatar.setImage(new Image(currentAvatarUrl));
        
        showFlashMessage("New avatar generated! Click Save to keep it.", "info", 3);
    }
    
    // Save current avatar
    private void saveCurrentAvatar() {
        try {
            Utilisateur user = Session.getActiveUser();
            user.setPhotoProfil(currentAvatarUrl);
            
            serviceUtilisateur.modifierUtilisateur(user);
            originalAvatarUrl = currentAvatarUrl; // Update original after saving
            
            showFlashMessage("Avatar saved successfully!", "success", 3);
        } catch (Exception e) {
            showFlashMessage("Failed to save avatar: " + e.getMessage(), "error", 4);
        }
    }
    
    // Revert to original avatar
    private void revertToOriginalAvatar() {
        currentAvatarUrl = originalAvatarUrl;
        profileAvatar.setImage(new Image(currentAvatarUrl));
        showFlashMessage("Reverted to original avatar", "info", 2);
    }
    
    // Save profile changes
    private void saveProfileChanges() {
        if (!validateForm()) {
            showFlashMessage("Please fix the errors in the form", "error", 3);
            return;
        }
        
        if (!validatePasswordMatch()) {
            showFlashMessage("Passwords do not match", "error", 3);
            return;
        }
        
        try {
            Utilisateur user = Session.getActiveUser();
            user.setNom(nameField.getText());
            user.setEmail(emailField.getText());
            user.setBio(bioField.getText());
            
            // Only update password if a new one is provided
            if (!newPasswordField.getText().isEmpty()) {
                // In a real app, you would verify the current password here
                user.setPassword(newPasswordField.getText()); // This would typically be hashed
            }
            
            serviceUtilisateur.modifierUtilisateur(user);
            
            showFlashMessage("Profile updated successfully!", "success", 3);
            
            // Refresh the profile display
            loadUserProfile();
        } catch (Exception e) {
            showFlashMessage("Failed to update profile: " + e.getMessage(), "error", 4);
        }
    }
    
    // Validate form fields
    private boolean validateForm() {
        boolean isValid = true;
        
        if (nameField.getText().isEmpty()) {
            nameField.getStyleClass().add("field-error");
            isValid = false;
        } else {
            nameField.getStyleClass().remove("field-error");
        }
        
        if (emailField.getText().isEmpty() || !emailField.getText().contains("@")) {
            emailField.getStyleClass().add("field-error");
            isValid = false;
        } else {
            emailField.getStyleClass().remove("field-error");
        }
        
        return isValid;
    }
    
    // Validate password fields match
    private boolean validatePasswordMatch() {
        if (newPasswordField.getText().isEmpty()) {
            // No new password provided, so no validation needed
            newPasswordField.getStyleClass().remove("field-error");
            confirmPasswordField.getStyleClass().remove("field-error");
            return true;
        }
        
        if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
            newPasswordField.getStyleClass().add("field-error");
            confirmPasswordField.getStyleClass().add("field-error");
            return false;
        } else {
            newPasswordField.getStyleClass().remove("field-error");
            confirmPasswordField.getStyleClass().remove("field-error");
            return true;
        }
    }
    
    // Show flash message
    private void showFlashMessage(String message, String type, int seconds) {
        flashMessageText.setText(message);
        
        // Clear previous styles
        flashMessageContainer.getStyleClass().removeAll("flash-success", "flash-error", "flash-info");
        
        // Add appropriate style
        switch (type) {
            case "success":
                flashMessageContainer.getStyleClass().add("flash-success");
                break;
            case "error":
                flashMessageContainer.getStyleClass().add("flash-error");
                break;
            case "info":
                flashMessageContainer.getStyleClass().add("flash-info");
                break;
        }
        
        // Show the message
        flashMessageContainer.setVisible(true);
        flashMessageContainer.setManaged(true);
        
        // Hide after delay
        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000);
                javafx.application.Platform.runLater(() -> {
                    flashMessageContainer.setVisible(false);
                    flashMessageContainer.setManaged(false);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
