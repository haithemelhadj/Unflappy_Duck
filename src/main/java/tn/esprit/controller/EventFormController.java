package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Evenement;
import tn.esprit.models.Lieu;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceLieu;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventFormController {

    @FXML private TextField eventNameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private DatePicker calendarDatePicker;

    @FXML private TextField newLocationField;
    @FXML private TextField addressField;
    @FXML private TextField capacityField;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final ServiceLieu serviceLieu = new ServiceLieu();

    @FXML
    public void initialize() {
        // No need to load locations into a ComboBox anymore
    }

    @FXML
    private void saveEvent() {
        String eventName = eventNameField.getText();
        String description = descriptionField.getText();
        String startTimeText = startTimeField.getText();
        String endTimeText = endTimeField.getText();
        LocalDate calendarDate = calendarDatePicker.getValue();

        String lieuName = newLocationField.getText();
        String lieuAddress = addressField.getText();
        String lieuCapacityStr = capacityField.getText();

        // Validate fields
        if (eventName.isEmpty() || description.isEmpty() || startTimeText.isEmpty() || endTimeText.isEmpty() || calendarDate == null ||
                lieuName.isEmpty() || lieuAddress.isEmpty() || lieuCapacityStr.isEmpty()) {
            showAlert("Erreur de validation", "Veuillez remplir tous les champs.");
            return;
        }

        // Validate time format (HH:mm)
        if (!startTimeText.matches("\\d{2}:\\d{2}") || !endTimeText.matches("\\d{2}:\\d{2}")) {
            showAlert("Erreur de validation", "L'heure doit être au format HH:MM.");
            return;
        }

        // Parse times
        String[] startTimeParts = startTimeText.split(":");
        String[] endTimeParts = endTimeText.split(":");

        int startHour = Integer.parseInt(startTimeParts[0]);
        int startMinute = Integer.parseInt(startTimeParts[1]);
        int endHour = Integer.parseInt(endTimeParts[0]);
        int endMinute = Integer.parseInt(endTimeParts[1]);

        LocalTime startLocalTime = LocalTime.of(startHour, startMinute);
        LocalTime endLocalTime = LocalTime.of(endHour, endMinute);

        // Convert LocalTime to Timestamp
        Timestamp startTimestamp = Timestamp.valueOf(calendarDate.atTime(startLocalTime));
        Timestamp endTimestamp = Timestamp.valueOf(calendarDate.atTime(endLocalTime));

        // Create Evenement object
        Evenement evenement = new Evenement();
        evenement.setNom(eventName);
        evenement.setDescription(description);
        evenement.setDateDebut(startTimestamp);
        evenement.setDateFin(endTimestamp);

        // Handle Lieu (location)
        int lieuId = handleLieu(lieuName, lieuAddress, lieuCapacityStr);
        if (lieuId == -1) {
            showAlert("Erreur", "Impossible de sauvegarder le lieu.");
            return;
        }
        evenement.setLieuId(lieuId);

        // Generate calendar ID (for example, current time-based ID)
        int calendarId = (int) (System.currentTimeMillis() / 1000);
        evenement.setCalendrierId(calendarId); // Placeholder for calendar-related info
        evenement.setCreateurEvenement(1); // Assuming creator is user with ID 1

        // Save event
        serviceEvenement.add(evenement);

        // Show success message and clear fields
        showAlert("Succès", "Événement enregistré avec succès!");
        clearFields();
    }

    private int handleLieu(String nom, String adresse, String capaciteStr) {
        try {
            int capacite = Integer.parseInt(capaciteStr); // Validate that capacity is a number

            // Check if the Lieu already exists in the database
            Lieu existingLieu = serviceLieu.getByNameAndAddress(nom, adresse);
            if (existingLieu != null) {
                return existingLieu.getLieuId(); // Return the existing Lieu ID
            }

            // If the Lieu doesn't exist, create a new one
            Lieu newLieu = new Lieu(0, nom, adresse, capacite);
            serviceLieu.add(newLieu);
            return newLieu.getLieuId(); // Return the new Lieu ID
        } catch (NumberFormatException e) {
            showAlert("Erreur de validation", "La capacité doit être un nombre.");
            return -1;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        eventNameField.clear();
        descriptionField.clear();
        startTimeField.clear();
        endTimeField.clear();
        calendarDatePicker.setValue(null);
        newLocationField.clear();
        addressField.clear();
        capacityField.clear();
    }

    @FXML
    private void goToAllEvents(ActionEvent event) {
        try {
            // Load the all_events.fxml interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/all_events.fxml"));
            Parent newroot = loader.load();
            Scene scene = new Scene(newroot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToUserInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserEventsView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) eventNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}