package tn.esprit.controller;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Evenement;
import tn.esprit.services.ServiceEvenement;

import java.io.IOException;
import java.util.List;

public class UserEventsController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private VBox eventsListContainer;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    public void initialize() {
        loadEvents(); // Load all events initially
        setupSearchAndSort(); // Set up search and sort functionality
    }

    private void loadEvents() {
        List<Evenement> events = serviceEvenement.getAll();
        displayEvents(events);
    }

    private void displayEvents(List<Evenement> events) {
        eventsListContainer.getChildren().clear(); // Clear the container

        if (events.isEmpty()) {
            Label noResultsLabel = new Label("Aucun événement trouvé avec ce nom.");
            noResultsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");
            eventsListContainer.getChildren().add(noResultsLabel);
            return;
        }

        for (Evenement event : events) {
            // Create a card for each event
            VBox card = new VBox();
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-spacing: 5;");
            card.setPrefWidth(400); // Set card width

            // Event Name
            Label nameLabel = new Label(event.getNom());
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            // Event Description
            Label descriptionLabel = new Label(event.getDescription());
            descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

            // Event Calendar (Date)
            Label dateLabel = new Label("Date: " + event.getDateDebut().toLocalDateTime().toLocalDate());
            dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

            // Add components to the card
            card.getChildren().addAll(nameLabel, descriptionLabel, dateLabel);

            // Add click event to show details
            card.setOnMouseClicked(e -> showEventDetails(event));

            // Add the card to the container
            eventsListContainer.getChildren().add(card);
        }
    }

    private void showEventDetails(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionEvenement/EventDetails.fxml"));
            Parent root = loader.load();

            EventDetailsController controller = loader.getController();
            controller.setEvent(event);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'événement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setupSearchAndSort() {
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Evenement> filteredEvents = serviceEvenement.searchEvents(newValue);
            displayEvents(filteredEvents);
        });

        // Sort functionality (always sort by name)
        List<Evenement> sortedEvents = serviceEvenement.sortEvents("Nom");
        displayEvents(sortedEvents);
    }

    @FXML
    private void goToMyEvents() throws IOException {
        Router.newWindow("/FrontOffice/GestionEvenement/MyEvents.fxml");
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionEvenement/MyEvents.fxml"));
//            Parent root = loader.load();
//
//            Stage stage = (Stage) eventsListContainer.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}