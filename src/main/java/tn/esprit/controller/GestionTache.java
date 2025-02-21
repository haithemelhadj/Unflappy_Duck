package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import tn.esprit.models.Tache;
import tn.esprit.models.enums.Statut;
import tn.esprit.models.enums.userRoles;
import tn.esprit.models.utilisateur;
import tn.esprit.services.ServiceTache;
import tn.esprit.services.ServiceUtilisateur;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class GestionTache {

    @FXML
    private TextField description;

    @FXML
    private ChoiceBox<Integer> equipe_id;

    @FXML
    private ChoiceBox<Integer> membre_id;

    @FXML
    private TextField titre;

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    //private TextField taches_list;
    private ListView<String> taches_list;

    ServiceTache st = new ServiceTache();
    @FXML
    void add(ActionEvent event) {
        //(int equipe_id, String titre, String description, int id_responsable, Statut statut)
        try {
            st.add(new Tache(equipe_id.getValue(),
                    titre.getText(), description.getText(),
                    membre_id.getValue(),
                    Statut.valueOf(statut.getValue())));
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void update(ActionEvent event) {
        try {
            st.update(new Tache(equipe_id.getValue(),
                    titre.getText(), description.getText(),
                    membre_id.getValue(),
                    Statut.valueOf(statut.getValue())));
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void delete(ActionEvent event) {
        String selectedItem = taches_list.getSelectionModel().getSelectedItem();
        int selectedIndex = taches_list.getSelectionModel().getSelectedIndex();
        st.delete(new Tache().fromString(selectedItem));
    }

    @FXML
    void refrech(ActionEvent event) {

        // Convert List to ObservableList
        ObservableList<String> observableList = FXCollections.observableArrayList(st.getAll().toString());
        // Set the ListView items
        taches_list.setItems(observableList);
    }



}
