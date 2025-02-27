package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import tn.esprit.models.enums.userRoles;
import tn.esprit.utils.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrontController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private MenuButton freelancerMenu;

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

    public void goToMyList(ActionEvent actionEvent) throws IOException {
        if (Session.getActiveUser().getRole().equals(userRoles.freelancer))
            borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionFreelance/Service.fxml")).load());
        else if (Session.getActiveUser().getRole().equals(userRoles.organisateur))
            borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionFreelance/Offre.fxml")).load());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Session.getActiveUser().getRole().equals(userRoles.freelancer))
            freelancerMenu.getItems().get(1).setText("Mes Services");
        else if (Session.getActiveUser().getRole().equals(userRoles.organisateur))
            freelancerMenu.getItems().get(1).setText("Mes Offres");
        else
            freelancerMenu.hide();
    }
}
