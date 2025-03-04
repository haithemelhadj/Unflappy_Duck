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

    public void setEvent(Evenement event) {
        this.event = event;
        displayEventDetails();
    }

    private void displayEventDetails() {
        eventNameLabel.setText(event.getNom());
        eventDescriptionLabel.setText(event.getDescription());
        eventDateLabel.setText("Date: " + event.getDateDebut().toLocalDateTime().toLocalDate());
        eventTimeLabel.setText("Heure: " + event.getDateDebut().toLocalDateTime().toLocalTime() + " - " + event.getDateFin().toLocalDateTime().toLocalTime());
        eventLocationLabel.setText("Lieu: " + event.getLieu().getNom() + " - " + event.getLieu().getAdresse());
    }

    @FXML
    private void addToMyEvents() {
        if (event != null) {
            ServiceEvenement serviceEvenement = new ServiceEvenement();
            serviceEvenement.addEventToUser(event.getEvenementId());
            System.out.println("Événement ajouté à votre liste: " + event.getNom());
        }
    }

    @FXML
    private void goBack() {
        System.out.println(Router.getStages().hashCode());
        System.out.println(Router.getStage().hashCode());
        Router.getStages().peek().close();
    }
}