package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.models.Offre;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceOffre;
import tn.esprit.services.ServiceService;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class MarketController implements Initializable {
    @FXML
    public TableView<Object> table;
    @FXML
    private ChoiceBox<String> menu;
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
                table.getColumns().clear();
                table.getItems().clear();
                table.getItems().addAll(serviceList);
                Field[] fields = Service.class.getDeclaredFields();
                table.getColumns().addAll(
                        Arrays.stream(fields).skip(1).map(f->{
                                TableColumn<Object, Object> col = new TableColumn<>(f.getName().toUpperCase().replace('_', ' '));
                                col.setCellValueFactory(new PropertyValueFactory<>(f.getName()));
                                return col;
                            }
                        ).toList()
                );
                searchMenu.getItems().clear();
                searchMenu.getItems().addAll(Arrays.stream(fields).skip(1).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
            }
            else {
                offreList = new ServiceOffre().getAll();
                table.getColumns().clear();
                table.getItems().clear();
                table.getItems().addAll(offreList);
                Field[] fields = Offre.class.getDeclaredFields();
                table.getColumns().addAll(
                        Arrays.stream(fields).skip(1).map(f->{
                                    TableColumn<Object, Object> col = new TableColumn<>(f.getName().toUpperCase().replace('_', ' '));
                                    col.setCellValueFactory(new PropertyValueFactory<>(f.getName()));
                                    return col;
                            }
                        ).toList()
                );
                searchMenu.getItems().clear();
                searchMenu.getItems().addAll(Arrays.stream(fields).skip(1).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
            }
        });
    }
}
