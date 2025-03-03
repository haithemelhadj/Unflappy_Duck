package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class FreelancerCard {
    @FXML
    private ProgressBar xpBar;
    @FXML
    private Label xpLabel;
    @FXML
    private HBox skillsContainer;

    private Label addSkill(String skill){
        Label l = new Label();
        l.setText(skill);
        l.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 8px; -fx-background-radius: 15;");
        return l;
    }

}
