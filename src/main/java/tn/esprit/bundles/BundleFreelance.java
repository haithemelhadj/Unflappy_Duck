package tn.esprit.bundles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.controller.Login;
import tn.esprit.controller.Login;

import java.io.IOException;

public class BundleFreelance extends Application {
    Parent login = null;


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/login.fxml"));
        Login controller = null;
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
            controller = loader.getController();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
