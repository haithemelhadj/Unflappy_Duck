package tn.esprit.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tn.esprit.utils.Session;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

public class Router {
    private static Stage primaryStage;

    public static BorderPane getActiveBorderPane() {
        return activeBorderPane;
    }

    private static BorderPane activeBorderPane;

    public static Stage getStage(){
        return primaryStage;
    }

    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    public static void navigateTo(String fxmlPath, Object o) {
        navigateTo(fxmlPath, null, o);
    }

    public static void navigateTo(String fxmlPath, String title, Object o) {
        try {
            FXMLLoader loader = new FXMLLoader(Router.class.getResource(fxmlPath));
            loader.setControllerFactory(con ->{
                if (o != null)
                    return o;
                else {
                    try {
                        return con.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            if (title != null)
                primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            showError("Failed to load " + fxmlPath + ": " + e.getMessage());
        }
    }

    public static void borderPaneCenterInsert(BorderPane borderPane, String fxmlpath) throws IOException {
        borderPane.setCenter(new FXMLLoader(Router.class.getResource(fxmlpath)).load());
        activeBorderPane = borderPane;
    }

    private static void showError(String message) {
        System.err.println("Error: " + message);
    }
}