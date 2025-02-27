package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Market2Controller implements Initializable {

    @FXML
    private BorderPane serviceBorderPane;
    @FXML
    private BorderPane offerBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceBorderPane.setCenter();
        offerBorderPane.setCenter();
    }
}
