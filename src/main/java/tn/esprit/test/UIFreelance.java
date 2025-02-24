package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.controller.LoginFreelance;

import java.io.IOException;

public class UIFreelance extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/FrontOffice/GestionFreelance/login.fxml"));
        LoginFreelance controller = null;
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
            controller = loader.getController();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (controller.getUser() != null){
            loader.getClass().getResource("/FrontOffice/GestionFreelance/GestionService.fxml");
            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Gestion Service");
                primaryStage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}