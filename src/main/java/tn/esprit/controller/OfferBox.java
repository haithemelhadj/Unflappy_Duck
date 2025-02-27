package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import tn.esprit.models.Offre;
import tn.esprit.models.Service;

public class OfferBox {
    @FXML
    private Rectangle container;
    @FXML
    private Text title;
    @FXML
    private Text description;
    @FXML
    private Button apply;
    @FXML
    private Text budget;
    @FXML
    private Text status;
    @FXML
    private Text contractType;
    @FXML
    private Text eventName;

    public void prepare(Offre offre){
        title.setText(offre.getTitre());
        description.setText(offre.getDescription());
        budget.setText(offre.getBudget().toString());
        eventName.setText(offre.getEvent().getNom());
        contractType.setText(offre.getType_contrat().toString());
        status.setText(offre.getStatut().toString());
    }
}
