package tn.esprit.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceService;
import tn.esprit.services.ServiceUtilisateur;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GestionService implements Initializable {
    public TextField text_titre;
    public TextField text_freelancer_id;
    public TextField text_prix;
    public TextField text_duree_jours;
    public Text service_id;
    public ChoiceBox<Service.MethodePaiement> mode_paiement;
    public ChoiceBox<Integer> test;
    public TextArea text_description;
    public TextArea text_expertise;
    @FXML
    private ListView<Service> services;

    ServiceService ss = new ServiceService();
    Service selectedService = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        services.getItems().addAll(ss.getAll());
        services.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Service>() {
            @Override
            public void changed(ObservableValue<? extends Service> observableValue, Service service, Service t1) {
                selectedService = services.getSelectionModel().getSelectedItem();
                text_description.setText(selectedService.getDescription());
                text_duree_jours.setText(Integer.toString(selectedService.getDuree_jours()));
                text_prix.setText(selectedService.getPrix().toString());
                text_titre.setText(selectedService.getTitre());
                text_expertise.setText(selectedService.getExpertise());
//                text_freelancer_id.setText(Integer.toString(selectedService.getFreelancer()));
                service_id.setText(Integer.toString(selectedService.getService_id()));
                mode_paiement.setValue(selectedService.getMode_paiement());
            }
        });
        mode_paiement.getItems().addAll(Service.MethodePaiement.values());
    }

    public void updateService(ActionEvent actionEvent) throws SQLException {
        if (selectedService == null) return;
        else{
            selectedService.setFreelancer(new ServiceUtilisateur().getUtilisateurById(Integer.parseInt(text_freelancer_id.getText())));
            selectedService.setTitre(text_titre.getText());
            selectedService.setDescription(text_description.getText());
            selectedService.setExpertise(text_expertise.getText());
            selectedService.setDuree_jours(Integer.parseInt(text_duree_jours.getText()));
            selectedService.setPrix(new BigDecimal(text_prix.getText()));
            selectedService.setMode_paiement(mode_paiement.getValue());
            ss.update(selectedService);
            services.getItems().set(services.getItems().indexOf(selectedService), selectedService);
            selectedService = null;
        }
    }

    public void supprimerService(ActionEvent actionEvent) {
        if (selectedService == null) return;
        else{
            ss.delete(selectedService);
            services.getItems().remove(selectedService);
            selectedService = null;
        }
    }

    public void ajouterService(ActionEvent actionEvent) {

    }
}
