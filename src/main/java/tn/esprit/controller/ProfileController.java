package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class ProfileController {

    @FXML
    private ImageView profileImageView;

    @FXML
    private TextField nameTextField, emailTextField;

    @FXML
    private TextArea bioTextArea;

    @FXML
    private Label xpLabel, levelLabel;

    private FileChooser fileChooser;
    private File profileImageFile;

    @FXML
    public void initialize() {
        // Initial setup can be done here
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
    }

    @FXML
    public void handleUploadProfilePicture() {
        Stage stage = (Stage) profileImageView.getScene().getWindow();
        profileImageFile = fileChooser.showOpenDialog(stage);
        if (profileImageFile != null) {
            Image profileImage = new Image(profileImageFile.toURI().toString());
            profileImageView.setImage(profileImage);
        }
    }

    @FXML
    public void handleSaveProfile() {

        String name = nameTextField.getText();
        String email = emailTextField.getText();
        String bio = bioTextArea.getText();


        System.out.println("Profile saved for user: " + name);
    }
}
