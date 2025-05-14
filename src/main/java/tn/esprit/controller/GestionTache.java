package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tn.esprit.models.Equipe;
import tn.esprit.models.MembresEquipe;
import tn.esprit.models.Tache;
//import tn.esprit.models.enums.Statut;
import tn.esprit.services.ServiceTache;
//choice box
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

public class GestionTache implements Initializable {

    @FXML
    private Label Error;

    @FXML
    private ListView<String> ListViewTaches;

    @FXML
    private TextField RechercheTache;

    @FXML
    private TextField description;

    @FXML
    private ComboBox<String> equipe_id;

    @FXML
    private TextField idfield;

    @FXML
    private ComboBox<String> membre_id;

    @FXML
    private Button sortTache;

    @FXML
    private CheckBox statusCheckBox;

    @FXML
    private ComboBox<String> tacheSort;

    @FXML
    private TextField titre;

    @FXML
    private CheckBox statut;

    @FXML
    private VBox taches_cards;


    //region init
    ServiceTache st = new ServiceTache();
    Connection cnx = MyDatabase.getInstance().getCnx();
    ObservableList<String> observableTacheList = FXCollections.observableArrayList();
    FilteredList<String> filteredTacheData;

    //
    private void loadEquipeChoiceBoxData(ComboBox comboBox) {

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
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //region combobox init
        loadEquipeChoiceBoxData(equipe_id);
        loadMembreChoiceBoxData(membre_id);
        //region sort taches
        tacheSort.getItems().addAll("Trier par ID", "Trier par Nom");
        tacheSort.setValue("Trier par ID");
        sortTache.setOnAction(event -> sortTache());
        //endregion

        //endregion

        //region recherche tache
        loadTacheData(observableTacheList);
        filteredTacheData = new FilteredList<>(observableTacheList, s -> true);
        ListViewTaches.setItems(filteredTacheData);
        RechercheTache.textProperty().addListener((observable, oldValue, newValue) -> {
            applyTacheFilter(newValue);
        });
        //endregion

    }


    private void add() {
        System.out.println("add started");

        try {
            String[] eqpParts = equipe_id.getValue().split("-", 2);
            int idEquipe = Integer.parseInt(eqpParts[0]);
            // String nomEqpModf = eqpParts[1]; // Unused

            String[] mbmrParts = membre_id.getValue().split("-", 2);
            int idMembr = Integer.parseInt(mbmrParts[0]);
            // String nomMbrModf = mbmrParts[1]; // Unused

            st.add(new Tache(
                        idEquipe,
                        titre.getText(),//string
                        description.getText(),//string
                        idMembr,
                        statusCheckBox.isSelected() // Changed from statut.getValue()
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
            // String nomEqpModf = eqpParts[1]; // Unused

            String[] mbmrParts = membre_id.getValue().split("-", 2);
            int idMembr = Integer.parseInt(mbmrParts[0]);
            // String nomMbrModf = mbmrParts[1]; // Unused

            st.update(new Tache(
                    id,
                    idEquipe,
                    titre.getText(),
                    description.getText(),
                    idMembr,
                    statusCheckBox.isSelected())); // Changed from statut.getValue()
        }catch(Exception e) {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }

    private List<Tache> getTacheById(int id) {
        List<Tache> taches = new ArrayList<>();
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
                p.setStatut(rs.getBoolean(6)); // Changed from rs.getString(6)

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
        if(idfield.getText().equals(""))
        {
            add();
        }
        else {
            try {
                int id = Integer.parseInt(idfield.getText());

                List<Tache> taches = getTacheById(id);
                if(taches.size()==1)
                {
                    // Populate form fields for update, including the checkbox
                    Tache tacheToEdit = taches.get(0);
                    titre.setText(tacheToEdit.getTitre());
                    description.setText(tacheToEdit.getDescription());
                    // equipe_id.setValue(...); // You might need to find the correct string format or Tache object
                    // membre_id.setValue(...); // Similar to equipe_id
                    statusCheckBox.setSelected(tacheToEdit.getStatut()); // Set checkbox state

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
    }

    @FXML
    void delete(ActionEvent event) {
        try{
            if(idfield.getText()!="")
            {
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
    void refrechByCard(ActionEvent event) {
        try{
            List<Tache> taches = st.getAll();
            taches_cards.getChildren().clear();
            for (int i = 0; i < taches.size(); i++) {
                FXMLLoader  loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("FrontOffice/GestionEquipe/TacheCard.fxml"));
                VBox cardBox = loader.load();
                TacheCard cardDeTache = loader.getController();
                //cardDeTache.setData(taches.get(i));
                taches_cards.getChildren().add(cardBox);
            }

        }catch (Exception e)
        {
            System.out.println("refrech error");
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }
    @FXML
    void refrech(ActionEvent event) {
        refreshTacheListView();
        applyTacheFilter(RechercheTache.getText());
    }

    //region recherche

    private void loadTacheData(ObservableList<String> list) {
        try{
            List<Tache> taches = st.getAll();
            for (Tache t : taches) {
                observableTacheList.add(t.getId()+" | "+t.getTitre()+" | "+t.getStatut()+" | ");
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            Error.setText(e.getMessage());
        }
    }
    public void refreshTacheListView() {
        observableTacheList.clear();
        loadTacheData(observableTacheList);
    }
    private void applyTacheFilter(String filterText) {
        filteredTacheData.setPredicate(item -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = filterText.toLowerCase();
            return item.toLowerCase().contains(lowerCaseFilter);
        });
    }

    //endregion

    //region sort
    @FXML
    void sortTache(ActionEvent event) {}

    /*
    private void sortTache() {
        String selectedOption = tacheSort.getValue();

        if (selectedOption == null) return;

        Comparator<String> comparator;

        if (selectedOption.equals("Trier par ID")) {
            comparator = Comparator.comparingInt(tache -> Integer.parseInt(tache.split(" \\| ")[0]));
        } else if (selectedOption.equals("Trier par Nom")) {
            comparator = Comparator.comparing(tache -> tache.split(" \\| ")[1]);
        }else {
            comparator = Comparator.comparingInt(tache -> Integer.parseInt(tache.split(" \\| ")[2]));
        }

        FXCollections.sort(observableTacheList, comparator);
    }
    /**/
    private void sortTache() {
        String selectedOption = tacheSort.getValue();

        if (selectedOption == null) return;

        System.out.println("Sorting tache by " + selectedOption);

        List<String> sortedList = observableTacheList.stream()
                .sorted((t1, t2) -> {
                    if (selectedOption.equals("Trier par ID")) {
                        return Integer.compare(
                                Integer.parseInt(t1.split(" \\| ")[0]),
                                Integer.parseInt(t2.split(" \\| ")[0])
                        );
                    } else if (selectedOption.equals("Trier par Nom")) {
                        return t1.split(" \\| ")[1].compareTo(t2.split(" \\| ")[1]);
                    } else {
                        return Integer.compare(
                                Integer.parseInt(t1.split(" \\| ")[2]),
                                Integer.parseInt(t2.split(" \\| ")[2])
                        );
                    }
                })
                .toList();

        observableTacheList.setAll(sortedList);
    }

    //endregion


}
