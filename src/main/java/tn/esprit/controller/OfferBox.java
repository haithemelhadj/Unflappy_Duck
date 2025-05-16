package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import tn.esprit.models.Offre;

import java.text.SimpleDateFormat;

public class OfferBox {

    @FXML
    private VBox offerCard;
    
    @FXML
    private Rectangle container;
    
    @FXML
    private Text eventName;
    
    @FXML
    private Text title;
    
    @FXML
    private Text budget;
    
    @FXML
    private Text description;
    
    @FXML
    private Text status;
    
    @FXML
    private Text contractType;
    
    @FXML
    private Text expireDate;
    
    @FXML
    private Button applyButton;
    
    private Offre offre;
    
    public void prepare(Offre offre) {
        this.offre = offre;
        
        // Set event name if available
        if (offre.getEvent() != null) {
            eventName.setText(offre.getEvent().getNom());
        } else {
            eventName.setText("Pas d'événement");
        }
        
        title.setText(offre.getTitre());
        budget.setText(offre.getBudget() + " DT");
        description.setText(offre.getDescription());
        status.setText(offre.getStatut().toString());
        contractType.setText(offre.getType_contrat().toString());
        
        // Format the expiration date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        expireDate.setText("Expire le: " + dateFormat.format(offre.getExpire_le()));
        
        // Add event handler for the apply button
        applyButton.setOnAction(event -> handleApply());
    }
    
    private void handleApply() {
        // Logic to apply for this offer
        System.out.println("Applied for offer: " + offre.getTitre());
        // This would typically open a dialog or navigate to an application form
    }
}
