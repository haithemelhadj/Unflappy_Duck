package tn.esprit.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Router {
    private static Stage primaryStage;
    public static Stage getStage(){
        return primaryStage;
    }

    // Initialize the router with the primary stage (call this once at startup)
    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    // Navigate to a new scene by FXML path
    public static void navigateTo(String fxmlPath, Object o) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(Router.class.getResource(fxmlPath));
            loader.setController(o);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            showError("Failed to load " + fxmlPath + ": " + e.getMessage());
        }
        System.out.println("Hello");
    }

    public static void navigateTo(String fxmlPath, String title, Object o) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(Router.class.getResource(fxmlPath));
            loader.setController(o);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            showError("Failed to load " + fxmlPath + ": " + e.getMessage());
        }
    }

    // Optional: Show error dialog
    private static void showError(String message) {
        // Implement a simple error dialog (e.g., Alert)
        System.err.println("Error: " + message); // Replace with GUI alert
    }
}