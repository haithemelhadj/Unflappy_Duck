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
import tn.esprit.utils.Session;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StartupController implements Initializable {
    @FXML
    private WebView loginCaptchaView;
    @FXML
    private WebView registerCaptchaView;
    @FXML
    private TextField loginUsernameOrEmailField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private TextField createNameField;
    @FXML
    private TextField createEmailField;
    @FXML
    private PasswordField createPasswordField;
    @FXML
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    private final Callback callback;

    @FXML
    private ToggleButton loginToggle;

    @FXML
    private ToggleButton registerToggle;

    @FXML
    private VBox loginForm;

    @FXML
    private VBox registerForm;

    @FXML
    private ToggleGroup formToggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            loginForm.setVisible(newToggle == loginToggle);
            registerForm.setVisible(newToggle == registerToggle);
        });
        WebEngine captchaEngine = loginCaptchaView.getEngine();
        captchaEngine.load("https://www.google.com/recaptcha/api/fallback?k=6Le2wuoqAAAAALRX6Ma4Ya_KS81cukJMp2SVglQA");
        captchaEngine = registerCaptchaView.getEngine();
        captchaEngine.load("https://www.google.com/recaptcha/api/fallback?k=6Le2wuoqAAAAALRX6Ma4Ya_KS81cukJMp2SVglQA");
    }


    public interface Callback{
        void call();
    }

    public StartupController(Callback call){
        this.callback = call;
    }

    public void login(ActionEvent actionEvent) {
        String usernameOrEmail = loginUsernameOrEmailField.getText();
        String password = loginPasswordField.getText();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter both username/email and password.");
            return;
        }

        // Check if the input is in email format
        if (usernameOrEmail.contains("@") && !isValidEmail(usernameOrEmail)) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter a valid email address.");
            return;
        }

        try {
            Utilisateur utilisateur = serviceUtilisateur.loginUtilisateur(usernameOrEmail, password);
            if (utilisateur != null) {
                showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome " + utilisateur.getNom() + "!");
                callback.call();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username/email or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error accessing the database.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    public void createAccount(ActionEvent actionEvent) {
        String name = createNameField.getText();
        String email = createEmailField.getText();
        String password = createPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Account Creation Error", "Please fill out all fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Account Creation Error", "Please enter a valid email address.");
            return;
        }

        Utilisateur newUser = new Utilisateur();
        newUser.setNom(name);
        newUser.setEmail(email);
        newUser.setMotDePasse(password);
        newUser.setRole(userRoles.utilisateur);
        newUser.setBio("");
        newUser.setPhotoProfil("");
        newUser.setXp(0);
        newUser.setNiveau(null);
        newUser.setXpRequis(100);

        try {
            serviceUtilisateur.ajouterUtilisateur(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Account Created", "Your account has been created successfully!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error while creating the account.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/v10.0/dialog/oauth";
    private static final String CLIENT_ID = "YOUR_CLIENT_ID";
    private static final String REDIRECT_URI = "http://localhost/oauth2callback";
    private static final String RESPONSE_TYPE = "code";

    @FXML
    private Button googleLoginButton;

    @FXML
    private Button facebookLoginButton;

    @FXML
    public void handleGoogleLogin() {
        String authUrl = GOOGLE_AUTH_URL +
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=" + RESPONSE_TYPE +
                "&scope=email";
        openBrowser(authUrl);
    }

    @FXML
    public void handleFacebookLogin() {
        String authUrl = FACEBOOK_AUTH_URL +
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=" + RESPONSE_TYPE +
                "&scope=email";
        openBrowser(authUrl);
    }

    private void openBrowser(String url) {

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);


        webEngine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
            if (newLocation.contains("code=")) {
                String code = newLocation.split("code=")[1];
                exchangeCodeForToken(code);
            }
        });
    }

    private void exchangeCodeForToken(String code) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth2.googleapis.com/token"))
                    .POST(HttpRequest.BodyPublishers.ofString("code=" + code +
                            "&client_id=" + CLIENT_ID +
                            "&client_secret=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" +
                            "&redirect_uri=" + REDIRECT_URI +
                            "&grant_type=authorization_code"))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            String token = extractTokenFromResponse(response.body());
            if (token != null) {
                handleLoginSuccess(token);
            } else {
                handleLoginError();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String extractTokenFromResponse(String responseBody) {

        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();


        return jsonObject.get("access_token").getAsString();
    }

    private void handleLoginSuccess(String token) {

        System.out.println("Login successful with token: " + token);
    }

    private void handleLoginError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Authentication failed");
        alert.setContentText("Please try again.");
        alert.showAndWait();
    }

    // Method to verify CAPTCHA response
    private boolean verifyCaptcha(String userResponseToken) {
        String secretKey = "6Le2wuoqAAAAACpmpZlwysCcFsM3YFR7QJWDNv_p";
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
}
