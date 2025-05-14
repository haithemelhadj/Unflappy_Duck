package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import tn.esprit.models.Tache;

public class TacheCard {

    @FXML
    private Label id;

    @FXML
    private Label equipe;

    @FXML
    private Label titre;

    @FXML
    private Label description;

    @FXML
    private Label statut;

    //@FXML
    //private CheckBox statut;

    @FXML
    private Label responsable;
/*
    public void setData(Tache tache) {
        id.setText( String.valueOf(tache.getId()));
        equipe.setText( String.valueOf(tache.getEquipe_id()));
        responsable.setText(String.valueOf(tache.getId_responsable()));
        statut.setText(tache.getStatut());
        description.setText(tache.getDescription());
        titre.setText(tache.getTitre());
    }
    public Tache getData() {
        int tacheId = Integer.parseInt(id.getText());
        int tacheEquipe = Integer.parseInt(equipe.getText());
        int tacheResponsable = Integer.parseInt(responsable.getText());
        Tache tache = new Tache(tacheId,tacheEquipe,titre.getText(),description.getText(),tacheResponsable,statut);
        return tache;
    }
    /* */
}
