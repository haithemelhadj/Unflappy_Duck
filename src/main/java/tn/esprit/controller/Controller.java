package tn.esprit.controller;

import tn.esprit.models.Utilisateur;

public class Controller {
    private static Utilisateur user = null;
    public static void login(){
        Router.navigateTo("/login.fxml", "Login", new Login(()->
            Router.navigateTo("/FrontOffice/GestionFreelance/GestionService.fxml", new GestionService())
        ));
    }

    public static void setUser(Utilisateur user) {
        Controller.user = user;
        System.out.println(user);
        System.out.println(Router.getStage().getScene());
    }

    public static Utilisateur getUser() {
        return user;
    }
}
