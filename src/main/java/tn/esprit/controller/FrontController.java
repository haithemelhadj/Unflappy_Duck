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

public class    FrontController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private MenuButton freelancerMenu;

    @FXML
    public void listEvents(ActionEvent actionEvent) throws IOException {
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionEvenement/UserEventsView.fxml");
    }

    @FXML
    public void createEvent(ActionEvent actionEvent) throws IOException{
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionEvenement/event_form.fxml");
    }

    @FXML
    public void passQuiz(ActionEvent actionEvent) throws IOException{
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionCours/quiz.fxml");

    }

    @FXML
    public void goToStore(ActionEvent actionEvent) throws IOException{
        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionStore/ArticleBoutique.fxml")).load());
    }

    @FXML
    public void goToMarket(ActionEvent actionEvent) throws IOException{
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionFreelance/Market2.fxml");

    }

    @FXML
    public void goToTeams(ActionEvent actionEvent) throws IOException{
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionEquipe/GestionEquipe.fxml");

    }

    @FXML
    public void goToTaches(ActionEvent actionEvent) throws IOException{
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionEquipe/GestionTache.fxml");
    }

    @FXML
    public void goToSettings(ActionEvent actionEvent) throws IOException{
        borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionUtilisateur/UserProfile.fxml")).load());
    }

    @FXML
    public void goToMyList(ActionEvent actionEvent) throws IOException {
        if (Session.getActiveUser().getRole().equals(userRoles.ROLE_FREELANCER))
            Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionFreelance/Service.fxml");
        else if (Session.getActiveUser().getRole().equals(userRoles.ROLE_CLIENT))
            Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionFreelance/Offre.fxml");
    }
    @FXML
    public void openChatbot(ActionEvent actionEvent) throws IOException {
        Router.borderPaneCenterInsert(borderPane, "/FrontOffice/GestionUtilisateur/ChatbotView.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Session.getActiveUser().getRole().equals(userRoles.ROLE_FREELANCER))
            freelancerMenu.getItems().get(1).setText("Mes Services");
        else if (Session.getActiveUser().getRole().equals(userRoles.ROLE_CLIENT))
            freelancerMenu.getItems().get(1).setText("Mes Offres");
        else
            freelancerMenu.hide();
    }
}