package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrontController implements Initializable {
    @FXML
    private MenuButton events_menu;
    @FXML
    private MenuButton freelance_menu;
    @FXML
    private MenuButton quiz_menu;
    @FXML
    private MenuButton store_menu;
    @FXML
    private static BorderPane borderPane;

    public static BorderPane getBorderPane(){
        System.out.println("front");
        return borderPane;
    }

    public void navigateToEvents(ActionEvent actionEvent) throws IOException {
        //borderPane.setCenter(new FXMLLoader(getClass().getResource("/FrontOffice/GestionEvenement/all_events.fxml")).load());
    }

    public void navigateToMarket(ActionEvent actionEvent) {
    }

    public void navigateToQuiz(ActionEvent actionEvent) {
    }

    public void navigateToStore(ActionEvent actionEvent) {
        
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        events_menu.getItems().addAll(new MenuItem("List"), new MenuItem("Create"));
        freelance_menu.getItems().addAll(new MenuItem("Market"), new MenuItem("Create Service"));
        quiz_menu.getItems().addAll(new MenuItem("ziw"), new MenuItem("ppiw"));
        store_menu.getItems().addAll(new MenuItem("Voir Panier"), new MenuItem("Voir Articles"));
        events_menu.getItems().
    }
}
