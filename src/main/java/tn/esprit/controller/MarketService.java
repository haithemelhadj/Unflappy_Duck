package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceService;

import java.io.IOException;
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

    private List<Service> serviceList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        sortOrder.getToggleGroup().getToggles().addAll("1","0");
        serviceList = new ServiceService().getAll();
        updateContainer(serviceList);
        Field[] fields = Service.class.getDeclaredFields();
        searchMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
        sortMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
        searchField.setOnKeyTyped(keyEvent -> {
            if (searchMenu.getValue() == null) return;
            updateContainer(serviceList.stream().filter(s ->
                switch (searchMenu.getValue()){
                    case "FREELANCER" -> s.getFreelancer().getNom().toLowerCase().contains(searchField.getText().toLowerCase());
                    case "TITRE" -> s.getTitre().toLowerCase().contains(searchField.getText().toLowerCase());
                    case "DESCRIPTION" -> s.getDescription().toLowerCase().contains(searchField.getText().toLowerCase());
                    case "EXPERTISE" -> s.getExpertise().toLowerCase().contains(searchField.getText().toLowerCase());
                    case "DUREE JOURS" -> Integer.toString(s.getDuree_jours()).toLowerCase().contains(searchField.getText().toLowerCase());
                    case "PRIX" -> s.getPrix().toString().toLowerCase().contains(searchField.getText().toLowerCase());
                    case "MODE PAIEMENT" -> s.getMode_paiement().toString().toLowerCase().contains(searchField.getText().toLowerCase());
                    default -> false;
                }
            ).toList());
        });
        sortMenu.valueProperty().addListener(keyEvent -> {
            if (sortMenu.getValue() == null) return;
            updateContainer(serviceList.stream().sorted((i1, i2) ->
                    switch (sortMenu.getValue()){
                        case "FREELANCER" -> i1.getFreelancer().getNom().compareTo(i2.getFreelancer().getNom());
                        case "TITRE" -> i1.getTitre().compareTo(i2.getTitre());
                        case "DESCRIPTION" -> i1.getDescription().compareTo(i2.getDescription());
                        case "EXPERTISE" -> i1.getExpertise().compareTo(i2.getExpertise());
                        case "DUREE JOURS" -> i1.getDuree_jours()-i2.getDuree_jours();
                        case "PRIX" -> i1.getPrix().compareTo(i2.getPrix());
                        case "MODE PAIEMENT" -> i1.getMode_paiement().compareTo(i2.getMode_paiement());
                        default -> 1;
                    }
            ).toList());
        });
    }

    private void updateContainer(List<Service> list){
        container.getChildren().clear();
        list.forEach(service -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionFreelance/ServiceBox.fxml"));
            VBox vBox;
            try {
                vBox = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ServiceBox cont = loader.getController();
            cont.prepare(service);
            container.getChildren().add(vBox);
        });
    }
}
