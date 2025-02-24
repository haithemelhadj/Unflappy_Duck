package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceService;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class AjouterService implements Initializable {
    @FXML
    private TextField freelance_id;
    @FXML
    private TextField titre;
    @FXML
    private TextField duree_jours;
    @FXML
    private TextField prix;
    @FXML
    private ChoiceBox<Service.MethodePaiement> mode_paiement;
    @FXML
    private TextArea description;
    @FXML
    private TextArea expertise;
    @FXML
    public Button boutonAjouter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mode_paiement.getItems().addAll(Service.MethodePaiement.values());
    }

    @FXML
    public void ajouterService(ActionEvent actionEvent) {
        new ServiceService().add(new Service(
                Integer.parseInt(freelance_id.getText()),
                titre.getText(),
                description.getText(),
                expertise.getText(),
                Integer.parseInt(duree_jours.getText()),
                new BigDecimal(prix.getText()),
                mode_paiement.getValue()
        ));
    }
}