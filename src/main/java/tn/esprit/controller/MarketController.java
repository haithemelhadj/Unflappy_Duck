package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.esprit.models.Offre;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceOffre;
import tn.esprit.services.ServiceService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MarketController implements Initializable {
    @FXML
    public ListView<Object> listView;
    @FXML
    private ChoiceBox<String> menu;
    @FXML
    private ChoiceBox<String> sortMenu;
    @FXML
    private ChoiceBox<String> searchMenu;
    @FXML
    private TextField searchField;

    private List<Service> serviceList;
    private List<Offre> offreList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menu.getItems().addAll("Services", "Offres");
        menu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (menu.getValue().equals("Services")) {
                serviceList = new ServiceService().getAll();
                listView.getItems().addAll(serviceList);
            }
            else {
                offreList = new ServiceOffre().getAll();
                listView.getItems().addAll(serviceList);
            }
        });
    }
}
