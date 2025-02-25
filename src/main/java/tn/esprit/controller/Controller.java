package tn.esprit.controller;

import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;

public class Controller {
    private static Utilisateur user = null;
    public static void login(){
        Router.navigateTo("/login.fxml", "Login", new Login(()->{
            if (user.getRole() == userRoles.admin)
                Router.navigateTo("/BackOffice/backController.fxml", null);
            else
                Router.navigateTo("/FrontOffice/frontController.fxml", null);

        }
        ));
    }

    public static void setUser(Utilisateur user) {
        Controller.user = user;
    }

    public static Utilisateur getUser() {
        return user;
    }
}
