package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Equipe;
import tn.esprit.models.Tache;
//import tn.esprit.models.enums.Statut;
import tn.esprit.models.enums.userRoles;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceEquipe;
import tn.esprit.services.ServiceTache;
import tn.esprit.services.ServiceUtilisateur;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
//choice box
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import tn.esprit.utils.MyDatabase;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GestionTache implements Initializable {

    @FXML
    private TextField description;

    @FXML
    private ChoiceBox<String> equipe_id;

    @FXML
    private ChoiceBox<String> membre_id;


    @FXML
    private TextField titre;

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    //private TextField taches_list;
    private ListView<String> taches_list;

    @FXML
    private VBox taches_cards;


    ServiceTache st = new ServiceTache();
    //region init choicebox
    //choice box init
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEquipeChoiceBoxData(equipe_id);
        loadMembreChoiceBoxData(membre_id);
        loadStatutChoiceBoxData(statut);
    }
    //
    private void loadEquipeChoiceBoxData(ChoiceBox choiceBox) {

            Connection cnx = MyDatabase.getInstance().getCnx();
            String qry ="SELECT * FROM `equipe`";

            try {
                Statement stm = cnx.createStatement();
                ResultSet rs = stm.executeQuery(qry);

                while (rs.next()){
                    choiceBox.getItems().add(rs.getString("id"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }
    //
    private void loadMembreChoiceBoxData(ChoiceBox choiceBox) {

        Connection cnx = MyDatabase.getInstance().getCnx();
        String qry ="SELECT * FROM `membre_equipe` WHERE role = 'membre'";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                choiceBox.getItems().add(rs.getString("utilisateur_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //
    private void loadStatutChoiceBoxData(ChoiceBox choiceBox) {
        statut.getItems().add("en_attente");
        statut.getItems().add("en_cours");//'en_attente','en_cours','termine'
        statut.getItems().add("termine");
    }
    //
    //endregion

    @FXML
    void add(ActionEvent event) {
        System.out.println("add started");

        try {
            System.out.println("adding: "+equipe_id.getValue()+"/"+titre.getText()+"/"+description.getText()+"/"+membre_id.getValue()+"/"+statut.getValue());
            st.add(new Tache(
                        Integer.parseInt(equipe_id.getValue()),//int
                        titre.getText(),//string
                        description.getText(),//string
                        Integer.parseInt(membre_id.getValue())//int
                        , statut.getValue()//String
                ));

        }catch(Exception e) {
            System.out.println("huge int failure :(");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    @FXML
    void update(ActionEvent event) {
        System.out.println("update started");
        try {
            System.out.println("updating: "+equipe_id.getValue()+"/"+titre.getText()+"/"+description.getText()+"/"+membre_id.getValue()+"/"+statut.getValue());
            st.update(new Tache(
                    Integer.parseInt(equipe_id.getValue()),
                    titre.getText(),
                    description.getText(),
                    Integer.parseInt(membre_id.getValue()),
                    statut.getValue()));
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void delete(ActionEvent event) {
        try{
            //System.out.println("deleting: "+equipe_id.getValue()+"/"+titre.getText()+"/"+description.getText()+"/"+membre_id.getValue()+"/"+statut.getValue());
            String selectedItem = taches_list.getSelectionModel().getSelectedItem();
            //Tache tch= new Tache()
            int selectedIndex = taches_list.getSelectionModel().getSelectedIndex();
            System.out.println("selectedItem: "+selectedItem);
            System.out.println("selectedIndex: "+selectedIndex);
            st.delete(new Tache().fromString(selectedItem));
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void refrech(ActionEvent event) {
        try{
            System.out.println("getting taches");

            // Get the list of tasks from the service
            List<Tache> taches = st.getAll();
            for (int i = 0; i < taches.size(); i++) {
                //System.out.println("getting tache :"+i);
                FXMLLoader  loader = new FXMLLoader();
                //System.out.println("getting tache :"+i);
                loader.setLocation(getClass().getResource("/TacheCard.fxml"));
                //loader.setLocation(getClass().getClassLoader().getResource("/TacheCard.fxml"));
                System.out.println("getting tache :"+i);
                VBox cardBox = loader.load();
                //HBox cardBox = loader.load();
                System.out.println("getting tache :"+i);
                TacheCard card = loader.getController();
                System.out.println("getting tache :"+i);
                card.setData(taches.get(i));
                System.out.println("getting tache :"+i);
                taches_cards.getChildren().add(cardBox);
                System.out.println("getting tache :"+i);
            }
            /*
            // Convert each Tache object to a meaningful String representation
            ObservableList<String> observableList = FXCollections.observableArrayList();
            for (Tache t : taches) {
                observableList.add(t.getTitre()+" | "+t.getDescription()+" | "+t.getId_responsable()); // Change this to whatever field you want to display
            }
            // Set the ListView items
            taches_list.setItems(observableList);
            /**/

        }catch (Exception e)
        {
            System.out.println("refrech error");
            System.out.println(e.getMessage());
        }
    }



}
