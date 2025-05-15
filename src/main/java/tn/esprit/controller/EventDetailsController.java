package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.models.Evenement;
import tn.esprit.services.ServiceEvenement;

public class EventDetailsController {

    @FXML private Label eventNameLabel;
    @FXML private Label eventDescriptionLabel;
    @FXML private Label eventDateLabel;
    @FXML private Label eventTimeLabel;
    @FXML private Label eventLocationLabel;

    private Evenement event;
    private boolean isInitialized = false;

    // Default constructor required by FXMLLoader
    public EventDetailsController() {
    }

    // Called automatically by FXMLLoader after @FXML fields are injected
    @FXML
    private void initialize() {
        isInitialized = true;
        if (event != null) {
            displayEventDetails();
        }
    }

    // Called by the caller to provide the event object
    public void setEvent(Evenement event) {
        this.event = event;
        if (isInitialized) {
            displayEventDetails();
        }
    }

    // Displays the event details safely after fields are injected
    private void displayEventDetails() {
        if (event == null) return;

        eventNameLabel.setText(event.getNom());
        eventDescriptionLabel.setText(event.getDescription());
        eventDateLabel.setText("Date: " + event.getDateDebut().toLocalDateTime().toLocalDate());
        eventTimeLabel.setText("Heure: " + event.getDateDebut().toLocalDateTime().toLocalTime() + " - " +
                event.getDateFin().toLocalDateTime().toLocalTime());
        eventLocationLabel.setText("Lieu: " + event.getLieu().getNom() + " - " + event.getLieu().getAdresse());
    }

    // Handles "Add to My Events" button click
    @FXML
    private void addToMyEvents() {
        if (event != null) {
            ServiceEvenement serviceEvenement = new ServiceEvenement();
            serviceEvenement.addEventToUser(event.getEvenementId());
            System.out.println("Événement ajouté à votre liste: " + event.getNom());
        }
    }

    // Handles "Go Back" button click
    @FXML
    private void goBack() {
        System.out.println(Router.getStages().hashCode());
        System.out.println(Router.getStage().hashCode());
        Router.getStages().peek().close();
    }
}
