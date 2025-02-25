package tn.esprit.controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
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
    @FXML private Label eventCalendarLabel;

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
        eventCalendarLabel.setText("Calendrier: " + event.getCalendrierId());
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
        try {
            // Load the FXML file for the previous view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionEvenement/UserEventsView.fxml")); // Adjust the path as needed
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) eventNameLabel.getScene().getWindow();

            // Set the new scene on the existing stage
            stage.setScene(new Scene(root));

            // Optionally, you can set the title of the window
            stage.setTitle("Mes Événements");

            // Show the stage (it's already visible, so this just updates the content)
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load the FXML file: " + e.getMessage());
        }
    }
}