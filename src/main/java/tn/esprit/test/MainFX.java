package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
  /*
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/event_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Event Form");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
      /**/

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader =new FXMLLoader(getClass().getResource("/FrontOffice/GestionEquipe/GestionTache.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("---- Gestion Tache -----");
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
