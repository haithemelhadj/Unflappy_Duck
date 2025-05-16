package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.models.Service;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceService;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceUser implements Initializable {

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private List<Service> serviceList;
    private final ServiceService ss = new ServiceService();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceList = ss.getAll();
        Field[] fields = Service.class.getDeclaredFields();
    }
}
