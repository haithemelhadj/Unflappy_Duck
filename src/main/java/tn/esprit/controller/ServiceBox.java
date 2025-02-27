package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import tn.esprit.models.Service;

public class ServiceBox {
    @FXML
    private Rectangle container;
    @FXML
    private Text title;
    @FXML
    private Text price;
    @FXML
    private Text description;
    @FXML
    private Button hireButton;
    @FXML
    private Text expertise;
    @FXML
    private Text FreelancerName;
    @FXML
    private Text paymentMethod;

    public void setData(Service service){
        title.setText(service.getTitre());
        price.setText(service.getPrix().toString());
        description.setText(service.getDescription());
        expertise.setText(service.getExpertise());
        FreelancerName.setText(service.getFreelancer().getNom());
        paymentMethod.setText(service.getMode_paiement().toString());
    }
}
