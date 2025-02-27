package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import tn.esprit.models.Offre;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceOffre;
import tn.esprit.services.ServiceService;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Market2Controller implements Initializable {
    // Offer items
    @FXML
    private Rectangle offerContainer;
    @FXML
    private ChoiceBox<String> offerSearchMenu;
    @FXML
    private TextField offerSearchField;
    @FXML
    private ChoiceBox<String> offerSortField;
    @FXML
    private ToggleButton offerSortOrder;
    @FXML
    private Pane serviceItem1;
    @FXML
    private Text offerTitle;
    @FXML
    private Text offerBudget;
    @FXML
    private Text offerDescription;
    @FXML
    private Button offerApply;
    @FXML
    private Text offerStatus;
    @FXML
    private Text offerEventName;
    @FXML
    private Text offerContactType;
    private List<Offre> offerList;

    // Service items
    @FXML
    private Text servicePaymentMethod;
    @FXML
    private Text serviceFreelancerName;
    @FXML
    private Text serviceExpertise;
    @FXML
    private Button serviceHire;
    @FXML
    private Text serviceDescription;
    @FXML
    private Text servicePrice;
    @FXML
    private Text serviceTitle;
    @FXML
    private Rectangle serviceContainer;
    @FXML
    private Pane serviceItem;
    private List<Pane> serviceItems;
    @FXML
    private ToggleButton serviceSortOrder;
    @FXML
    private ChoiceBox<String> serviceSortMenu;
    @FXML
    private TextField serviceSearchField;
    @FXML
    private ChoiceBox<String> serviceSearchMenu;
    private List<Service> serviceList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Offre
        offerList = new ServiceOffre().getAll();
        Field[] fields = Offre.class.getDeclaredFields();
        offerSearchMenu.getItems().clear();
        offerSearchMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(f->!f.getName().equals("cree_le") && !f.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());

        // Service
        serviceList = new ServiceService().getAll();
        fields = Service.class.getDeclaredFields();
        serviceSearchMenu.getItems().clear();
        serviceSearchMenu.getItems().addAll(Arrays.stream(fields).skip(1).filter(f->!f.getName().equals("cree_le") && !f.getName().equals("mis_a_jour_le")).map(f-> f.getName().toUpperCase().replace('_', ' ')).toList());
        serviceList.forEach(service -> {

        });
    }

    private class PaneItem{
        public
    }
}
