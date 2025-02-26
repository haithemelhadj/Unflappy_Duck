package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.models.*;
import tn.esprit.services.*;
import tn.esprit.utils.MyDatabase;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.*;
//import tn.esprit.models.enums.Statut;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
//choice box
import javafx.collections.transformation.FilteredList;

public class GestionEquipe implements Initializable {

    //region equipes

    @FXML
    private TextField idEquipe;

    @FXML
    private TextField NomEquipe;

    @FXML
    private ComboBox<String> id_Event;

    @FXML
    private ComboBox<String> idChefEquipe;

    @FXML
    private ListView<String> ListViewEquipes;

    @FXML
    private TextField RechercheEquipe;

    //endregion

    //region Membre

    @FXML
    private ComboBox<String> EquipesList;

    @FXML
    private ComboBox<String> UsersList;

    @FXML
    private ListView<String> ListViewMembres;

    @FXML
    private TextField RechercheMembre;

    //endregion

    @FXML
    private Label Error;

    //region init
    ServiceEquipe se = new ServiceEquipe();
    ServiceMembresEquipe sme = new ServiceMembresEquipe();
    Connection cnx = MyDatabase.getInstance().getCnx();
    ObservableList<String> observableEquipeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //region combobox
        //membre
        loadEquipesListChoiceBoxData(EquipesList);
        loadUsersListChoiceBoxData(UsersList);
        //equipe
        loadUsersListChoiceBoxData(idChefEquipe);
        loadid_EventChoiceBoxData(id_Event);
        //endregion


        loadEquipesData(observableEquipeList); // Load data from database

        FilteredList<String> filteredEqipData = new FilteredList<>(observableEquipeList, s -> true);

        RechercheEquipe.textProperty().addListener((observable, oldValue, newValue) -> {
            refreshEquipeListView();
            filteredEqipData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Bind filtered data to ListView
        ListViewEquipes.setItems(filteredEqipData);

        /*
        // Load Membre Data
        ObservableList<String> MembreData = FXCollections.observableArrayList();
        loadMembresData(MembreData); // Load members from database

        // Wrap data in a FilteredList
        FilteredList<String> filteredMmbrData = new FilteredList<>(MembreData, s -> true);

        // Add listener to search field
        RechercheMembre.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredMmbrData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Bind filtered data to ListView
        ListViewMembres.setItems(filteredMmbrData);
        /**/

    }


    // Function to load Membres data from the database
    private void loadMembresData(ObservableList<String> list) {
        String qry = "SELECT * FROM utilisateur"; // Modify if needed for members
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                list.add(rs.getInt("id") + "-" + rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    //
    private void loadEquipesListChoiceBoxData(ComboBox comboBox) {

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
    private void loadUsersListChoiceBoxData(ComboBox comboBox) {


        String qry ="SELECT * FROM utilisateur";

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
    private void loadid_EventChoiceBoxData(ComboBox comboBox) {
        String qry ="SELECT * FROM evenement";

        try {

            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                comboBox.getItems().add(rs.getInt("evenement_id")+"-"+rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }
    //
    //endregion

    //region methods equipe
    private void addEquipe() {
        System.out.println("add started");
        try {
            String[] evntParts = id_Event.getValue().split("-", 2);
            int idEvnt = Integer.parseInt(evntParts[0]);
            String nomEvntModf = evntParts[1];

            String[] chefParts = idChefEquipe.getValue().split("-", 2);
            int idChef = Integer.parseInt(chefParts[0]);
            String nomChefModf = chefParts[1];

            se.add(new Equipe(
                    idEvnt,
                    NomEquipe.getText(),//string
                    idChef
            ));
        }catch(Exception e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    private void updateEquipe(int id) {
        System.out.println("update started");
        try {
            String[] evntParts = id_Event.getValue().split("-", 2);
            int idEvnt = Integer.parseInt(evntParts[0]);
            String nomEvntModf = evntParts[1];

            String[] chefParts = idChefEquipe.getValue().split("-", 2);
            int idChef = Integer.parseInt(chefParts[0]);
            String nomChefModf = chefParts[1];
            se.update(new Equipe(
                    id,
                    idEvnt,
                    NomEquipe.getText(),//string
                    idChef
            ));
        }catch(Exception e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    private List<Equipe> getEquipeById(int id) {
        List<Equipe> equipes = new ArrayList<>();
        String qry="SELECT * FROM `equipe` WHERE `id` = "+ id;
        try{

            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()){
                Equipe e = new Equipe();
                e.setId(rs.getInt(1));
                e.setEvenement_id(rs.getInt(2));
                e.setNom(rs.getString(3));
                e.setChef_equipe_id(rs.getInt(4));

                equipes.add(e);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
        return equipes;
    }

    //endregion

    //region equipes
    @FXML
    void AddUpdateEquipe(ActionEvent event) {
        if(idEquipe.getText().equals(""))
        {
            addEquipe();
        }
        else {
            try {
                int id = Integer.parseInt(idEquipe.getText());

                List<Equipe> equipes = getEquipeById(id);
                if(equipes.size()==1)
                {
                    updateEquipe(id);
                }
                else if(equipes.isEmpty())
                {
                    addEquipe();
                }
                else
                {
                    System.out.println("too many equipes found");
                    Error.setText("too many equipes found");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Error.setText(e.getMessage());
            }
        }

    }

    @FXML
    void deleteEquipe(ActionEvent event) {
        try{
            if(idEquipe.getText()!="")
            {
                int id = Integer.parseInt(idEquipe.getText());
                List<Equipe> equipes = getEquipeById(id);
                if(equipes.isEmpty())
                {
                    Error.setText("no taches found");
                }
                else if(equipes.size()==1)
                {
                    se.delete(equipes.get(0));
                }
                else
                {
                    System.out.println("too many equipes found");
                    Error.setText("too many equipes found");
                }
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }

    }


    // Function to load Equipes data from the database
    private void loadEquipesData(ObservableList<String> list) {
        try{
            List<Equipe> equipes = se.getAll();
            for (Equipe e : equipes) {
                observableEquipeList.add(e.getId()+" | "+e.getNom()+" | "); // Change this to whatever field you want to display
            }
        }catch (Exception e)
        {
            System.out.println("refrech error");
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }
    public void refreshEquipeListView() {
        observableEquipeList.clear(); // Clear existing data instead of creating a new list
        loadEquipesData(observableEquipeList); // Reload data from the database
        ListViewEquipes.setItems(observableEquipeList);
    }

    @FXML
    void refrechEquipes(ActionEvent event) {
        refreshEquipeListView();

        /*
        try{
            List<Equipe> equipes = se.getAll();
            ObservableList<String> observableList = FXCollections.observableArrayList();

            for (Equipe e : equipes) {
                observableList.add(e.getId()+" | "+e.getNom()+" | "); // Change this to whatever field you want to display
            }
            ListViewEquipes.setItems(observableList);

        }catch (Exception e)
        {
            System.out.println("refrech error");
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
        /**/

    }
    //endregion

    //region members
    @FXML
    void AddMember(ActionEvent event) {
        System.out.println("adding member");
        String[] usrsParts = UsersList.getValue().split("-", 2);
        int idUsr = Integer.parseInt(usrsParts[0]);
        String nomUsrModf = usrsParts[1];
        try {
            System.out.println("trying to add member");
            List<MembresEquipe> mmbrsEquipes = getMembreById(idUsr);
            if(mmbrsEquipes.size()==1)
            {
                updateMembre(idUsr);
            }
            else if(mmbrsEquipes.isEmpty())
            {
                addMembre();
                System.out.println("member added");
            }
            else
            {
                System.out.println("too many members found");
                Error.setText("too many members found");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    @FXML
    void EffacerMembre(ActionEvent event) {
        try{
            String[] usrsParts = UsersList.getValue().split("-", 2);
            int idUsr = Integer.parseInt(usrsParts[0]);
            String nomUsrModf = usrsParts[1];
            List<MembresEquipe> mmbrsEquipes = getMembreById(idUsr);
            if(mmbrsEquipes.isEmpty())
            {
                Error.setText("no members found");
            }
            else if(mmbrsEquipes.size()==1)
            {
                sme.delete(mmbrsEquipes.get(0));
            }
            else
            {
                System.out.println("too many members found");
                Error.setText("too many members found");
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    @FXML
    void refrechMembres(ActionEvent event) {
        try{
            List<MembresEquipe> mmbrsEquipes = sme.getAll();
            ObservableList<String> observableList = FXCollections.observableArrayList();
            observableList.add("utilisateur_id"+" | "+"nom"+" | "+"equipe_id");
            for (MembresEquipe me : mmbrsEquipes) {

                String qry ="SELECT * FROM membre_equipe m JOIN utilisateur u ON m.utilisateur_id = u.id WHERE m.id = '"+me.getId()+"'";
                try {
                    Statement stm = cnx.createStatement();
                    ResultSet rs = stm.executeQuery(qry);


                    while (rs.next()){
                        observableList.add(rs.getInt("utilisateur_id")+" | "+rs.getString("nom")+" | "+rs.getString("equipe_id"));
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    Error.setText(e.getMessage());
                }
            }
            ListViewMembres.setItems(observableList);

        }catch (Exception e)
        {
            System.out.println("refrech error");
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }

    }


    //endregion

    //region methods membres
    private void addMembre() {
        System.out.println("add started");

        try {
            String[] eqpParts = EquipesList.getValue().split("-", 2);
            int idEqp = Integer.parseInt(eqpParts[0]);
            String nomEqpModf = eqpParts[1];

            String[] usrsParts = UsersList.getValue().split("-", 2);
            int idUsr = Integer.parseInt(usrsParts[0]);
            String nomUsrModf = usrsParts[1];

            sme.add(new MembresEquipe(
                    idEqp,
                    idUsr,
                    "Membre"
            ));
        }catch(Exception e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    private void updateMembre(int id) {
        System.out.println("update started");
        try {
            String[] eqpParts = EquipesList.getValue().split("-", 2);
            int idEqp = Integer.parseInt(eqpParts[0]);
            String nomEqpModf = eqpParts[1];

            String[] usrsParts = UsersList.getValue().split("-", 2);
            int idUsr = Integer.parseInt(usrsParts[0]);
            String nomUsrModf = usrsParts[1];

            sme.update(new MembresEquipe(
                    id,
                    idEqp,
                    idUsr,
                    "Membre"
            ));
        }catch(Exception e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    private List<MembresEquipe> getMembreById(int id) {
        List<MembresEquipe> mmbrEquipes = new ArrayList<>();
        String qry="SELECT * FROM `membre_equipe` WHERE `utilisateur_id` = "+ id;
        try{

            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()){
                MembresEquipe e = new MembresEquipe();
                e.setId(rs.getInt(1));
                e.setEquipe_id(rs.getInt(2));
                e.setUtilisateur_id(rs.getInt(3));
                e.setRole(rs.getString(4));

                mmbrEquipes.add(e);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
        return mmbrEquipes;
    }

    //endregion



}
