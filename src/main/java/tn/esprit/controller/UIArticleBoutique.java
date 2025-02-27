package tn.esprit.controller;

import esprit.tn.models.ArticleBoutique;
import esprit.tn.services.ServiceArticleBoutique;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import static esprit.tn.models.ArticleBoutique.Type.article;

public class UIArticleBoutique implements Initializable {


    @FXML
    private ListView<ArticleBoutique> article;
    @FXML
    private TextField nom;
    @FXML
    private ChoiceBox<ArticleBoutique.Type> type;
    @FXML
    private TextField prix;
    @FXML
    private TextField searchField;

    @FXML
    private List<ArticleBoutique> listOfArticleBoutique;








    public void ajouterArticle(ActionEvent actionEvent) {
        ServiceArticleBoutique serviceArticleBoutique = new ServiceArticleBoutique();
        BigDecimal bigDecimal = new BigDecimal(prix.getText());
        ArticleBoutique articleBoutique = new ArticleBoutique(nom.getText(),type.getValue(),bigDecimal);
        serviceArticleBoutique.add(articleBoutique);
        afficherArticle(actionEvent);
    }

    public void modiferArticle(ActionEvent actionEvent) {
        ServiceArticleBoutique serviceArticleBoutique = new ServiceArticleBoutique();
        ArticleBoutique articleBoutique = article.getSelectionModel().getSelectedItem();



        if (nom.getText().length()>0){
            articleBoutique.setNom(nom.getText());
        }
        if (prix.getText().length()>0){
          BigDecimal bigDecimal = new BigDecimal(prix.getText());
            articleBoutique.setPrix(bigDecimal);
        }
        if (type.getValue() != null){
            articleBoutique.setType(type.getValue());
        }
        serviceArticleBoutique.update(articleBoutique);
        afficherArticle(actionEvent);


    }

    public void supprimerArticle(ActionEvent actionEvent) {
        article.getSelectionModel().getSelectedItems().forEach(i -> new ServiceArticleBoutique().delete(i));
        afficherArticle(actionEvent);
    }

    public void afficherArticle(ActionEvent actionEvent) {
        listOfArticleBoutique = new ServiceArticleBoutique().getAll();
        article.getItems().clear();
        article.getItems().addAll(listOfArticleBoutique);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        type.getItems().addAll(ArticleBoutique.Type.values());
        listOfArticleBoutique = new ServiceArticleBoutique().getAll();
        article.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            nom.setText("");
            prix.setText("");
            type.setValue(null);
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
        article.getItems().clear();
        article.getItems().addAll( listOfArticleBoutique.stream().filter(a -> a.getNom().startsWith(searchField.getText())).toList());

    }


    public void trier(ActionEvent actionEvent) {
        article.getItems().clear();
        article.getItems().addAll( listOfArticleBoutique.stream().sorted((a,b)->a.getNom().compareTo(b.getNom())).toList());

    }
}



