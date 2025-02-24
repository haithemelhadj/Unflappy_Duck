package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Evenement;
import tn.esprit.models.Lieu;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceLieu;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class UpdateEventController {

    @FXML private TextField eventNameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private ComboBox<Lieu> locationComboBox;
    @FXML private DatePicker calendarDatePicker;

    // Fields for manual Lieu entry
    @FXML private TextField newLocationField;
    @FXML private TextField addressField;
    @FXML private TextField capacityField;
    @FXML private CheckBox manualLocationCheckBox;

    private Evenement event;
    private AllEventsController allEventsController;
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final ServiceLieu serviceLieu = new ServiceLieu();

    // Method to set the event to be updated
    public void setEvent(Evenement event) {
        this.event = event;
        loadEventDetails();
        initializeLocationComboBox();
    }

    // Method to set the AllEventsController reference
    public void setAllEventsController(AllEventsController allEventsController) {
        this.allEventsController = allEventsController;
    }

    // Load the event details into the form
    private void loadEventDetails() {
        eventNameField.setText(event.getNom());
        descriptionField.setText(event.getDescription());
        startTimeField.setText(event.getDateDebut().toLocalDateTime().toLocalTime().toString());
        endTimeField.setText(event.getDateFin().toLocalDateTime().toLocalTime().toString());
        calendarDatePicker.setValue(event.getDateDebut().toLocalDateTime().toLocalDate());

        // Set the selected location in the ComboBox
        Lieu selectedLieu = event.getLieu();
        if (selectedLieu != null) {
            locationComboBox.getSelectionModel().select(selectedLieu);
        }
    }

    // Initialize the location ComboBox
    private void initializeLocationComboBox() {
        List<Lieu> lieux = serviceLieu.getAll();
        locationComboBox.getItems().addAll(lieux);

        // Set a custom cell factory to display Lieu details
        locationComboBox.setCellFactory(param -> new ListCell<Lieu>() {
            @Override
            protected void updateItem(Lieu item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " - " + item.getAdresse() + " (Capacité: " + item.getCapacite() + ")");
                }
            }
        });

        // Set a custom button cell to display Lieu details
        locationComboBox.setButtonCell(new ListCell<Lieu>() {
            @Override
            protected void updateItem(Lieu item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " - " + item.getAdresse() + " (Capacité: " + item.getCapacite() + ")");
                }
            }
        });

        // Disable manual fields initially
        toggleManualLocationFields(false);
    }

    // Toggle manual Lieu entry fields
    @FXML
    private void toggleManualLocationFields() {
        boolean isManual = manualLocationCheckBox.isSelected();
        toggleManualLocationFields(isManual);
    }

    private void toggleManualLocationFields(boolean isManual) {
        newLocationField.setDisable(!isManual);
        addressField.setDisable(!isManual);
        capacityField.setDisable(!isManual);
        locationComboBox.setDisable(isManual);
    }

    @FXML
    private void saveUpdatedEvent() {
        // Check if event name, description, and time fields are not empty
        if (eventNameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            showAlert("Erreur", "Le nom et la description de l'événement sont obligatoires.");
            return;
        }

        // Check if start and end times are valid (HH:mm format)
        if (!isValidTimeFormat(startTimeField.getText()) || !isValidTimeFormat(endTimeField.getText())) {
            showAlert("Erreur", "Le format de l'heure doit être hh:mm.");
            return;
        }

        // Parse start and end times
        String[] startTimeParts = startTimeField.getText().split(":");
        String[] endTimeParts = endTimeField.getText().split(":");

        int startHour = Integer.parseInt(startTimeParts[0]);
        int startMinute = Integer.parseInt(startTimeParts[1]);
        int endHour = Integer.parseInt(endTimeParts[0]);
        int endMinute = Integer.parseInt(endTimeParts[1]);

        LocalTime startLocalTime = LocalTime.of(startHour, startMinute);
        LocalTime endLocalTime = LocalTime.of(endHour, endMinute);

        // Convert LocalTime to Timestamp
        event.setDateDebut(Timestamp.valueOf(calendarDatePicker.getValue().atTime(startLocalTime)));
        event.setDateFin(Timestamp.valueOf(calendarDatePicker.getValue().atTime(endLocalTime)));

        // Handle Lieu (location)
        if (manualLocationCheckBox.isSelected()) {
            // Validate manual location fields
            if (newLocationField.getText().isEmpty() || addressField.getText().isEmpty() || capacityField.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs du lieu sont obligatoires.");
                return;
            }

            // Validate if the capacity is a positive number
            try {
                int capacite = Integer.parseInt(capacityField.getText());
                if (capacite <= 0) {
                    showAlert("Erreur", "La capacité doit être un nombre positif.");
                    return;
                }

                // Create a new Lieu from manual entry
                Lieu newLieu = new Lieu();
                newLieu.setNom(newLocationField.getText());
                newLieu.setAdresse(addressField.getText());
                newLieu.setCapacite(capacite);

                // Save the new Lieu to the database
                serviceLieu.add(newLieu);

                // Set the new Lieu ID in the event
                event.setLieuId(newLieu.getLieuId());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La capacité doit être un nombre valide.");
                return;
            }
        } else {
            // Use the selected Lieu from the ComboBox
            Lieu selectedLieu = locationComboBox.getValue();
            if (selectedLieu == null) {
                showAlert("Erreur", "Vous devez sélectionner un lieu.");
                return;
            }
            event.setLieuId(selectedLieu.getLieuId());
        }

        // Save the updated event
        serviceEvenement.update(event);
        System.out.println("Événement mis à jour avec succès.");

        // Refresh the list of events in AllEventsController
        if (allEventsController != null) {
            allEventsController.refreshEvents();
        }

        // Close the update window
        eventNameField.getScene().getWindow().hide();
    }

    // Helper method to check if the time format is valid (HH:mm)
    private boolean isValidTimeFormat(String time) {
        String regex = "^([01]?[0-9]|2[0-3]):([0-5][0-9])$";
        return time.matches(regex);
    }

    // Helper method to show an alert with an error message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}