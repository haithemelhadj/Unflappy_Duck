package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import tn.esprit.models.Tache;

public class TacheCard {
    
    @FXML
    private VBox taskCard;
    
    @FXML
    private Circle statusIndicator;
    
    @FXML
    private Label taskTitle;
    
    @FXML
    private Label taskId;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label taskDescription;
    
    @FXML
    private Label teamLabel;
    
    @FXML
    private Label assigneeLabel;
    
    @FXML
    private Button editButton;
    
    @FXML
    private Button deleteButton;
    
    private Tache tache;
    private GestionTache parentController;
    
    /**
     * Sets the data for the task card
     * @param tache The task to display
     * @param teamName The name of the team
     * @param assigneeName The name of the assigned member
     * @param parentController Reference to the parent controller for callbacks
     */
    public void setData(Tache tache, String teamName, String assigneeName, GestionTache parentController) {
        this.tache = tache;
        this.parentController = parentController;
        
        taskTitle.setText(tache.getTitre());
        taskId.setText("ID: " + tache.getId());
        taskDescription.setText(tache.getDescription());
        teamLabel.setText("Équipe: " + teamName);
        assigneeLabel.setText("Assigné à: " + assigneeName);
        
        // Set status label and indicator
        statusLabel.setText(tache.getStatut());
        setStatusStyle(tache.getStatut());
    }
    
    /**
     * Sets the appropriate style classes based on task status
     * @param status The status of the task
     */
    private void setStatusStyle(String status) {
        // Remove all status classes
        statusLabel.getStyleClass().removeAll(
            "status-todo", 
            "status-inprogress", 
            "status-completed", 
            "status-blocked",
            "status-proposed"
        );
        
        statusIndicator.getStyleClass().removeAll(
            "status-indicator-todo", 
            "status-indicator-inprogress", 
            "status-indicator-completed", 
            "status-indicator-blocked",
            "status-indicator-proposed"
        );
        
        // Add appropriate class based on status
        switch(status.toUpperCase()) {
            case "À FAIRE":
                statusLabel.getStyleClass().add("status-todo");
                statusIndicator.getStyleClass().add("status-indicator-todo");
                break;
            case "EN COURS":
                statusLabel.getStyleClass().add("status-inprogress");
                statusIndicator.getStyleClass().add("status-indicator-inprogress");
                break;
            case "TERMINÉE":
                statusLabel.getStyleClass().add("status-completed");
                statusIndicator.getStyleClass().add("status-indicator-completed");
                break;
            case "BLOQUÉE":
                statusLabel.getStyleClass().add("status-blocked");
                statusIndicator.getStyleClass().add("status-indicator-blocked");
                break;
            case "PROPOSÉE":
                statusLabel.getStyleClass().add("status-proposed");
                statusIndicator.getStyleClass().add("status-indicator-proposed");
                break;
            default:
                statusLabel.getStyleClass().add("status-todo");
                statusIndicator.getStyleClass().add("status-indicator-todo");
                break;
        }
    }
    
    /**
     * Edit button click handler
     */
    @FXML
    void editTask() {
        if (parentController != null) {
            parentController.loadTaskForEdit(tache);
        }
    }
    
    /**
     * Delete button click handler
     */
    @FXML
    void deleteTask() {
        if (parentController != null) {
            parentController.confirmDeleteTask(tache);
        }
    }
}
