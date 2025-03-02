package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BackController implements Initializable {
    @FXML
    private BorderPane freelancerBorderPane;
    @FXML
    private BorderPane evenementBorderPane;
    @FXML
    private BorderPane utilisateurBorderPane;
    @FXML
    private BorderPane equipeBorderPane;
    @FXML
    private BorderPane coursBorderPane;
    @FXML
    private BorderPane storeBorderPane;
    @FXML
    private BorderPane borderPane;

    public BorderPane getBorderPane(){
        return borderPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void show_article(ActionEvent actionEvent) throws IOException {
        Router.borderPaneCenterInsert(storeBorderPane,"/BackOffice/GestionStore/ArticleBoutique.fxml");
    }

    public void show_panier(ActionEvent actionEvent) throws IOException {
        Router.borderPaneCenterInsert(storeBorderPane,"/BackOffice/GestionStore/Panier.fxml");
    }
}
