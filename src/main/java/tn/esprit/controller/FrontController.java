package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class FrontController extends Controller{
    @FXML
    private BorderPane borderPane;

    public BorderPane getBorderPane(){
        System.out.println("front");
        return borderPane;
    }

    public void navigateToEvents(ActionEvent actionEvent) throws IOException {
        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionEvenement/all_events.fxml")).load());
    }

    public void navigateToMarket(ActionEvent actionEvent) {
    }

    public void navigateToQuiz(ActionEvent actionEvent) {
    }

    public void navigateToStore(ActionEvent actionEvent) {
        
    }
}
