package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tn.esprit.models.Equipe;
import tn.esprit.models.MembresEquipe;
import tn.esprit.models.Tache;
//import tn.esprit.models.enums.Statut;
import tn.esprit.services.ServiceTache;
//choice box
import tn.esprit.utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class GestionTache implements Initializable {

    @FXML
    private Label Error;

    @FXML
    private ListView<String> ListViewTaches;

    @FXML
    private TextField RechercheTache;

    @FXML
    private TextArea description;

    @FXML
    private ComboBox<String> equipe_id;

    @FXML
    private TextField idfield;

    @FXML
    private ComboBox<String> membre_id;

    @FXML
    private Button sortTache;

    @FXML
    private ComboBox<String> tacheSort;

    @FXML
    private TextField titre;
    
    @FXML
    private VBox tasksContainer;
    
    @FXML
    private Button searchTaskBtn;
    
    // Radio buttons for status
    @FXML
    private RadioButton statusAFaire;
    
    @FXML
    private RadioButton statusEnCours;
    
    @FXML
    private RadioButton statusTerminee;
    
    @FXML
    private RadioButton statusBloquee;
    
    @FXML
    private RadioButton statusProposee;
    
    @FXML
    private ToggleGroup statusGroup;
    
    // Filter toggle buttons
    @FXML
    private ToggleButton filterAll;
    
    @FXML
    private ToggleButton filterTodo;
    
    @FXML
    private ToggleButton filterInProgress;
    
    @FXML
    private ToggleButton filterCompleted;
    
    @FXML
    private ToggleButton filterBlocked;
    
    @FXML
    private ToggleGroup filterGroup;

    //region init
    ServiceTache st = new ServiceTache();
    Connection cnx = MyDatabase.getInstance().getCnx();
    ObservableList<String> observableTacheList = FXCollections.observableArrayList();
    FilteredList<String> filteredTacheData;
    // Map to store team and member names by ID for quick lookup
    private Map<Integer, String> teamNamesById = new HashMap<>();
    private Map<Integer, String> memberNamesById = new HashMap<>();

    //
    private void loadEquipeChoiceBoxData(ComboBox<String> comboBox) {
        comboBox.getItems().clear();
        teamNamesById.clear();
        
        String qry ="SELECT * FROM `equipe`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("nom");
                comboBox.getItems().add(id + "-" + name);
                teamNamesById.put(id, name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showError(e.getMessage());
        }
    }
    
    private void loadMembreChoiceBoxData(ComboBox<String> comboBox) {
        comboBox.getItems().clear();
        memberNamesById.clear();
        
        String qry ="SELECT * FROM membre_equipe m JOIN utilisateur u ON m.utilisateur_id = u.id WHERE m.role = 'membre'";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                int id = rs.getInt("utilisateur_id");
                String name = rs.getString("nom");
                comboBox.getItems().add(id + "-" + name);
                memberNamesById.put(id, name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showError(e.getMessage());
        }
    }
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize ComboBoxes
        loadEquipeChoiceBoxData(equipe_id);
        loadMembreChoiceBoxData(membre_id);

        // Initialize sort options
        tacheSort.getItems().addAll("Trier par ID", "Trier par Nom", "Trier par Statut");
        tacheSort.setValue("Trier par ID");
        
        // Setup filter toggle group
        setupFilterGroup();
        
        // Initialize search and filter for list view (legacy)
        loadTacheData(observableTacheList);
        filteredTacheData = new FilteredList<>(observableTacheList, s -> true);
        ListViewTaches.setItems(filteredTacheData);
        RechercheTache.textProperty().addListener((observable, oldValue, newValue) -> {
            applyTacheFilter(newValue);
        });
        
        // Load tasks into the new container
        refreshTaskCards();
    }
    
    private void setupFilterGroup() {
        // Configure toggle buttons for filtering
        filterGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                applyStatusFilter();
            } else {
                // If all toggles are deselected, select "All" by default
                filterAll.setSelected(true);
            }
        });
        
        // Select "All" by default
        filterAll.setSelected(true);
    }

    /**
     * Creates and loads a new task
     */
    private void add() {
        try {
            // Get selected team ID
            String[] eqpParts = equipe_id.getValue().split("-", 2);
            int idEquipe = Integer.parseInt(eqpParts[0]);
            
            // Get selected member ID
            String[] mbmrParts = membre_id.getValue().split("-", 2);
            int idMembr = Integer.parseInt(mbmrParts[0]);
            
            // Get selected status
            String status = getSelectedStatus();
            
            // Create and add the task
            Tache newTask = new Tache(
                    idEquipe,
                    titre.getText(),
                    description.getText(),
                    idMembr,
                    status
            );
            
            st.add(newTask);
            
            // Show success message and refresh
            showSuccess("Tâche ajoutée avec succès");
            clearForm();
            refreshTaskCards();
            
        } catch(Exception e) {
            System.out.println("Error adding task: " + e.getMessage());
            showError("Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    /**
     * Updates an existing task
     */
    private void update(int id) {
        try {
            // Get selected team ID
            String[] eqpParts = equipe_id.getValue().split("-", 2);
            int idEquipe = Integer.parseInt(eqpParts[0]);
            
            // Get selected member ID
            String[] mbmrParts = membre_id.getValue().split("-", 2);
            int idMembr = Integer.parseInt(mbmrParts[0]);
            
            // Get selected status
            String status = getSelectedStatus();
            
            // Create and update the task
            Tache updatedTask = new Tache(
                    id,
                    idEquipe,
                    titre.getText(),
                    description.getText(),
                    idMembr,
                    status
            );
            
            st.update(updatedTask);
            
            // Show success message and refresh
            showSuccess("Tâche mise à jour avec succès");
            clearForm();
            refreshTaskCards();
            
        } catch(Exception e) {
            System.out.println("Error updating task: " + e.getMessage());
            showError("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    /**
     * Gets the selected status from radio buttons
     */
    private String getSelectedStatus() {
        RadioButton selectedRadioButton = (RadioButton) statusGroup.getSelectedToggle();
        String status = "À faire"; // Default status
        
        if (selectedRadioButton != null) {
            status = selectedRadioButton.getText();
        }
        
        return status;
    }
    
    /**
     * Sets the status radio button based on the provided status string
     */
    private void setStatusRadioButton(String status) {
        if (status == null) {
            statusAFaire.setSelected(true);
            return;
        }
        
        switch (status.toUpperCase()) {
            case "À FAIRE":
                statusAFaire.setSelected(true);
                break;
            case "EN COURS":
                statusEnCours.setSelected(true);
                break;
            case "TERMINÉE":
                statusTerminee.setSelected(true);
                break;
            case "BLOQUÉE":
                statusBloquee.setSelected(true);
                break;
            case "PROPOSÉE":
                statusProposee.setSelected(true);
                break;
            default:
                statusAFaire.setSelected(true);
                break;
        }
    }

    /**
     * Retrieves a task by ID from the database
     */
    private List<Tache> getTacheById(int id) {
        List<Tache> taches = new ArrayList<>();
        String qry="SELECT * FROM `tache` WHERE `id` = "+ id;
        try{
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()){
                Tache p = new Tache();
                p.setId(rs.getInt(1));
                p.setEquipe_id(rs.getInt(2));
                p.setTitre(rs.getString(3));
                p.setDescription(rs.getString(4));
                p.setId_responsable(rs.getInt(5));
                p.setStatut(rs.getString(6));

                taches.add(p);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            showError(e.getMessage());
        }
        return taches;
    }

    /**
     * Handle add/update button action
     */
    @FXML
    void AddUpdate(ActionEvent event) {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        if(idfield.getText().isEmpty()) {
            // No ID specified, add new task
            add();
        } else {
            try {
                // ID specified, update existing task
                int id = Integer.parseInt(idfield.getText());
                update(id);
            } catch (NumberFormatException e) {
                showError("ID de tâche invalide");
            }
        }
    }
    
    /**
     * Validates the form before saving
     */
    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (titre.getText() == null || titre.getText().trim().isEmpty()) {
            errorMessage.append("Le titre est requis.\n");
        }
        
        if (description.getText() == null || description.getText().trim().isEmpty()) {
            errorMessage.append("La description est requise.\n");
        }
        
        if (equipe_id.getValue() == null) {
            errorMessage.append("Veuillez sélectionner une équipe.\n");
        }
        
        if (membre_id.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un membre responsable.\n");
        }
        
        if (errorMessage.length() > 0) {
            showError(errorMessage.toString());
            return false;
        }
        
        return true;
    }

    /**
     * Handle delete button action
     */
    @FXML
    void delete(ActionEvent event) {
        if (idfield.getText().isEmpty()) {
            showError("Veuillez spécifier l'ID de la tâche à supprimer");
            return;
        }
        
        try {
            int id = Integer.parseInt(idfield.getText());
            List<Tache> taches = getTacheById(id);
            
            if (taches.isEmpty()) {
                showError("Aucune tâche trouvée avec cet ID");
                return;
            }
            
            if (taches.size() == 1) {
                // Confirm deletion with a dialog
                confirmDeleteTask(taches.get(0));
            } else {
                showError("Plusieurs tâches trouvées avec cet ID");
            }
        } catch (NumberFormatException e) {
            showError("ID de tâche invalide");
        }
    }
    
    /**
     * Shows confirmation dialog for task deletion
     */
    public void confirmDeleteTask(Tache tache) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette tâche?");
        alert.setContentText("Titre: " + tache.getTitre() + "\nID: " + tache.getId());
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                st.delete(tache);
                showSuccess("Tâche supprimée avec succès");
                clearForm();
                refreshTaskCards();
            } catch (Exception e) {
                showError("Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }
    
    /**
     * Load a task into the form for editing
     */
    public void loadTaskForEdit(Tache tache) {
        idfield.setText(String.valueOf(tache.getId()));
        titre.setText(tache.getTitre());
        description.setText(tache.getDescription());
        
        // Set combo box selections
        for (String option : equipe_id.getItems()) {
            if (option.startsWith(tache.getEquipe_id() + "-")) {
                equipe_id.setValue(option);
                break;
            }
        }
        
        for (String option : membre_id.getItems()) {
            if (option.startsWith(tache.getId_responsable() + "-")) {
                membre_id.setValue(option);
                break;
            }
        }
        
        // Set status
        setStatusRadioButton(tache.getStatut());
    }
    
    /**
     * Clear form fields
     */
    @FXML
    void clearForm() {
        idfield.clear();
        titre.clear();
        description.clear();
        equipe_id.setValue(null);
        membre_id.setValue(null);
        statusAFaire.setSelected(true);
        Error.setText("");
    }
    
    /**
     * Search for a task by ID and load it into the form
     */
    @FXML
    void searchTask() {
        if (idfield.getText().isEmpty()) {
            showError("Veuillez entrer un ID de tâche à rechercher");
            return;
        }
        
        try {
            int id = Integer.parseInt(idfield.getText());
            List<Tache> taches = getTacheById(id);
            
            if (taches.isEmpty()) {
                showError("Aucune tâche trouvée avec cet ID");
                return;
            }
            
            if (taches.size() == 1) {
                loadTaskForEdit(taches.get(0));
            } else {
                showError("Plusieurs tâches trouvées avec cet ID");
            }
        } catch (NumberFormatException e) {
            showError("ID de tâche invalide");
        }
    }

    /**
     * Refresh task cards in the container
     */
    private void refreshTaskCards() {
        tasksContainer.getChildren().clear();
        
        try {
            List<Tache> taches = st.getAll();
            
            // Apply status filter if necessary
            if (filterGroup.getSelectedToggle() != filterAll) {
                taches = filterTachesByStatus(taches);
            }
            
            // Apply search filter if text is entered
            String searchText = RechercheTache.getText();
            if (searchText != null && !searchText.isEmpty()) {
                taches = taches.stream()
                    .filter(t -> t.getTitre().toLowerCase().contains(searchText.toLowerCase()) || 
                                 t.getDescription().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            
            // Sort if needed
            sortTachesList(taches);
            
            // Create cards for each task
            for (Tache tache : taches) {
                try {
                    FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/BackOffice/GestionEquipe/TacheCard.fxml")
                    );
                    VBox taskCardNode = loader.load();
                    TacheCard cardController = loader.getController();
                    
                    // Get team and member names
                    String teamName = teamNamesById.getOrDefault(tache.getEquipe_id(), "Équipe #" + tache.getEquipe_id());
                    String memberName = memberNamesById.getOrDefault(tache.getId_responsable(), "Membre #" + tache.getId_responsable());
                    
                    // Set data for the card
                    cardController.setData(tache, teamName, memberName, this);
                    
                    // Add to container
                    tasksContainer.getChildren().add(taskCardNode);
                    
                } catch (IOException e) {
                    System.out.println("Error loading task card: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Show message if no tasks found
            if (taches.isEmpty()) {
                Label noTasksLabel = new Label("Aucune tâche trouvée");
                noTasksLabel.getStyleClass().add("no-tasks-label");
                tasksContainer.getChildren().add(noTasksLabel);
            }
            
        } catch (Exception e) {
            System.out.println("Error refreshing task cards: " + e.getMessage());
            showError("Erreur lors du chargement des tâches: " + e.getMessage());
        }
    }
    
    /**
     * Filter tasks by the selected status
     */
    private List<Tache> filterTachesByStatus(List<Tache> taches) {
        ToggleButton selectedFilter = (ToggleButton) filterGroup.getSelectedToggle();
        
        if (selectedFilter == null || selectedFilter == filterAll) {
            return taches;
        }
        
        String statusFilter;
        
        if (selectedFilter == filterTodo) {
            statusFilter = "À FAIRE";
        } else if (selectedFilter == filterInProgress) {
            statusFilter = "EN COURS";
        } else if (selectedFilter == filterCompleted) {
            statusFilter = "TERMINÉE";
        } else if (selectedFilter == filterBlocked) {
            statusFilter = "BLOQUÉE";
        } else {
            return taches; // Default to no filtering
        }
        
        return taches.stream()
            .filter(t -> t.getStatut().equalsIgnoreCase(statusFilter))
            .toList();
    }
    
    /**
     * Apply status filter based on selected toggle button
     */
    private void applyStatusFilter() {
        refreshTaskCards();
    }

    /**
     * Refresh button action handler
     */
    @FXML
    void refrech(ActionEvent event) {
        refreshLegacyListView();
        refreshTaskCards();
    }

    //region legacy list view methods
    private void loadTacheData(ObservableList<String> list) {
        try {
            List<Tache> taches = st.getAll();
            for (Tache t : taches) {
                observableTacheList.add(t.getId() + " | " + t.getTitre() + " | " + t.getStatut() + " | ");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showError(e.getMessage());
        }
    }
    
    public void refreshLegacyListView() {
        observableTacheList.clear();
        loadTacheData(observableTacheList);
    }
    
    private void applyTacheFilter(String filterText) {
        filteredTacheData.setPredicate(item -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = filterText.toLowerCase();
            return item.toLowerCase().contains(lowerCaseFilter);
        });
        
        // Also refresh the task cards
        refreshTaskCards();
    }
    //endregion

    /**
     * Sort button action handler
     */
    @FXML
    void sortTache(ActionEvent event) {
        refreshTaskCards();
    }
    
    /**
     * Sort tasks based on selected criteria
     */
    private void sortTachesList(List<Tache> taches) {
        String selectedOption = tacheSort.getValue();
        
        if (selectedOption == null) return;
        
        switch (selectedOption) {
            case "Trier par ID":
                taches.sort(Comparator.comparingInt(Tache::getId));
                break;
                
            case "Trier par Nom":
                taches.sort(Comparator.comparing(Tache::getTitre));
                break;
                
            case "Trier par Statut":
                taches.sort(Comparator.comparing(Tache::getStatut));
                break;
                
            default:
                // No sorting
                break;
        }
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
        Error.setText(message);
        Error.getStyleClass().add("error-text");
    }
    
    /**
     * Show success message
     */
    private void showSuccess(String message) {
        Error.setText(message);
        Error.getStyleClass().removeAll("error-text");
        Error.getStyleClass().add("success-text");
    }
}
