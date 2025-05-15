package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import tn.esprit.models.Utilisateur;

import java.time.LocalDate;

public class ListUtilisateur {

    @FXML private TableView<Utilisateur> tableView;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colPrenom;
    @FXML private TableColumn<Utilisateur, String> colEmail;
    @FXML private TableColumn<Utilisateur, String> colRole;
    @FXML private TableColumn<Utilisateur, LocalDate> colDateNaissance;
    @FXML private TableColumn<Utilisateur, String> colTelephone;
    @FXML private TableColumn<Utilisateur, String> colAdresse;
    @FXML private TableColumn<Utilisateur, String> colStatut;
    private final ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
   /*
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));



        tableView.setItems(utilisateurs);
*/
    }

    @FXML
    public void handleAjouter(ActionEvent actionEvent) {

        System.out.println("Ajouter utilisateur");
        // TODO: Add logic or open dialog
        /**/
    }

    @FXML
    public void handleModifier(ActionEvent actionEvent) {

        Utilisateur selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Modifier utilisateur: " + selected.getNom());
            // TODO: Modify logic or open dialog
        } else {
            System.out.println("Veuillez sélectionner un utilisateur.");
        }
        /**/
    }
}
