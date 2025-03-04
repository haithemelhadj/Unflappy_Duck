package tn.esprit.test;

import javafx.application.Application;
import javafx.stage.Stage;
import tn.esprit.api.FirebaseConfig;
import tn.esprit.controller.Controller;
import tn.esprit.controller.Router;
import tn.esprit.services.ServiceUtilisateur;
import tn.esprit.utils.Session;

import java.io.IOException;
import java.sql.SQLException;


public class UIFreelance extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        Router.initialize(primaryStage);
        Controller con = new Controller();
      // Session.start(new ServiceUtilisateur().getUtilisateurById(1));
        con.start();
    }
    void initFireBase(Stage primaryStage) throws IOException {
        FirebaseConfig.initializeFirebase(); // Initialize Firebase on App Start
        primaryStage.setTitle("JavaFX Firebase Push Notification");
        primaryStage.show();
    }
}