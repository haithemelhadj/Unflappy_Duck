package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import tn.esprit.api.FirebaseNotificationSender;

public class NotificationCardController {
    @FXML private TextField titleField;
    @FXML private TextField bodyField;
    @FXML private Button sendButton;

    @FXML
    public void initialize() {
        sendButton.setOnAction(event -> {
            String title = titleField.getText();
            String body = bodyField.getText();

            try {
                FirebaseNotificationSender.sendNotificationToUser("news", title, body);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}