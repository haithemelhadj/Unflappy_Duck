package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tn.esprit.models.Evenement;
import tn.esprit.services.ServiceEvenement;

import java.sql.SQLException;
import java.util.List;

public class AdminDashboardController {

    @FXML
    private GridPane gridPane;

    private final ServiceEvenement serviceEvent = new ServiceEvenement();

    @FXML
    public void initialize() {
        List<Evenement> events = serviceEvent.getAll();
        addEventsToGrid(events);
    }

    // Dynamically add event cards to grid
    private void addEventsToGrid(List<Evenement> events) {
        for (int i = 0; i < events.size(); i++) {
            Evenement event = events.get(i);

            // Create a card with event name and description
            VBox card = createCard(event.getNom(), event.getDescription());
            // Add Edit and Delete buttons to the card
            addCardButtons(card, event.getEvenementId(), "event");
            // Positioning cards in a grid format (3 columns per row)
            gridPane.add(card, i % 3, i / 3);
        }
    }

    // Create a card for each event with title and description
    private VBox createCard(String title, String description) {
        VBox card = new VBox();
        card.getStyleClass().add("card");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("card-description");

        card.getChildren().addAll(titleLabel, descriptionLabel);
        return card;
    }

    // Add Edit and Delete buttons to each card
    private void addCardButtons(VBox card, int entityId, String entityType) {
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        editButton.getStyleClass().add("edit-button");
        deleteButton.getStyleClass().add("delete-button");

        editButton.setOnAction(event -> handleEdit(entityId, entityType));
        deleteButton.setOnAction(event -> handleDelete(entityId, entityType));

        card.getChildren().addAll(editButton, deleteButton);
    }

    // Handle edit button click
    private void handleEdit(int entityId, String entityType) {
        System.out.println("Editing " + entityType + " with ID: " + entityId);
        // Implement edit logic here (could involve opening a new window/form)
    }

    // Handle delete button click
    private void handleDelete(int entityId, String entityType) {
        System.out.println("Deleting " + entityType + " with ID: " + entityId);
        // Call the service to delete the event from the database
        //serviceEvent.delete(entity.evenementId);
        System.out.println(entityType + " with ID " + entityId + " deleted successfully.");
        // Optionally refresh the UI to remove the deleted card
    }
}
