package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import tn.esprit.models.Offre;

import java.text.SimpleDateFormat;

public class OffreBox {

    @FXML
    private VBox offerCard;
    
    @FXML
    private Rectangle container;
    
    @FXML
    private Text title;
    
    @FXML
    private Text budget;
    
    @FXML
    private Text description;
    
    @FXML
    private Text status;
    
    @FXML
    private Text expireDate;
    
    @FXML
    private Button applyButton;
    
    // OfferBox.fxml components
    @FXML
    private Text eventName;
    
    @FXML
    private Text contractType;
    
    private Offre offre;
    
    /**
     * @deprecated Use prepare() instead to match MarketOffre expectations
     */
    @Deprecated
    public void setData(Offre offre) {
        prepare(offre);
    }
    
    public void prepare(Offre offre) {
        this.offre = offre;
        
        title.setText(offre.getTitre());
        budget.setText(offre.getBudget() + " DT");
        description.setText(offre.getDescription());
        status.setText(offre.getStatut().toString());
        
        // Format the expiration date if the component exists
        if (expireDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            expireDate.setText("Expire le: " + dateFormat.format(offre.getExpire_le()));
        }
        
        // Set event name if available and component exists
        if (eventName != null && offre.getEvent() != null) {
            eventName.setText(offre.getEvent().getNom());
        } else if (eventName != null) {
            eventName.setText("Pas d'événement");
        }
        
        // Set contract type if component exists
        if (contractType != null) {
            contractType.setText(offre.getType_contrat().toString());
        }
        
        // Add event handler for the apply button
        if (applyButton != null) {
            applyButton.setOnAction(event -> handleApply());
        }
    }
    
    private void handleApply() {
        // Logic to apply for this offer
        System.out.println("Applied for offer: " + offre.getTitre());
        // This would typically open a dialog or navigate to an application form
    }
    
    public VBox getCard() {
        return offerCard;
    }
} 