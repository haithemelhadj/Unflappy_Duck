package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.interfaces.IService;
import tn.esprit.models.utilisateur;
import tn.esprit.services.ServiceUtilisateur;

public class GestionPersonne {
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfAge;

    IService<utilisateur> sp = new ServiceUtilisateur();
    @FXML
    private Label lbPersonnes;

    @FXML
    public void ajouterPersonne(ActionEvent actionEvent) {
    utilisateur p = new utilisateur();
    p.setNom(tfNom.getText());
    p.setPrenom(tfPrenom.getText());
    p.setAge(Integer.parseInt(tfAge.getText()));

    sp.add(p);
    }

    @FXML
    public void afficherPersonnes(ActionEvent actionEvent) {

    lbPersonnes.setText(sp.getAll().toString());
    }
}
