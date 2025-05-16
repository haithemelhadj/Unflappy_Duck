package tn.esprit.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import tn.esprit.models.Offre;
import tn.esprit.services.ServiceOffre;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OffreUser implements Initializable {

    @FXML
    private TableView<Offre> tableView;
    
    @FXML
    private TableColumn<Offre, String> titleColumn;
    
    @FXML
    private TableColumn<Offre, String> descriptionColumn;
    
    @FXML
    private TableColumn<Offre, String> budgetColumn;
    
    @FXML
    private TableColumn<Offre, String> expiresColumn;
    
    @FXML
    private TableColumn<Offre, String> statusColumn;
    
    @FXML
    private TableColumn<Offre, Void> actionsColumn;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private FlowPane cardContainer;
    
    @FXML
    private ToggleButton viewToggle;

    private List<Offre> offerList;
    private final ServiceOffre serviceOffre = new ServiceOffre();
    
    private boolean isTableView = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOffers();
        setupTableColumns();
        
        // If we have the toggle button initialized
        if (viewToggle != null) {
            viewToggle.setOnAction(event -> toggleView());
        }
    }
    
    private void loadOffers() {
        // Get all offers and load them into the table
        offerList = serviceOffre.getAll();
        // Card View
        if (cardContainer != null) {
            loadCardView();
            tableView.setVisible(false);
            cardContainer.setVisible(true);
        }
    }
    
    private void loadCardView() {
        if (cardContainer != null) {
            cardContainer.getChildren().clear();
            
            for (Offre offre : offerList) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionFreelance/OffreBox.fxml"));
                    VBox offerBox = loader.load();
                    
                    OffreBox controller = loader.getController();
                    controller.setData(offre);
                    
                    cardContainer.getChildren().add(offerBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void toggleView() {
        isTableView = !isTableView;
        loadOffers();
    }
    
    private void setupTableColumns() {
        // Configure each column to display the appropriate data
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        
        // For description, truncate if too long
        descriptionColumn.setCellValueFactory(cellData -> {
            String desc = cellData.getValue().getDescription();
            if (desc.length() > 50) {
                desc = desc.substring(0, 47) + "...";
            }
            return new SimpleStringProperty(desc);
        });
        
        budgetColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBudget().toString() + " DT"));
        
        expiresColumn.setCellValueFactory(cellData -> {
            Date expireDate = cellData.getValue().getExpire_le();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return new SimpleStringProperty(dateFormat.format(expireDate));
        });
        
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatut().toString()));
            
        // Setup action buttons column
        setupActionsColumn();
    }
    
    private void setupActionsColumn() {
        Callback<TableColumn<Offre, Void>, TableCell<Offre, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Offre, Void> call(final TableColumn<Offre, Void> param) {
                return new TableCell<>() {
                    private final Button editBtn = new Button("Modifier");
                    private final Button deleteBtn = new Button("Supprimer");
                    private final HBox buttonBox = new HBox(5, editBtn, deleteBtn);

                    {
                        editBtn.getStyleClass().add("edit-button");
                        deleteBtn.getStyleClass().add("delete-button");
                        
                        editBtn.setOnAction(event -> {
                            Offre offer = getTableView().getItems().get(getIndex());
                            handleEditOffer(offer);
                        });
                        
                        deleteBtn.setOnAction(event -> {
                            Offre offer = getTableView().getItems().get(getIndex());
                            handleDeleteOffer(offer);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonBox);
                        }
                    }
                };
            }
        };
        
        actionsColumn.setCellFactory(cellFactory);
    }
    
    @FXML
    private void handleAddOffer() {
        // Open a dialog to add a new offer
        // This would typically navigate to a form or open a dialog
        System.out.println("Add new offer");
    }
    
    private void handleEditOffer(Offre offer) {
        // Open a dialog to edit the selected offer
        System.out.println("Edit offer: " + offer.getTitre());
    }
    
    private void handleDeleteOffer(Offre offer) {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText("Supprimer l'offre");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette offre : " + offer.getTitre() + "?");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the offer
            serviceOffre.delete(offer);
            loadOffers(); // Reload the table
        }
    }
    
    @FXML
    private void handleSave() {
        // Save any pending changes
        System.out.println("Changes saved");
    }
    
    @FXML
    private void handleCancel() {
        // Cancel any pending changes
        loadOffers(); // Reload the original data
    }
} 