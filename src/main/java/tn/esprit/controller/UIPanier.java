package tn.esprit.controller;

import esprit.tn.models.ArticleBoutique;
import esprit.tn.models.Panier;
import esprit.tn.services.ServiceArticleBoutique;
import esprit.tn.services.ServicePanier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UIPanier implements Initializable {


    public TextField quantite;

    public TextField utilisateur_id;
    public ListView<esprit.tn.models.Panier> panier;
    public ChoiceBox<Integer> article_id;
    public TextField searchfilde;
    @FXML
    private List<Panier> listofpanier;


    public void ajouterPanier(ActionEvent actionEvent) {

        ServicePanier servicePanier = new ServicePanier();
        int utilisateurId = Integer.parseInt(utilisateur_id.getText().trim());
        int quantiteValue = Integer.parseInt(quantite.getText().trim());
       Panier panier = new Panier (utilisateurId, article_id.getValue(),quantiteValue);
       servicePanier.add( panier);
       afficherPanier(actionEvent);
        listofpanier = new ServicePanier().getAll();

    }

    public void modiferPanier(ActionEvent actionEvent) {
       ServicePanier servicePanier = new ServicePanier();
       Panier panier1=panier.getSelectionModel().getSelectedItem();

        if (!utilisateur_id.getText().isEmpty()){
            panier1.setUtilisateur_id(Integer.parseInt(utilisateur_id.getText()));
        }
        if (article_id.getValue()!=null){
            panier1.setArticle_id(article_id.getValue());
        }
        if (!quantite.getText().isEmpty()){
            panier1.setQuantite(Integer.parseInt(quantite.getText()));
        }

        servicePanier.update(panier1);
        afficherPanier(actionEvent);
        listofpanier = new ServicePanier().getAll();

    }

    public void supprimerPanier(ActionEvent actionEvent) {
        panier.getSelectionModel().getSelectedItems().forEach(i -> new ServicePanier().delete(i));
        afficherPanier(actionEvent);
        listofpanier = new ServicePanier().getAll();
    }

    public void afficherPanier(ActionEvent actionEvent) {

        panier.getItems().clear();
        List<Panier> p = new ServicePanier().getAll();
        panier.getItems().addAll(p);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ServiceArticleBoutique serviceArticleBoutique=new ServiceArticleBoutique();
        listofpanier = new ServicePanier().getAll();
        article_id.getItems().addAll(serviceArticleBoutique.getAll().stream().map(a->a.getId()).toList());

        panier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            quantite.setText("");
            article_id.setValue(null);
            utilisateur_id.setText("");
        });
    }


    public void switch_to_home(ActionEvent actionEvent) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;

        FXMLLoader loader =new FXMLLoader(getClass().getResource("/Home.fxml"));
        root = loader.load();
        stage=(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


    public void recherche(ActionEvent actionEvent) {
      panier.getItems().clear();
      panier.getItems().addAll(listofpanier.stream().filter(a->a.getArticle_id()==Integer.parseInt(searchfilde.getText().trim())).toList());
    }

    public void trier(ActionEvent actionEvent) {
        panier.getItems().clear();
        panier.getItems().addAll( listofpanier.stream().sorted((a,b)->a.getQuantite()-b.getQuantite()).toList());
    }
}
