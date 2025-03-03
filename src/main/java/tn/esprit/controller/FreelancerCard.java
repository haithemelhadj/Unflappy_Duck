package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;

public class FreelancerCard {
    @FXML
    private VBox recentJobsContainer;
    @FXML
    private Label userName;
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView profilePic;
    @FXML
    private Circle statusCircle;
    @FXML
    private ProgressBar xpBar;
    @FXML
    private Label xpLabel;
    @FXML
    private HBox skillsContainer;

    private final int nbrRecentJobs = 3;

    private Label addSkill(String skill){
        Label l = new Label();
        l.setText(skill);
        l.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 8px; -fx-background-radius: 15;");
        return l;
    }

    private List<HBox> recentJobs(){

    }

}
