package tn.esprit.controller;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import tn.esprit.models.ArticleBoutique;
import tn.esprit.services.ServiceArticleBoutique;
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
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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

    @FXML
    public GridPane itemlist;
    private List<ArticleBoutique> q;


    public void show() {
        ServiceArticleBoutique serviceArticleBoutique = new ServiceArticleBoutique();
        q = serviceArticleBoutique.getAll();

        int col = 0;
        int row = 1;
        for (ArticleBoutique qzz : q) {


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionStore/storeItem.fxml"));
            try {
                AnchorPane p = fxmlLoader.load();
                UIItemArticleController cntrll = fxmlLoader.getController();
                cntrll.setDATA(qzz, this);
                if (col == 3) {
                    col = 0;
                    row++;
                }
                itemlist.add(p, col++, row);
                GridPane.setMargin(p, new Insets(10));
                //  itemlist.getChildren().add(p);
            } catch (
                    IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }




    public void ajouterArticle(ActionEvent actionEvent) {
        ServiceArticleBoutique serviceArticleBoutique = new ServiceArticleBoutique();
        BigDecimal bigDecimal = new BigDecimal(prix.getText());
        ArticleBoutique articleBoutique = new ArticleBoutique(nom.getText(),type.getValue(),bigDecimal);
        serviceArticleBoutique.add(articleBoutique);
        afficherArticle(actionEvent);
    }
    public void afficherArticle(ActionEvent actionEvent) {
        listOfArticleBoutique = new ServiceArticleBoutique().getAll();
        article.getItems().clear();
        article.getItems().addAll(listOfArticleBoutique);
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
        show();
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

        show();


    }
    private void displayArticle(List<ArticleBoutique> articleBoutiques) {
        int col = 0;
        int row = 1;
        for (ArticleBoutique qzz : articleBoutiques) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FrontOffice/GestionStore/storeitem.fxml"));
            try {
                AnchorPane p = fxmlLoader.load();
                UIItemArticleController cntrll = fxmlLoader.getController();
                cntrll.setDATA(qzz, this);
                if (col == 3) {
                    col = 0;
                    row++;
                }
                itemlist.add(p, col++, row);
                GridPane.setMargin(p, new Insets(10));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }




    public void switch_to_home(ActionEvent actionEvent) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;

        FXMLLoader loader =new FXMLLoader(getClass().getResource("/FrontOffice/GestionStore/Store.fxml"));
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
    public void recherche1(ActionEvent actionEvent) {
        itemlist.getChildren().clear();  // Clear all items from the GridPane
        List<ArticleBoutique> filteredArticles = listOfArticleBoutique.stream()
                .filter(a -> a.getNom().startsWith(searchField.getText()))
                .toList();
        displayArticle(filteredArticles);  // Use displayArticle to add filtered items


    }


    public void trier1(ActionEvent actionEvent) {
        itemlist.getChildren().clear();  // Clear all items from the GridPane
        List<ArticleBoutique> sortedArticles = listOfArticleBoutique.stream()
                .sorted((a, b) -> a.getNom().compareTo(b.getNom()))
                .toList();
        displayArticle(sortedArticles);  // Use displayArticle to add sorted items


    }
}



