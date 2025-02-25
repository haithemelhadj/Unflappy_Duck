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
    private static Stack<Scene> navigationStack = new Stack<>();
    private static BorderPane borderPane;

    public static Stage getStage(){
        return primaryStage;
    }

    public static void initialize(Stage stage) {
        primaryStage = stage;
        primaryStage.sceneProperty().addListener((observable, oldScene, newScene)->{
            if (oldScene != null) {
                System.out.println("Scene removed: " + oldScene);
            }
            if (newScene != null) {
                System.out.println("Scene added: " + newScene);
            } else {
                System.out.println("No scene is set on the stage.");
            }
        });
    }

    public static void navigateTo(String fxmlPath, Object o) {
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
            primaryStage.show();
            navigationStack.add(scene);
        } catch (IOException e) {
            showError("Failed to load " + fxmlPath + ": " + e.getMessage());
        }
        System.out.println("Hello");
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
            primaryStage.setTitle(title);
            primaryStage.show();
            navigationStack.add(scene);
        } catch (IOException e) {
            showError("Failed to load " + fxmlPath + ": " + e.getMessage());
        }
    }

    public static void navigateBack(){
        if (navigationStack.size() == 1)
            return;
        navigationStack.pop();
        if (navigationStack.size() == 1)
            Session.end();
        primaryStage.setScene(navigationStack.peek());
        primaryStage.show();
    }

    public static void innerTo(String fxmlPath){

    }

    // Optional: Show error dialog
    private static void showError(String message) {
        // Implement a simple error dialog (e.g., Alert)
        System.err.println("Error: " + message); // Replace with GUI alert
    }
}