package tn.esprit.controller;

import tn.esprit.models.Utilisateur;

public class Controller {
    private static Utilisateur user = null;
    public static void login(){
        Router.navigateTo("/login.fxml", "Login");
    }

    public static void setUser(Utilisateur user) {
        Controller.user = user;
        Router.
    }

    public static Utilisateur getUser() {
        return user;
    }
}
