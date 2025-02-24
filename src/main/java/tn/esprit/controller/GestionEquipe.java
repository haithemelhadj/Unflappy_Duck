package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import tn.esprit.models.Tache;
//import tn.esprit.models.enums.Statut;
import tn.esprit.models.enums.userRoles;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceTache;
import tn.esprit.services.ServiceUtilisateur;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
public class GestionEquipe {

    @FXML
    private TextField evenement_id;

    @FXML
    private ChoiceBox<?> membre_id;

    @FXML
    private TextField nom;

    @FXML
    private ListView<?> taches_list;

    @FXML
    void add(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {

    }

    @FXML
    void refrech(ActionEvent event) {

    }

    @FXML
    void update(ActionEvent event) {

    }

}

