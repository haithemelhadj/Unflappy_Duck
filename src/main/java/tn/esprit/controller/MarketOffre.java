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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        offerList = new ServiceOffre().getAll();
        updateContainer(offerList);
        Field[] fields = Offre.class.getDeclaredFields();
        searchMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
        sortMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(field -> !field.getName().equals("cree_le") && !field.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
//        searchField.setOnKeyTyped(keyEvent -> {
//            updateContainer(offerList.stream().filter(o -> {
//                switch (searchMenu.getValue()){
//                   case "" -> System.out.println("a");
//                }
//            }));
//        });
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
