package tn.esprit.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tn.esprit.models.Evenement;
import tn.esprit.models.Lieu;
import tn.esprit.services.ServiceEvenement;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

// Add these imports
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.geometry.Insets; // For padding

public class MyEventsController {

    @FXML
    private VBox myEventsContainer;

    private Evenement selectedEvent; // Track the selected event
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    public void initialize() {
        loadMyEvents();
    }

    private void loadMyEvents() {
        List<Evenement> myEvents = serviceEvenement.getAll(); // Fetch all events
        displayMyEvents(myEvents);
    }

    private void displayMyEvents(List<Evenement> events) {
        myEventsContainer.getChildren().clear(); // Clear the container

        if (events.isEmpty()) {
            // Display a message when no events are found
            Label noEventsLabel = new Label("Aucun événement ajouté.");
            myEventsContainer.getChildren().add(noEventsLabel);

            // Add a "Retour" button to go back to UserEventsView
            Button backButton = new Button("Retour");
            backButton.setOnAction(e -> goBack()); // Link the button to the goBack method
            myEventsContainer.getChildren().add(backButton);
            return;
        }

        for (Evenement event : events) {
            // Create a VBox to hold the event details
            VBox eventCard = new VBox(10); // 10 is the spacing between elements
            eventCard.setPadding(new Insets(15)); // Add padding

            // Format the date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dateDebut = dateFormat.format(event.getDateDebut());
            String dateFin = dateFormat.format(event.getDateFin());

            // Fetch the Lieu object
            Lieu lieu = event.getLieu();

            // Display event details
            Label nameLabel = new Label("Nom: " + event.getNom());
            Label descriptionLabel = new Label("Description: " + event.getDescription());
            Label dateDebutLabel = new Label("Date de début: " + dateDebut);
            Label dateFinLabel = new Label("Date de fin: " + dateFin);
            Label lieuLabel = new Label("Lieu: " + (lieu != null ? lieu.getNom() : "Inconnu"));

            // Add a button to remove the event from "My Events"
            Button removeButton = new Button("Supprimer de mes événements");
            removeButton.setOnAction(e -> {
                selectedEvent = event; // Set the selected event
                removeFromMyEvents(); // Call the remove method
            });

            // Add all elements to the card
            eventCard.getChildren().addAll(nameLabel, descriptionLabel, dateDebutLabel, dateFinLabel, lieuLabel, removeButton);

            // Add the card to the container
            myEventsContainer.getChildren().add(eventCard);
        }

        // Add a "Retour" button at the bottom (even if events are present)
        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> goBack()); // Link the button to the goBack method
        myEventsContainer.getChildren().add(backButton);
    }

    @FXML
    private void removeFromMyEvents() {
        if (selectedEvent != null) {
            // Create a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Voulez-vous vraiment supprimer cet événement ?");
            alert.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Logic to remove the event from the user's "My Events" list
                System.out.println("Événement supprimé de mes événements: " + selectedEvent.getNom());

                // Remove the event from the database (or local list)
                serviceEvenement.delete(selectedEvent);

                // Refresh the list
                loadMyEvents();
            }
        } else {
            System.out.println("Aucun événement sélectionné.");
        }
    }

    @FXML
    private void goBack() {
        try {
            // Load the FXML file for the previous view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserEventsView.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) myEventsContainer.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}