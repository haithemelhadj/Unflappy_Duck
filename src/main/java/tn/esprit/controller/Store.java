package tn.esprit.controller;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Store implements Initializable {


    public Button ArticleBoutique;
    public Button Panier;
    public BorderPane articlePane;
    public BorderPane panierPane;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Router.borderPaneCenterInsert(articlePane, "/FrontOffice/GestionStore/ArticleBoutique.fxml");
            Router.borderPaneCenterInsert(panierPane, "/FrontOffice/GestionStore/Panier.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
