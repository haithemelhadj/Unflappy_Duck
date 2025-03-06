package tn.esprit.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;
import tn.esprit.services.ServiceUtilisateur;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import tn.esprit.utils.Session;

public class StartupController implements Initializable {

    @FXML private WebView loginCaptchaView;
    @FXML private WebView registerCaptchaView;
    @FXML private TextField loginUsernameOrEmailField;
    @FXML private PasswordField loginPasswordField;
    @FXML private TextField createNameField;
    @FXML private TextField createEmailField;
    @FXML private PasswordField createPasswordField;
    @FXML private ToggleButton loginToggle;
    @FXML private ToggleButton registerToggle;
    @FXML private VBox loginForm;
    @FXML private VBox registerForm;
    @FXML private ToggleGroup formToggleGroup;
    @FXML private PasswordField confirmPasswordField;

    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    private final Callback callback;

    private static final String REDIRECT_URI = "http://localhost/oauth2callback";
    private static final String GOOGLE_CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID";
    private static final String GOOGLE_CLIENT_SECRET = "YOUR_GOOGLE_CLIENT_SECRET";
    private static final String FACEBOOK_CLIENT_ID = "YOUR_FACEBOOK_CLIENT_ID";
    private static final String FACEBOOK_CLIENT_SECRET = "YOUR_FACEBOOK_CLIENT_SECRET";
    private static final String CAPTCHA_SECRET_KEY = "6Le2wuoqAAAAACpmpZlwysCcFsM3YFR7QJWDNv_p";

    public interface Callback {
        void call();
    }

    public StartupController(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Toggle between login and register forms
        formToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            loginForm.setVisible(newToggle == loginToggle);
            registerForm.setVisible(newToggle == registerToggle);
        });

        // Load CAPTCHA for login and register (just displays, no verification yet)
        WebEngine loginCaptchaEngine = loginCaptchaView.getEngine();
        loginCaptchaEngine.load("https://www.google.com/recaptcha/api/fallback?k=6Le2wuoqAAAAALRX6Ma4Ya_KS81cukJMp2SVglQA");
        WebEngine registerCaptchaEngine = registerCaptchaView.getEngine();
        registerCaptchaEngine.load("https://www.google.com/recaptcha/api/fallback?k=6Le2wuoqAAAAALRX6Ma4Ya_KS81cukJMp2SVglQA");
    }

    public void login(ActionEvent actionEvent) {
        String usernameOrEmail = loginUsernameOrEmailField.getText().trim();
        String password = loginPasswordField.getText();

        // Check if fields are empty
        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter both username/email and password.");
            return;
        }

        // Basic email validation
        if (usernameOrEmail.contains("@") && !isValidEmail(usernameOrEmail)) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter a valid email address.");
            return;
        }

        try {
            // Try logging in with the service
            Utilisateur utilisateur = serviceUtilisateur.loginUtilisateur(usernameOrEmail, password);
            if (utilisateur != null) {
                showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome " + utilisateur.getNom() + "!");
                Session.start(utilisateur);
                callback.call(); // Trigger next step (e.g., open main app)
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username/email or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error accessing the database.");
        }
    }

    public void createAccount(ActionEvent actionEvent) {
        String name = createNameField.getText().trim();
        String email = createEmailField.getText().trim();
        String password = createPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Basic validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez remplir tous les champs");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Email invalide", "Veuillez entrer une adresse email valide");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Mot de passe non concordant",
                    "Les mots de passe ne correspondent pas");
            return;
        }

        // CAPTCHA verification
        String captchaResponse = getResponseFromCaptcha(registerCaptchaView);
        if (!verifyCaptcha(captchaResponse)) {
            showAlert(Alert.AlertType.WARNING, "CAPTCHA invalide", "Veuillez valider le CAPTCHA");
            return;
        }

        // Check if email already exists
        try {
            if (serviceUtilisateur.emailExists(email)) {
                showAlert(Alert.AlertType.ERROR, "Email existant",
                        "Un compte avec cet email existe déjà");
                return;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données",
                    "Impossible de vérifier l'email");
            e.printStackTrace();
            return;
        }

        // Create user
        Utilisateur newUser = new Utilisateur();
        newUser.setNom(name);
        newUser.setEmail(email);
        newUser.setMotDePasse(password); // Consider hashing before saving
        newUser.setRole(userRoles.utilisateur);
        newUser.setBio("");
        newUser.setPhotoProfil("");
        newUser.setXp(0);
        newUser.setNiveau(null);
        newUser.setXpRequis(100);

        try {
            serviceUtilisateur.ajouterUtilisateur(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Votre compte a été créé avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de création",
                    "Erreur lors de la création du compte");
            e.printStackTrace();
        }
    }

    private String getResponseFromCaptcha(WebView captchaView) {
        return (String) captchaView.getEngine()
                .executeScript("document.getElementById('g-recaptcha-response').value");
    }

    private boolean verifyCaptcha(String userResponseToken) {
        String secretKey = "your-secret-key-here";
        String url = "https://www.google.com/recaptcha/api/siteverify";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?secret=" + secretKey + "&response=" + userResponseToken))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().contains("\"success\": true");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    public void handleGoogleLogin() {
        String authUrl = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=" + GOOGLE_CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=code" +
                "&scope=email%20profile";
        openOAuthWindow(authUrl, "Google");
    }

    @FXML
    public void handleFacebookLogin() {
        String authUrl = "https://www.facebook.com/v10.0/dialog/oauth" +
                "?client_id=" + FACEBOOK_CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=code" +
                "&scope=email";
        openOAuthWindow(authUrl, "Facebook");
    }

    private void openOAuthWindow(String authUrl, String provider) {
        Stage oauthStage = new Stage();
        oauthStage.initModality(Modality.APPLICATION_MODAL); // Blocks main window until this closes
        oauthStage.setTitle(provider + " Login");

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(authUrl);

        // Listen for redirect URL with the code
        webEngine.locationProperty().addListener((obs, oldUrl, newUrl) -> {
            if (newUrl.startsWith(REDIRECT_URI)) {
                String code = newUrl.split("code=")[1].split("&")[0];
                oauthStage.close();
                exchangeCodeForToken(provider, code);
            }
        });

        oauthStage.setScene(new Scene(new VBox(webView), 450, 600));
        oauthStage.showAndWait(); // Waits until window is closed
    }

    private void exchangeCodeForToken(String provider, String code) {
        String tokenUrl = provider.equals("Google")
                ? "https://oauth2.googleapis.com/token"
                : "https://graph.facebook.com/v10.0/oauth/access_token";

        String clientId = provider.equals("Google") ? GOOGLE_CLIENT_ID : FACEBOOK_CLIENT_ID;
        String clientSecret = provider.equals("Google") ? GOOGLE_CLIENT_SECRET : FACEBOOK_CLIENT_SECRET;

        String requestBody = "code=" + code +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + REDIRECT_URI +
                (provider.equals("Google") ? "&grant_type=authorization_code" : "");

        try {
            // Make the HTTP request synchronously
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                String accessToken = json.get("access_token").getAsString();
                handleSocialLoginSuccess(provider, accessToken);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Authentication failed for " + provider);
            }
        } catch (IOException | InterruptedException e) {
            showAlert(Alert.AlertType.ERROR, "Network Error", "Failed to connect to " + provider + " servers.");
        }
    }

    private void handleSocialLoginSuccess(String provider, String accessToken) {
        System.out.println(provider + " login successful! Access Token: " + accessToken);
        // For now, just log it. You’d normally fetch user info here and link it to your app
        // callback.call(); // Uncomment this to proceed after login
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // Blocks until user clicks OK
    }
}