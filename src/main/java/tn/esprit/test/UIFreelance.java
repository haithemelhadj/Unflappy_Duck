package tn.esprit.test;

import com.google.firebase.messaging.FirebaseMessaging;
import javafx.application.Application;
import javafx.stage.Stage;
import tn.esprit.api.FirebaseConfig;
import tn.esprit.controller.Controller;
import tn.esprit.controller.Router;
import tn.esprit.services.ServiceUtilisateur;
import tn.esprit.utils.Session;

import java.io.IOException;
import java.sql.SQLException;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;


public class UIFreelance extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        Router.initialize(primaryStage);
        Controller con = new Controller();
        FirebaseConfig.initializeFirebase();
      // Session.start(new ServiceUtilisateur().getUtilisateurById(1));
        con.start();
    }
    void initFireBase(Stage primaryStage) throws IOException {
        FirebaseConfig.initializeFirebase(); // Initialize Firebase on App Start
        primaryStage.setTitle("JavaFX Firebase Push Notification");
        primaryStage.show();
    }


}



