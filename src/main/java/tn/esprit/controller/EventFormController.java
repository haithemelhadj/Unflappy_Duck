package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;
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

public class EventFormController {

    @FXML private TextField eventNameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private DatePicker calendarDatePicker;
    @FXML private TextField newLocationField;
    @FXML private TextField addressField;
    @FXML private TextField capacityField;
    @FXML private WebView mapWebView; // WebView for the map

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final ServiceLieu serviceLieu = new ServiceLieu();

    @FXML
    public void initialize() {
        // Load the map when the form initializes
        loadMap();
        mapWebView.getEngine().load("https://www.openstreetmap.org/");
    }

    private void loadMap() {
        WebEngine webEngine = mapWebView.getEngine();
        // Load a local HTML file or an online map
        String mapHtml = getClass().getResource("/map.html").toExternalForm();
        webEngine.load(mapHtml);

        // Add a Java-to-JavaScript bridge
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) { // Use Worker.State enum
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("java", this);
            }
        });
    }

    // This method will be called from JavaScript when a location is selected
    public void setLocation(double lat, double lon) {
        // Reverse geocode the coordinates to get the address
        String address = reverseGeocode(lat, lon);
        addressField.setText(address); // Populate the address field
    }

    private String reverseGeocode(double lat, double lon) {
        // Use OpenStreetMap's Nominatim API to reverse geocode
        String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + lat + "&lon=" + lon;
        try {
            java.net.URI uri = new java.net.URI(url);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "YourAppName/1.0");

            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            org.json.JSONObject json = new org.json.JSONObject(response.toString());
            return json.getJSONObject("address").optString("road", "") + ", " +
                    json.getJSONObject("address").optString("city", "") + ", " +
                    json.getJSONObject("address").optString("country", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "Adresse non trouvée";
        }
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
    private void goToAllEvents(ActionEvent event) throws IOException {
        Router.borderPaneCenterInsert(Router.getActiveBorderPane(), "/FrontOffice/GestionEvenement/all_events.fxml");
    }
}