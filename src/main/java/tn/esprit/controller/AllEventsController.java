package tn.esprit.controller;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.models.Evenement;
import tn.esprit.models.Lieu;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceLieu;

import java.io.IOException;
import java.util.List;

public class AllEventsController {

    @FXML private TilePane eventsGridContainer; // Changed from VBox to TilePane

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final ServiceLieu serviceLieu = new ServiceLieu();

    @FXML
    public void initialize() {
        displayEvents();
    }

    @FXML
    private void goBack() throws IOException {
        Router.borderPaneCenterInsert(Router.getActiveBorderPane(), "/FrontOffice/GestionEvenement/event_form.fxml");
    }

    @FXML
    private void displayEvents() {
        eventsGridContainer.getChildren().clear();  // Clear the container before adding new content

        List<Evenement> events = serviceEvenement.getAll();  // Fetch all events

        // If no events found, show a message
        if (events.isEmpty()) {
            eventsGridContainer.getChildren().add(new Label("Aucun événement trouvé"));
            return; // Exit the method if there are no events
        }

        // Loop through all events and display them
        for (Evenement event : events) {
            // Create a VBox to hold event details and buttons
            VBox eventBox = new VBox(10); // 10 is the spacing between elements
            eventBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-padding: 10;");

            // Get event details
            String eventDetails = "Nom: " + event.getNom() +
                    "\nDescription: " + event.getDescription() +
                    "\nDate: " + event.getDateDebut().toLocalDateTime().toLocalDate() +
                    "\nHeure de début: " + event.getDateDebut().toLocalDateTime().toLocalTime() +
                    "\nHeure de fin: " + event.getDateFin().toLocalDateTime().toLocalTime();

            // Retrieve the Lieu (location) details
            Lieu lieu = serviceLieu.getById(event.getLieuId()); // Fetch the Lieu object using the lieuId

            if (lieu != null) {
                eventDetails += "\nLieu: " + lieu.getNom() + " - " + lieu.getAdresse() + " (Capacité: " + lieu.getCapacite() + ")";
            } else {
                eventDetails += "\nLieu: Inconnu"; // If Lieu is null, display "Inconnu"
            }

            // Create a label to show the event details
            Label eventLabel = new Label(eventDetails);

            // Create an HBox to hold the buttons
            HBox buttonBox = new HBox(10); // 10 is the spacing between buttons
            buttonBox.setAlignment(Pos.CENTER); // Center the buttons horizontally

            // Add the update button for modifying the event
            Button updateButton = new Button("Modifier");
            updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            updateButton.setOnAction(e -> updateEvent(event));

            // Add the delete button for removing the event
            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            deleteButton.setOnAction(e -> deleteEvent(event));

            // Add the buttons to the HBox
            buttonBox.getChildren().addAll(updateButton, deleteButton);

            // Add the event label and button HBox to the VBox
            eventBox.getChildren().addAll(eventLabel, buttonBox);

            // Add the VBox to the eventsGridContainer
            eventsGridContainer.getChildren().add(eventBox);
        }
    }

    // Method to refresh the list of events after any operation (e.g., delete or update)
    public void refreshEvents() {
        displayEvents(); // Call displayEvents again to refresh the list
    }

    // Method to handle event update
    private void updateEvent(Evenement event) {
        try {
            // Load the update form for the selected event
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionEvenement/update_event_form.fxml"));
            Parent root = loader.load();

            // Get the controller for the update form and pass the selected event
            UpdateEventController updateEventController = loader.getController();
            updateEventController.setEvent(event); // Pass the selected event to the update form

            // Create a new stage for the update form
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'événement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle event deletion
    private void deleteEvent(Evenement event) {
        // Delete the event and refresh the list
        serviceEvenement.delete(event);
        System.out.println("Événement supprimé: " + event.getNom());
        displayEvents(); // Refresh the list after deletion
    }
}