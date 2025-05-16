package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;
import tn.esprit.services.ServiceUtilisateur;

public class ModifierUtilisateur {

    @FXML private Label idLabel;
    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<userRoles> roleChoiceBox;
    @FXML private TextArea bioField;
    @FXML private TextField photoField;
    @FXML private TextField xpField;
    @FXML private TextField niveauField;
    @FXML private TextField xpRequisField;

    private Utilisateur utilisateur;

    private final ServiceUtilisateur utilisateurService = new ServiceUtilisateur();

    public void setUtilisateur(Utilisateur u) {
        this.utilisateur = u;
        populateFields();
    }

    private void populateFields() {
        idLabel.setText("ID: " + utilisateur.getId());
        nomField.setText(utilisateur.getNom());
        emailField.setText(utilisateur.getEmail());
        passwordField.setText(utilisateur.getPassword());
        roleChoiceBox.setValue(utilisateur.getRole());
        bioField.setText(utilisateur.getBio());
        photoField.setText(utilisateur.getPhotoProfil());
        xpField.setText(String.valueOf(utilisateur.getXp()));
        niveauField.setText(String.valueOf(utilisateur.getNiveau()));
        xpRequisField.setText(String.valueOf(utilisateur.getXpRequis()));
    }

    @FXML
    private void handleModifier() {
        try {
            utilisateur.setNom(nomField.getText());
            utilisateur.setEmail(emailField.getText());
            utilisateur.setPassword(passwordField.getText());
            utilisateur.setRole(userRoles.valueOf(String.valueOf(roleChoiceBox.getValue())));
            utilisateur.setBio(bioField.getText());
            utilisateur.setPhotoProfil(photoField.getText());
            utilisateur.setXp(Integer.parseInt(xpField.getText()));
            utilisateur.setNiveau(Integer.parseInt(niveauField.getText()));
            utilisateur.setXpRequis(Integer.parseInt(xpRequisField.getText()));

            utilisateurService.modifierUtilisateur(utilisateur);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Utilisateur modifié avec succès !");
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
