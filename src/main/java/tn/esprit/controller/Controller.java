package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import tn.esprit.models.enums.userRoles;
import tn.esprit.utils.Session;

public class Controller {
    public final void login(){
        if (Session.isActive()){
            if (Session.getActiveUser().getRole() == userRoles.admin)
                Router.navigateTo("/BackOffice/backController.fxml", null);
            else
                Router.navigateTo("/FrontOffice/homePage.fxml", null);
        }
        else
            Router.navigateTo("/login.fxml", "Login", new Login(()->{
                if (Session.getActiveUser().getRole() == userRoles.admin)
                    Router.navigateTo("/BackOffice/backController.fxml", null);
                else
                    Router.navigateTo("/FrontOffice/homePage.fxml", null);
            }
            ));
    }
}
