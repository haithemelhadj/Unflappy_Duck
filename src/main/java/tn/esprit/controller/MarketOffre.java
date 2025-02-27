package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import tn.esprit.models.Offre;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceOffre;

import java.io.IOException;
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

    private List<Offre> offerList;
    private List<Offre> offerListDynamic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        offerList = new ServiceOffre().getAll();
        updateContainer(offerList);
        Field[] fields = Offre.class.getDeclaredFields();
        searchMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
        sortMenu.getItems().add("Default");
        sortMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
        searchField.setOnKeyTyped(keyEvent -> {
            if (searchMenu.getValue() == null) return;
            offerListDynamic = offerList.stream().filter(o ->
                    switch (searchMenu.getValue()){
                        case "EVENT" -> o.getEvent().getNom().toLowerCase().contains(searchField.getText().toLowerCase());
                        case "TITRE" -> o.getTitre().toLowerCase().contains(searchField.getText().toLowerCase());
                        case "DESCRIPTION" -> o.getDescription().toLowerCase().contains(searchField.getText().toLowerCase());
                        case "EXPIRE LE" -> o.getExpire_le().toString().toLowerCase().contains(searchField.getText().toLowerCase());
                        case "BUDGET" -> o.getBudget().toString().toLowerCase().contains(searchField.getText().toLowerCase());
                        case "STATUS" -> o.getStatut().toString().toLowerCase().contains(searchField.getText().toLowerCase());
                        case "TYPE CONTRAT" -> o.getType_contrat().toString().toLowerCase().contains(searchField.getText().toLowerCase());
                        default -> false;
                    }
            ).toList();
            updateContainer(offerListDynamic);
        });
        sortMenu.valueProperty().addListener((observable, oldVal, newVal) -> {
            offerListDynamic = offerList.stream().sorted((i1, i2) ->
                    switch (searchMenu.getValue()){
                        case "EVENT" -> i1.getEvent().getNom().compareTo(i2.getEvent().getNom());
                        case "TITRE" -> i1.getTitre().compareTo(i2.getTitre());
                        case "DESCRIPTION" -> i1.getDescription().compareTo(i2.getDescription());
                        case "EXPIRE LE" -> i1.getExpire_le().compareTo(i2.getExpire_le());
                        case "BUDGET" -> i1.getBudget().compareTo(i2.getBudget());
                        case "STATUS" -> i1.getStatut().compareTo(i2.getStatut());
                        case "TYPE CONTRAT" -> i1.getType_contrat().compareTo(i2.getType_contrat());
                        default -> 1;
                    }
            ).toList();
            updateContainer(offerListDynamic);
        });
        sortOrder.selectedProperty().addListener(e -> {
            if (offerListDynamic == null) return;
            offerListDynamic = offerListDynamic.stream()
                    .sorted((s1, s2) -> offerListDynamic.indexOf(s2) - offerListDynamic.indexOf(s1))
                    .toList();
            updateContainer(offerListDynamic);
        });
    }

    private void updateContainer(List<Offre> list){
        container.getChildren().clear();
        list.forEach(service -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionFreelance/OffreBox.fxml"));
            VBox vBox;
            try {
                vBox = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            OfferBox cont = loader.getController();
            cont.prepare(service);
            container.getChildren().add(vBox);
        });
    }
}
