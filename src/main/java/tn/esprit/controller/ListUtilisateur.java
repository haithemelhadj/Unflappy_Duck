package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtilisateur {

    public TextField searchField;
    @FXML private ListView<Utilisateur> listView;
    private List<Utilisateur> utilisateurs;
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

    @FXML
    public void initialize() throws SQLException {
        utilisateurs = serviceUtilisateur.getAllUtilisateurs();
        loadData(utilisateurs);
        setupListView();
    }

    private void setupListView() {
        listView.setCellFactory(param -> new UserListCell());
    }

    private void loadData(List<Utilisateur> u) {
        listView.setItems(FXCollections.observableArrayList(u));
    }

    @FXML
    public void handleAjouter(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/GestionUtilisateur/AjoutUtilisateur.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Ajouter un utilisateur");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            // Refresh after adding
            loadData(utilisateurs);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Impossible d'ouvrir la fenêtre d'ajout: " + e.getMessage());
        }
    }

    @FXML
    public void handleModifier(ActionEvent actionEvent) {
        Utilisateur selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackOffice/GestionUtilisateur/ModifierUtilisateur.fxml"));
                Scene scene = new Scene(loader.load());
                
                ModifierUtilisateur controller = loader.getController();
                controller.setUtilisateur(selected);
                
                Stage stage = new Stage();
                stage.setTitle("Modifier l'utilisateur");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                
                // Refresh after modification
                loadData(utilisateurs);
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                        "Impossible d'ouvrir la fenêtre de modification: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", 
                    "Veuillez sélectionner un utilisateur à modifier.");
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void rechercher(ActionEvent actionEvent) throws SQLException {
        utilisateurs = serviceUtilisateur.getAllUtilisateurs();
        List<Utilisateur> filteredData = utilisateurs.stream()
                .filter(a -> a.getNom().toLowerCase().contains(searchField.getText().toLowerCase()))
                .collect(Collectors.toList());
        listView.setItems((ObservableList<Utilisateur>) filteredData); // Use displayArticle to add filtered items
    }

    public void tri(ActionEvent actionEvent) {
    }

    // Custom ListCell implementation for Utilisateur objects
    private class UserListCell extends ListCell<Utilisateur> {
        private final VBox content;
        private final GridPane gridPane;
        private final Label nameLabel;
        private final Label emailLabel;
        private final Label roleLabel;
        private final Label xpLabel;
        private final Label levelLabel;
        private final Label statusLabel;
        
        public UserListCell() {
            content = new VBox(5);
            content.setPadding(new Insets(10));
            content.getStyleClass().add("user-card");
            
            // Header with Name and Role
            HBox header = new HBox(10);
            header.setAlignment(Pos.CENTER_LEFT);
            
            nameLabel = new Label();
            nameLabel.getStyleClass().add("user-name");
            
            roleLabel = new Label();
            roleLabel.getStyleClass().add("user-role");
            HBox.setHgrow(roleLabel, Priority.ALWAYS);
            
            statusLabel = new Label();
            statusLabel.getStyleClass().add("user-status");
            
            header.getChildren().addAll(nameLabel, roleLabel, statusLabel);
            
            // Information Grid
            gridPane = new GridPane();
            gridPane.setHgap(15);
            gridPane.setVgap(5);
            gridPane.setPadding(new Insets(5, 0, 0, 0));
            
            // Email
            gridPane.add(new Label("Email:"), 0, 0);
            emailLabel = new Label();
            gridPane.add(emailLabel, 1, 0);
            
            // XP and Level
            gridPane.add(new Label("XP:"), 0, 1);
            xpLabel = new Label();
            gridPane.add(xpLabel, 1, 1);
            
            gridPane.add(new Label("Niveau:"), 2, 1);
            levelLabel = new Label();
            gridPane.add(levelLabel, 3, 1);
            
            // Add everything to the content
            content.getChildren().addAll(header, gridPane, new Separator());
        }
        
        @Override
        protected void updateItem(Utilisateur user, boolean empty) {
            super.updateItem(user, empty);
            
            if (empty || user == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Set user data to the components
                nameLabel.setText(user.getNom());
                emailLabel.setText(user.getEmail());
                roleLabel.setText(user.getRole().toString());
                xpLabel.setText(String.valueOf(user.getXp()));
                levelLabel.setText(user.getNiveau() != null ? String.valueOf(user.getNiveau()) : "N/A");
                
                // Set status with appropriate styling
                statusLabel.setText(user.getStatut() ? "Actif" : "Inactif");
                statusLabel.getStyleClass().removeAll("status-active", "status-inactive");
                statusLabel.getStyleClass().add(user.getStatut() ? "status-active" : "status-inactive");
                
                // Set the content as this cell's graphic
                setGraphic(content);
            }
        }
    }
}
