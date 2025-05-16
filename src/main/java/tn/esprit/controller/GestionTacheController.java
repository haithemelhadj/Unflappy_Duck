package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Tache;
import tn.esprit.services.ServiceTache;
import tn.esprit.utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GestionTacheController implements Initializable {

    @FXML
    private Label Error;

    @FXML
    private TextField RechercheTache;

    @FXML
    private ListView<Tache> ListViewTaches;

    // Service instances
    private final ServiceTache serviceTache = new ServiceTache();
    private final Connection cnx = MyDatabase.getInstance().getCnx();
    
    // Data collections
    private ObservableList<Tache> tachesList = FXCollections.observableArrayList();
    private FilteredList<Tache> filteredTaches;
    
    // Maps for team and member names by ID
    private Map<Integer, String> teamNamesById = new HashMap<>();
    private Map<Integer, String> memberNamesById = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load team and member data
        loadTeamData();
        loadMemberData();
        
        // Initialize the task list
        loadTasks();
        
        // Setup search functionality
        setupSearch();
    }
    
    /**
     * Loads team data from database for reference
     */
    private void loadTeamData() {
        teamNamesById.clear();
        String query = "SELECT id, nom FROM equipe";
        
        try {
            Statement stmt = cnx.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nom");
                teamNamesById.put(id, name);
            }
        } catch (SQLException e) {
            showError("Erreur lors du chargement des équipes: " + e.getMessage());
        }
    }
    
    /**
     * Loads member data from database for reference
     */
    private void loadMemberData() {
        memberNamesById.clear();
        String query = "SELECT u.id, u.nom, u.prenom FROM utilisateur u JOIN membre_equipe m ON u.id = m.utilisateur_id";
        
        try {
            Statement stmt = cnx.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String fullName = rs.getString("prenom") + " " + rs.getString("nom");
                memberNamesById.put(id, fullName);
            }
        } catch (SQLException e) {
            showError("Erreur lors du chargement des membres: " + e.getMessage());
        }
    }
    
    /**
     * Loads all tasks and sets up the filtered list
     */
    private void loadTasks() {
        try {
            // Clear and reload tasks
            tachesList.clear();
            List<Tache> tasks = serviceTache.getAll();
            tachesList.addAll(tasks);
            
            // Setup filtered list
            filteredTaches = new FilteredList<>(tachesList, p -> true);
            
            // Populate custom list view
            populateTaskCards();
        } catch (Exception e) {
            showError("Erreur lors du chargement des tâches: " + e.getMessage());
        }
    }
    
    /**
     * Populates the task cards in the list view
     */
    private void populateTaskCards() {
        try {
            ListViewTaches.getItems().clear();
            
            // Create a custom cell factory to display task cards
            ListViewTaches.setCellFactory(lv -> new javafx.scene.control.ListCell<Tache>() {
                @Override
                protected void updateItem(Tache tache, boolean empty) {
                    super.updateItem(tache, empty);
                    
                    if (empty || tache == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        try {
                            // Load the task card FXML
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionEquipe/TacheCard.fxml"));
                            VBox taskCard = loader.load();
                            
                            // Get the controller and set the data
                            TacheCard controller = loader.getController();
                            String teamName = teamNamesById.getOrDefault(tache.getEquipe_id(), "Équipe inconnue");
                            String assigneeName = memberNamesById.getOrDefault(tache.getId_responsable(), "Membre inconnu");
                            
                            controller.setData(tache, teamName, assigneeName, null);
                            
                            setGraphic(taskCard);
                        } catch (IOException e) {
                            setText("Erreur d'affichage: " + e.getMessage());
                        }
                    }
                }
            });
            
            // Add all filtered tasks to the list view
            ListViewTaches.setItems(filteredTaches);
        } catch (Exception e) {
            showError("Erreur lors de l'affichage des tâches: " + e.getMessage());
        }
    }
    
    /**
     * Sets up the search functionality
     */
    private void setupSearch() {
        RechercheTache.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTaches.setPredicate(tache -> {
                // If search field is empty, show all tasks
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String searchText = newValue.toLowerCase();
                
                // Match against various task properties
                if (tache.getTitre().toLowerCase().contains(searchText)) {
                    return true;
                } else if (tache.getDescription().toLowerCase().contains(searchText)) {
                    return true;
                } else if (tache.getStatut().toLowerCase().contains(searchText)) {
                    return true;
                } else if (teamNamesById.getOrDefault(tache.getEquipe_id(), "").toLowerCase().contains(searchText)) {
                    return true;
                } else if (memberNamesById.getOrDefault(tache.getId_responsable(), "").toLowerCase().contains(searchText)) {
                    return true;
                }
                
                return false;
            });
            
            // Refresh the list view with filtered results
            populateTaskCards();
        });
    }
    
    /**
     * Refreshes all task data
     */
    public void refreshTasks() {
        loadTeamData();
        loadMemberData();
        loadTasks();
    }
    
    /**
     * Shows an error message
     */
    private void showError(String message) {
        Error.setText(message);
        Error.setVisible(true);
        
        // Hide the error after 5 seconds
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                javafx.application.Platform.runLater(() -> Error.setVisible(false));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
} 