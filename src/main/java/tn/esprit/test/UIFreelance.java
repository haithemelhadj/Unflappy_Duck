package tn.esprit.test;

import javafx.application.Application;
import javafx.stage.Stage;
import tn.esprit.controller.Controller;
import tn.esprit.controller.Router;

public class UIFreelance extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Router.initialize(primaryStage);

        Controller.login();
    }
}