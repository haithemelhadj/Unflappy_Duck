package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.stream.IntStream;

public class GestionTache implements Initializable {

    @FXML
    private TextField idfield;

    @FXML
    private TextField titre;

    @FXML
    private TextField description;

    @FXML
    private ComboBox<String> equipe_id;

    @FXML
    private ComboBox<String> membre_id;

    @FXML
    private ComboBox<String> statut;

    @FXML
    private VBox taches_cards;

    @FXML
    private Label Error;


    ServiceTache st = new ServiceTache();
    //region init combobox
    //combo box init
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEquipeChoiceBoxData(equipe_id);
        loadMembreChoiceBoxData(membre_id);
        loadStatutChoiceBoxData(statut);
    }
    //
    private void loadEquipeChoiceBoxData(ComboBox comboBox) {

            Connection cnx = MyDatabase.getInstance().getCnx();
            String qry ="SELECT * FROM `equipe`";
            //String qry ="SELECT u.nom FROM tache m JOIN equipe u ON m.equipe_id = u.id";
            try {
                Statement stm = cnx.createStatement();
                ResultSet rs = stm.executeQuery(qry);

                while (rs.next()){
                    comboBox.getItems().add(rs.getInt("id")+"-"+rs.getString("nom"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                Error.setText(e.getMessage());
            }
    }
    //
    private void loadMembreChoiceBoxData(ComboBox comboBox) {

        Connection cnx = MyDatabase.getInstance().getCnx();
        String qry ="SELECT * FROM membre_equipe m JOIN utilisateur u ON m.utilisateur_id = u.id WHERE m.role = 'membre'";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                comboBox.getItems().add(rs.getInt("utilisateur_id")+"-"+rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }
    //
    private void loadStatutChoiceBoxData(ComboBox comboBox) {
        statut.getItems().add("en_attente");
        statut.getItems().add("en_cours");//'en_attente','en_cours','termine'
        statut.getItems().add("termine");
    }
    //
    //endregion

    private void add() {
        System.out.println("add started");

        try {
            String[] eqpParts = equipe_id.getValue().split("-", 2);
            int idEquipe = Integer.parseInt(eqpParts[0]);
            String nomEqpModf = eqpParts[1];

            String[] mbmrParts = membre_id.getValue().split("-", 2);
            int idMembr = Integer.parseInt(mbmrParts[0]);
            String nomMbrModf = mbmrParts[1];

            st.add(new Tache(
                        idEquipe,
                        titre.getText(),//string
                        description.getText(),//string
                        idMembr
                        ,statut.getValue()//String
                ));
        }catch(Exception e) {
            System.out.println("huge int failure :(");
            e.printStackTrace();
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    private void update(int id) {
        System.out.println("update started");
        try {
            String[] eqpParts = equipe_id.getValue().split("-", 2);
            int idEquipe = Integer.parseInt(eqpParts[0]);
            String nomEqpModf = eqpParts[1];

            String[] mbmrParts = membre_id.getValue().split("-", 2);
            int idMembr = Integer.parseInt(mbmrParts[0]);
            String nomMbrModf = mbmrParts[1];

            st.update(new Tache(
                    id,
                    idEquipe,
                    titre.getText(),
                    description.getText(),
                    idMembr,
                    statut.getValue()));
        }catch(Exception e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    private List<Tache> getTacheById(int id) {
        List<Tache> taches = new ArrayList<>();
        Connection cnx = MyDatabase.getInstance().getCnx();
        String qry="SELECT * FROM `tache` WHERE `id` = "+ id;
        try{
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()){
                Tache p = new Tache();
                p.setId(rs.getInt(1));
                p.setEquipe_id(rs.getInt(2));
                p.setTitre(rs.getString(3));
                p.setDescription(rs.getString(4));
                p.setId_responsable(rs.getInt(5));
                p.setStatut(rs.getString(6));

                taches.add(p);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
        return taches;
    }

    @FXML
    void AddUpdate(ActionEvent event) {
        //if id field is empty add new task
        if(idfield.getText().equals(""))
        {
            add();
        }
        else {
            try {
                //get in id and check if it exists
                int id = Integer.parseInt(idfield.getText());

                List<Tache> taches = getTacheById(id);
                if(taches.size()==1)
                {
                    update(id);
                }
                else if(taches.isEmpty())
                {
                    add();
                }
                else
                {
                    System.out.println("too many taches found");
                    Error.setText("too many taches found");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Error.setText(e.getMessage());
            }
        }

        //update();
    }

    @FXML
    void delete(ActionEvent event) {
        try{
            //if id field is empty add new task
            if(idfield.getText()!="")
            {
                //get in id and check if it exists
                int id = Integer.parseInt(idfield.getText());
                List<Tache> taches = getTacheById(id);
                if(taches.isEmpty())
                {
                    Error.setText("no taches found");
                }
                else if(taches.size()==1)
                {
                    st.delete(taches.get(0));
                }
                else
                {
                    System.out.println("too many taches found");
                    Error.setText("too many taches found");
                }
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }

    }

    @FXML
    void refrech(ActionEvent event) {
        try{
            List<Tache> taches = st.getAll();
            taches_cards.getChildren().clear();
            for (int i = 0; i < taches.size(); i++) {
                FXMLLoader  loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("TacheCard.fxml"));
                VBox cardBox = loader.load();
                TacheCard cardDeTache = loader.getController();
                cardDeTache.setData(taches.get(i));
                taches_cards.getChildren().add(cardBox);
            }

        }catch (Exception e)
        {
            System.out.println("refrech error");
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }



}
