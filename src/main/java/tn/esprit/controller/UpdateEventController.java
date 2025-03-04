package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Evenement;
import tn.esprit.models.Lieu;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceLieu;
import java.time.format.DateTimeParseException;
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

    @FXML private TextField newLocationField;
    @FXML private TextField addressField;
    @FXML private TextField capacityField;
    @FXML private CheckBox manualLocationCheckBox;

    private Evenement event;
    private AllEventsController allEventsController;
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final ServiceLieu serviceLieu = new ServiceLieu();

    public void setEvent(Evenement event) {
        this.event = event;
        loadEventDetails();
        initializeLocationComboBox();
    }

    public void setAllEventsController(AllEventsController allEventsController) {
        this.allEventsController = allEventsController;
    }

    private void loadEventDetails() {
        eventNameField.setText(event.getNom());
        descriptionField.setText(event.getDescription());
        startTimeField.setText(event.getDateDebut().toLocalDateTime().toLocalTime().toString());
        endTimeField.setText(event.getDateFin().toLocalDateTime().toLocalTime().toString());
        calendarDatePicker.setValue(event.getDateDebut().toLocalDateTime().toLocalDate());

        Lieu selectedLieu = event.getLieu();
        if (selectedLieu != null) {
            locationComboBox.getSelectionModel().select(selectedLieu);
        }
    }

    private void initializeLocationComboBox() {
        List<Lieu> lieux = serviceLieu.getAll();
        locationComboBox.getItems().addAll(lieux);

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

        toggleManualLocationFields(false);
    }

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
        if (eventNameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            showAlert("Erreur", "Le nom et la description de l'événement sont obligatoires.");
            return;
        }

        try {
            LocalTime startLocalTime = LocalTime.parse(startTimeField.getText());
            LocalTime endLocalTime = LocalTime.parse(endTimeField.getText());
            LocalDate eventDate = calendarDatePicker.getValue();

            event.setDateDebut(Timestamp.valueOf(eventDate.atTime(startLocalTime)));
            event.setDateFin(Timestamp.valueOf(eventDate.atTime(endLocalTime)));

            if (manualLocationCheckBox.isSelected()) {
                if (newLocationField.getText().isEmpty() || addressField.getText().isEmpty() || capacityField.getText().isEmpty()) {
                    showAlert("Erreur", "Tous les champs du lieu sont obligatoires.");
                    return;
                }

                try {
                    int capacite = Integer.parseInt(capacityField.getText());
                    if (capacite <= 0) {
                        showAlert("Erreur", "La capacité doit être un nombre positif.");
                        return;
                    }

                    Lieu newLieu = new Lieu();
                    newLieu.setNom(newLocationField.getText());
                    newLieu.setAdresse(addressField.getText());
                    newLieu.setCapacite(capacite);

                    serviceLieu.add(newLieu);
                    event.setLieuId(newLieu.getLieuId());
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "La capacité doit être un nombre valide.");
                    return;
                }
            } else {
                Lieu selectedLieu = locationComboBox.getValue();
                if (selectedLieu == null) {
                    showAlert("Erreur", "Vous devez sélectionner un lieu.");
                    return;
                }
                event.setLieuId(selectedLieu.getLieuId());
            }

            serviceEvenement.update(event);
            System.out.println("Événement mis à jour avec succès.");

            if (allEventsController != null) {
                allEventsController.refreshEvents();
            }

            eventNameField.getScene().getWindow().hide();
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Le format de l'heure doit être hh:mm.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}