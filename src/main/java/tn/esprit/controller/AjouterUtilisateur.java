package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;
import tn.esprit.services.ServiceUtilisateur;

public class AjouterUtilisateur {

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private PasswordField motDePasseField;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private TextArea bioField;
    @FXML private TextField photoField;
    @FXML private TextField xpField;
    @FXML private TextField niveauField;
    @FXML private TextField xpRequisField;

    private final ServiceUtilisateur utilisateurService = new ServiceUtilisateur();

    @FXML
    public void initialize() {
        /*
        roleChoiceBox.getSelectionModel().select("utilisateur");
        /* */
    }

    @FXML
    private void handleAjouter() {
        /*
        try {
            Utilisateur u = new Utilisateur();
            u.setNom(nomField.getText());
            u.setEmail(emailField.getText());
            u.setMotDePasse(motDePasseField.getText());
            u.setRole(userRoles.valueOf(roleChoiceBox.getValue()));
            u.setBio(bioField.getText());
            u.setPhotoProfil(photoField.getText());
            u.setXp(Integer.parseInt(xpField.getText()));
            u.setNiveau(Integer.parseInt(niveauField.getText()));
            u.setXpRequis(Integer.parseInt(xpRequisField.getText()));
            u.setStatut(true);

            utilisateurService.ajouterUtilisateur(u);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Utilisateur ajouté avec succès !");
            alert.showAndWait();



        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage());
            alert.showAndWait();
        }
        /* */
    }
}
