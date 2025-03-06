package tn.esprit.api;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class ChatbotController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    public void sendMessage() {
        String message = inputField.getText();
        chatArea.appendText("You: " + message + "\n");
        inputField.clear();
        getApiResponse(message);
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

    private void getApiResponse(String message) {
        String apiKey = "AIzaSyBidS2L33bSz73nCywuv64Z-lxIx3IaifE"; // Replace with your actual API key
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"contents\": [{\"parts\": [{\"text\": \"" + message + "\"}]}] }"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::processApiResponse)
                .exceptionally(ex -> {
                    chatArea.appendText("Error: " + ex.getMessage() + "\n");
                    return null;
                });
    }

    private void processApiResponse(String responseBody) {
        try {
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            // Adjust based on the actual response structure of the Gemini API
            // This is a placeholder, you'll need to inspect the response to get the correct path.
            String reply = jsonResponse.getAsJsonArray("candidates").get(0).getAsJsonObject().getAsJsonObject("content").getAsJsonArray("parts").get(0).getAsJsonObject().get("text").getAsString();

            chatArea.appendText("Bot: " + reply + "\n");
        } catch (Exception e) {
            chatArea.appendText("Error parsing API response.\n");
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
}