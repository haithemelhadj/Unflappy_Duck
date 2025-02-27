package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class FrontController {
    @FXML
    public BorderPane borderPane;

    public void listEvents(ActionEvent actionEvent) throws IOException {
        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionEvenement/all_events.fxml")).load());
    }

    public void createEvent(ActionEvent actionEvent) throws IOException{
        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionEvenement/event_form.fxml")).load());
    }

    public void passQuiz(ActionEvent actionEvent) throws IOException{
        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionCours/quiz.fxml")).load());
    }

    public void goToStore(ActionEvent actionEvent) throws IOException{
//        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/homePage.fxml")).load());
    }

    public void goToMarket(ActionEvent actionEvent) throws IOException{
        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionFreelance/Market.fxml")).load());
    }

    public void goToTeams(ActionEvent actionEvent) throws IOException{
        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionEquipe/GestionEquipe.fxml")).load());
    }

    public void goToSettings(ActionEvent actionEvent) throws IOException{
//        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/homePage.fxml")).load());
    }
}
