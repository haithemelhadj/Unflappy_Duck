package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
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
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionEvenement/all_events.fxml");
    }

    public void createEvent(ActionEvent actionEvent) throws IOException{
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionEvenement/event_form.fxml");
    }

    public void passQuiz(ActionEvent actionEvent) throws IOException{
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionCours/quiz.fxml");

    }

    public void goToStore(ActionEvent actionEvent) throws IOException{
//        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/homePage.fxml")).load());
    }

    public void goToMarket(ActionEvent actionEvent) throws IOException{
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionFreelance/Market2.fxml");

    }

    public void goToTeams(ActionEvent actionEvent) throws IOException{
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionEquipe/GestionEquipe.fxml");

    }

    public void goToSettings(ActionEvent actionEvent) throws IOException{
//        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/homePage.fxml")).load());
    }

    public void goToMyList(ActionEvent actionEvent) throws IOException {
        if (Session.getActiveUser().getRole().equals(userRoles.freelancer))
            Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionFreelance/Service.fxml");
        else if (Session.getActiveUser().getRole().equals(userRoles.organisateur))
            Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionFreelance/Offre.fxml");

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
