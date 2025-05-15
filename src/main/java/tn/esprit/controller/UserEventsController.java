package tn.esprit.controller;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import tn.esprit.models.Evenement;
import tn.esprit.services.ServiceEvenement;

import java.io.IOException;
import java.util.List;

public class UserEventsController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private TilePane eventsListContainer;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    public void initialize() {
        setupSortComboBox();
        loadEvents();
        setupSearchAndSort();
    }

    private void setupSortComboBox() {
        sortComboBox.getItems().addAll("Name", "Date", "Location");
        sortComboBox.setValue("Name");
    }

    private void loadEvents() {
        List<Evenement> events = serviceEvenement.getAll();
        displayEvents(events);
    }

    private void displayEvents(List<Evenement> events) {
        eventsListContainer.getChildren().clear();

        if (events.isEmpty()) {
            Label noResultsLabel = new Label("No events found");
            noResultsLabel.getStyleClass().add("no-results-label");
            eventsListContainer.getChildren().add(noResultsLabel);
            return;
        }

        for (Evenement event : events) {
            VBox eventCard = createEventCard(event);
            eventsListContainer.getChildren().add(eventCard);
        }
    }

    private VBox createEventCard(Evenement event) {
        VBox card = new VBox(10);
        card.getStyleClass().add("event-card");
        card.setPadding(new Insets(15));
        card.setPrefWidth(300);
        card.setPrefHeight(250);

        // Event Name
        Label nameLabel = new Label(event.getNom());
        nameLabel.getStyleClass().add("event-name");

        // Event Description
        Label descriptionLabel = new Label(event.getDescription());
        descriptionLabel.getStyleClass().add("event-description");
        descriptionLabel.setWrapText(true);

        // Event Date
        Label dateLabel = new Label("Date: " + event.getDateDebut().toLocalDateTime().toLocalDate());
        dateLabel.getStyleClass().add("event-date");

        // Event Time
        Label timeLabel = new Label("Time: " + event.getDateDebut().toLocalDateTime().toLocalTime() + 
                                  " - " + event.getDateFin().toLocalDateTime().toLocalTime());
        timeLabel.getStyleClass().add("event-time");

        // Action Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button detailsButton = new Button("View Details");
        detailsButton.getStyleClass().add("action-button");
        detailsButton.setOnAction(e -> showEventDetails(event));

        Button addButton = new Button("Add to My Events");
        addButton.getStyleClass().add("action-button");
        addButton.setOnAction(e -> addToMyEvents(event));

        buttonBox.getChildren().addAll(detailsButton, addButton);

        card.getChildren().addAll(nameLabel, descriptionLabel, dateLabel, timeLabel, buttonBox);
        return card;
    }

    private void showEventDetails(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionEvenement/EventDetails.fxml"));
            Parent root = loader.load();

            EventDetailsController controller = loader.getController();
            controller.setEvent(event);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Event Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToMyEvents(Evenement event) {
        serviceEvenement.addEventToUser(event.getEvenementId());
        // Show confirmation
        showConfirmation("Event added to your list: " + event.getNom());
    }

    private void showConfirmation(String message) {
        // You can implement a nice confirmation dialog here
        System.out.println(message);
    }

    private void setupSearchAndSort() {
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Evenement> filteredEvents = serviceEvenement.searchEvents(newValue);
            displayEvents(filteredEvents);
        });

        // Sort functionality
        sortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            List<Evenement> sortedEvents = serviceEvenement.sortEvents(newValue);
            displayEvents(sortedEvents);
        });
    }

    @FXML
    private void goToMyEvents() throws IOException {
        Router.borderPaneCenterInsert(Router.getActiveBorderPane(), "/FrontOffice/GestionEvenement/MyEvents.fxml");
    }
}