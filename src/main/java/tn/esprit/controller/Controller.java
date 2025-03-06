package tn.esprit.controller;

import tn.esprit.models.enums.userRoles;
import tn.esprit.utils.Session;

public class Controller {
    public final void start(){
        if (Session.isActive()){
            if (Session.getActiveUser().getRole() == userRoles.admin)
                Router.navigateTo("/BackOffice/backController.fxml", null);
            else
                Router.navigateTo("/homePage.fxml", null);
        }
        else
            Router.navigateTo("/startupWindow.fxml", "StartupController", new StartupController(()->{
                if (Session.getActiveUser().getRole() == userRoles.admin)
                    Router.navigateTo("/BackOffice/backController.fxml", null);
                else
                    Router.navigateTo("/homePage.fxml", null);
            }
            ));
    }
}
