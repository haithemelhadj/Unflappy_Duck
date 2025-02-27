package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import tn.esprit.models.Offre;
import tn.esprit.services.ServiceOffre;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MarketOffre implements Initializable {
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

    private List<Offre> offerListDB;
    private List<Offre> offerList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        offerListDB = new ServiceOffre().getAll();
        Field[] fields = Offre.class.getDeclaredFields();
        searchMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
        sortMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
    }
}
