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
import java.util.Comparator;
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

    @FXML
    private ComboBox<String> equipeSort;


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

    @FXML
    private ComboBox<String> memberSort;

    @FXML
    private Button sortEquipe;

    @FXML
    private Button sortMembre;


    //endregion

    @FXML
    private Label Error;

    //region init
    ServiceEquipe se = new ServiceEquipe();
    ServiceMembresEquipe sme = new ServiceMembresEquipe();
    Connection cnx = MyDatabase.getInstance().getCnx();
    //recherche equipe
    ObservableList<String> observableEquipeList = FXCollections.observableArrayList();
    FilteredList<String> filteredEqipData;
    //recherche membre
    ObservableList<String> observableMembreList = FXCollections.observableArrayList();
    FilteredList<String> filteredMmbrData;


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

        //region recherche equipe
        // Load initial data
        loadEquipesData(observableEquipeList); // Load data from database
        // Wrap observable list in a FilteredList
        filteredEqipData = new FilteredList<>(observableEquipeList, s -> true);
        // Bind filtered data to ListView
        ListViewEquipes.setItems(filteredEqipData);
        // Listen for search field changes
        RechercheEquipe.textProperty().addListener((observable, oldValue, newValue) -> {
            applyEquipeFilter(newValue);
        });
        //endregion

        //region recherche membre
        // Load initial data
        loadMembreData(observableMembreList); // Load data from database
        // Wrap observable list in a FilteredList
        filteredMmbrData = new FilteredList<>(observableMembreList, s -> true);
        // Bind filtered data to ListView
        ListViewMembres.setItems(filteredMmbrData);
        // Listen for search field changes
        RechercheMembre.textProperty().addListener((observable, oldValue, newValue) -> {
            applyMemberFilter(newValue);
        });
        //endregion

        //region sort equipe
        equipeSort.getItems().addAll("Trier par ID", "Trier par Nom");
        equipeSort.setValue("Trier par ID"); // Default selection

        sortEquipe.setOnAction(event -> sortEquipes());
        //endregion

        //region sort Membre
        memberSort.getItems().addAll("Trier par ID", "Trier par Nom","Trier par equipe");
        memberSort.setValue("Trier par ID"); // Default selection

        sortMembre.setOnAction(event -> sortMembres());
        //endregion

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

    //region equipes

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
        observableEquipeList.clear(); // Clear existing data
        loadEquipesData(observableEquipeList); // Reload data from the database
    }
    private void applyEquipeFilter(String filterText) {
        filteredEqipData.setPredicate(item -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = filterText.toLowerCase();
            return item.toLowerCase().contains(lowerCaseFilter);
        });
    }

    @FXML
    void refrechEquipes(ActionEvent event) {
        refreshEquipeListView();
        applyEquipeFilter(RechercheEquipe.getText()); // Reapply the filter after refresh
    }

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

    //region members

    private void loadMembreData(ObservableList<String> list) {
        try{
            List<MembresEquipe> mmbrsEquipes = sme.getAll();

            ObservableList<String> observableList = FXCollections.observableArrayList();
            //observableList.add("utilisateur_id"+" | "+"nom"+" | "+"equipe_id");
            for (MembresEquipe me : mmbrsEquipes) {

                String qry = "SELECT * FROM membre_equipe m JOIN utilisateur u ON m.utilisateur_id = u.id WHERE m.id = '" + me.getId() + "'";
                try {
                    Statement stm = cnx.createStatement();
                    ResultSet rs = stm.executeQuery(qry);

                    while (rs.next()) {
                        observableMembreList.add(rs.getInt("utilisateur_id") + " | " + rs.getString("nom") + " | " + rs.getString("equipe_id"));
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    Error.setText(e.getMessage());
                }
            }
            //ListViewMembres.setItems(observableList);
        }catch (Exception e)
        {
            System.out.println("refrech error");
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }
    public void refreshMemberListView() {
        observableMembreList.clear(); // Clear existing data
        loadMembreData(observableMembreList); // Reload data from the database
    }
    private void applyMemberFilter(String filterText) {
        filteredMmbrData.setPredicate(item -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = filterText.toLowerCase();
            return item.toLowerCase().contains(lowerCaseFilter);
        });
    }

    @FXML
    void refrechMembres(ActionEvent event) {
        refreshMemberListView();
        applyMemberFilter(RechercheMembre.getText()); // Reapply the filter after refresh

        /*
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
        /**/

    }


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

    private void sortEquipes() {
        String selectedOption = equipeSort.getValue();

        if (selectedOption == null) return;

        Comparator<String> comparator;

        if (selectedOption.equals("Trier par ID")) {
            comparator = Comparator.comparingInt(equipe -> Integer.parseInt(equipe.split(" \\| ")[0])); // Extract ID
        } else { // Sort by Name
            comparator = Comparator.comparing(equipe -> equipe.split(" \\| ")[1]); // Extract Name
        }

        FXCollections.sort(observableEquipeList, comparator);
    }

    private void sortMembres() {
        String selectedOption = memberSort.getValue();

        if (selectedOption == null) return;

        Comparator<String> comparator;

        if (selectedOption.equals("Trier par ID")) {
            comparator = Comparator.comparingInt(membre -> Integer.parseInt(membre.split(" \\| ")[0]));
            System.out.println("sorting mmbrs by id");
        } else if (selectedOption.equals("Trier par Nom")) {
            comparator = Comparator.comparing(membre -> membre.split(" \\| ")[1]);
            System.out.println("sorting mmbrs by name");
        }else {
            comparator = Comparator.comparingInt(membre -> Integer.parseInt(membre.split(" \\| ")[2]));
            System.out.println("sorting mmbrs by equipe");
        }

        FXCollections.sort(observableMembreList, comparator);
    }



    @FXML
    void sortEquipes(ActionEvent event) {

    }

    @FXML
    void sortMembres(ActionEvent event) {

    }
/**/
}
