package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceService;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MarketService implements Initializable {
    @FXML
    private ChoiceBox<String> searchMenu;
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> sortMenu;
    @FXML
    private ToggleButton sortOrder;
    @FXML
    private VBox container;

    private List<Service> serviceListDB;
    private List<Service> serviceList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceListDB = new ServiceService().getAll();
        Field[] fields = Service.class.getDeclaredFields();
        searchMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
        sortMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
    }
}
