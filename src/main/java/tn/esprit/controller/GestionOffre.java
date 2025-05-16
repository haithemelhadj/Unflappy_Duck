package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestionOffre implements Initializable {

    // Liste des offres
    @FXML
    private ListView<String> offres;
    
    // Champs pour l'ID
    @FXML
    private Text offre_id;
    
    // Champs pour les détails de l'offre
    @FXML
    private TextField text_entreprise_id;
    
    @FXML
    private TextField text_duree;
    
    @FXML
    private TextField text_titre;
    
    @FXML
    private TextField text_budget;
    
    @FXML
    private ChoiceBox<String> statut;
    
    @FXML
    private DatePicker date_publication;
    
    @FXML
    private TextArea text_description;
    
    @FXML
    private TextArea text_competences;
    
    // Boutons d'action
    @FXML
    private Button ajouter;
    
    @FXML
    private Button update;
    
    @FXML
    private Button supprimer;
    
    // Liste observable pour stocker les offres (simulation)
    private ObservableList<String> offresList = FXCollections.observableArrayList(
        "Offre{id=1001, entreprise=1201, titre='Développement application mobile', budget=5000}",
        "Offre{id=1002, entreprise=1305, titre='Création site e-commerce', budget=3500}",
        "Offre{id=1003, entreprise=1178, titre='Design UX/UI plateforme web', budget=2800}",
        "Offre{id=1004, entreprise=1422, titre='Développement API REST', budget=4200}",
        "Offre{id=1005, entreprise=1106, titre='Intégration système CRM', budget=6500}"
    );
    
    // Statuts possibles
    private final String[] statusOptions = {"Ouvert", "En cours", "Attribué", "Terminé", "Annulé"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser la liste des offres
        offres.setItems(offresList);
        
        // Configurer le ChoiceBox des statuts
        statut.getItems().addAll(statusOptions);
        
        // Formater le DatePicker
        configureDatePicker();
        
        // Ajouter un listener pour afficher les détails quand une offre est sélectionnée
        offres.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                afficherDetailsOffre(newValue);
            }
        });
        
        // Sélectionner la première offre par défaut
        if (!offresList.isEmpty()) {
            offres.getSelectionModel().select(0);
        }
    }
    
    /**
     * Configure le format du DatePicker
     */
    private void configureDatePicker() {
        // Définir le format de date
        StringConverter<LocalDate> converter = new StringConverter<>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        
        date_publication.setConverter(converter);
        date_publication.setPromptText("jj/mm/aaaa");
    }
    
    /**
     * Affiche les détails d'une offre sélectionnée
     * @param offreString représentation de l'offre sélectionnée
     */
    private void afficherDetailsOffre(String offreString) {
        // Extraire l'ID de l'offre (exemple: "Offre{id=1001, ...")
        String idPart = offreString.substring(offreString.indexOf("id=") + 3);
        String id = idPart.substring(0, idPart.indexOf(","));
        offre_id.setText(id);
        
        // Simuler le chargement des données de l'offre
        if (id.equals("1001")) {
            text_entreprise_id.setText("1201");
            text_titre.setText("Développement application mobile");
            text_budget.setText("5000");
            text_duree.setText("45");
            statut.setValue("Ouvert");
            date_publication.setValue(LocalDate.now().minusDays(5));
            text_description.setText("Nous recherchons un développeur mobile expérimenté pour créer une application iOS et Android pour notre service de livraison. L'application doit permettre aux utilisateurs de commander, suivre leur livraison en temps réel et payer en ligne.");
            text_competences.setText("- Développement natif iOS (Swift)\n- Développement natif Android (Kotlin)\n- Intégration API REST\n- Expérience en UI/UX mobile\n- Connaissance des systèmes de paiement en ligne");
        } else if (id.equals("1002")) {
            text_entreprise_id.setText("1305");
            text_titre.setText("Création site e-commerce");
            text_budget.setText("3500");
            text_duree.setText("30");
            statut.setValue("En cours");
            date_publication.setValue(LocalDate.now().minusDays(15));
            text_description.setText("Conception et développement d'un site e-commerce complet pour une boutique de vêtements. Le site doit inclure un catalogue produits, un système de panier, l'intégration de paiement et un espace client.");
            text_competences.setText("- Maîtrise de WooCommerce ou PrestaShop\n- HTML/CSS/JavaScript\n- PHP\n- Intégration passerelles de paiement\n- SEO");
        } else {
            // Valeurs par défaut pour les autres offres
            text_entreprise_id.setText(id.substring(0, 1) + "200");
            text_titre.setText(offreString.substring(offreString.indexOf("titre='") + 7, offreString.indexOf("', budget")));
            text_budget.setText(offreString.substring(offreString.indexOf("budget=") + 7, offreString.indexOf("}")));
            text_duree.setText(String.valueOf(30 + Integer.parseInt(id) % 60));
            statut.setValue(statusOptions[Integer.parseInt(id) % statusOptions.length]);
            date_publication.setValue(LocalDate.now().minusDays(Integer.parseInt(id) % 30));
            text_description.setText("Description détaillée de l'offre " + id + "...");
            text_competences.setText("Compétences requises pour l'offre " + id + "...");
        }
    }
    
    /**
     * Ajoute une nouvelle offre
     */
    @FXML
    public void ajouterOffre() {
        // Générer un nouvel ID
        int newId = 1006 + offresList.size() - 5; // Commencer à 1006 puisque nous avons déjà 5 offres
        
        // Créer une nouvelle offre à partir des champs
        String newOffre = "Offre{id=" + newId + 
                        ", entreprise=" + text_entreprise_id.getText() + 
                        ", titre='" + text_titre.getText() + 
                        "', budget=" + text_budget.getText() + "}";
        
        // Ajouter à la liste et sélectionner
        offresList.add(newOffre);
        offres.getSelectionModel().select(offresList.size() - 1);
        
        // Afficher confirmation
        showAlert(Alert.AlertType.INFORMATION, "Offre Ajoutée", 
                  "L'offre a été ajoutée avec succès.", "ID de la nouvelle offre: " + newId);
    }
    
    /**
     * Met à jour l'offre sélectionnée
     */
    @FXML
    public void updateOffre() {
        int selectedIndex = offres.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Récupérer l'ID actuel
            String id = offre_id.getText();
            
            // Mettre à jour l'offre dans la liste
            String updatedOffre = "Offre{id=" + id + 
                               ", entreprise=" + text_entreprise_id.getText() + 
                               ", titre='" + text_titre.getText() + 
                               "', budget=" + text_budget.getText() + "}";
            
            offresList.set(selectedIndex, updatedOffre);
            
            // Reselectionner pour rafraîchir l'affichage
            offres.getSelectionModel().select(selectedIndex);
            
            // Afficher confirmation
            showAlert(Alert.AlertType.INFORMATION, "Offre Mise à Jour", 
                      "L'offre a été mise à jour avec succès.", "ID de l'offre: " + id);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", 
                      "Aucune offre n'est sélectionnée.", "Veuillez sélectionner une offre à mettre à jour.");
        }
    }
    
    /**
     * Supprime l'offre sélectionnée
     */
    @FXML
    public void supprimerOffre() {
        int selectedIndex = offres.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Demander confirmation
            Optional<ButtonType> result = showConfirmation("Supprimer l'Offre", 
                                          "Êtes-vous sûr de vouloir supprimer cette offre?", 
                                          "Cette action ne peut pas être annulée.");
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer l'offre
                offresList.remove(selectedIndex);
                
                // Sélectionner une autre offre si disponible
                if (!offresList.isEmpty()) {
                    offres.getSelectionModel().select(Math.min(selectedIndex, offresList.size() - 1));
                } else {
                    // Effacer les champs si aucune offre restante
                    clearFields();
                }
                
                // Afficher confirmation
                showAlert(Alert.AlertType.INFORMATION, "Offre Supprimée", 
                          "L'offre a été supprimée avec succès.", null);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", 
                      "Aucune offre n'est sélectionnée.", "Veuillez sélectionner une offre à supprimer.");
        }
    }
    
    /**
     * Efface tous les champs du formulaire
     */
    private void clearFields() {
        offre_id.setText("");
        text_entreprise_id.clear();
        text_titre.clear();
        text_budget.clear();
        text_duree.clear();
        statut.setValue(null);
        date_publication.setValue(null);
        text_description.clear();
        text_competences.clear();
    }
    
    /**
     * Affiche une boîte de dialogue d'alerte
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Affiche une boîte de dialogue de confirmation
     */
    private Optional<ButtonType> showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
