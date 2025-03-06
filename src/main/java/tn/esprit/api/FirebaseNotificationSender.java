package tn.esprit.api;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

public class FirebaseNotificationSender {

    public static void sendNotificationToUser(String registrationToken, String title, String body) {
        try {
            FirebaseMessaging fcm = FirebaseConfig.getFirebaseMessaging();

            Message message = Message.builder()
                    .setToken(registrationToken) // Send to a single user
                    .putData("title", title)
                    .putData("body", body)
                    .build();

            String messageId = fcm.send(message);
            System.out.println("Message sent successfully! ID: " + messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
public class FirebaseNotifs {

    //send Notification To a Topic
    public static void sendNotificationToTopic(String topic, String title, String body) throws Exception {
        Message message = Message.builder()
                .setTopic(topic)
                .putData("title", title)
                .putData("body", body)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Notification sent successfully: " + response);
    }

    //Sending to a Specific Device
    public static void sendNotificationToDevice(String token, String title, String body) throws Exception {
        Message message = Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("body", body)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Message sent to device: " + response);
    }
}
/**/
/*
class TokenHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Get the request body
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // Parse the token (assuming the token is sent as JSON)
        String requestBody = sb.toString();
        String token = extractTokenFromJson(requestBody); // Custom function to extract token from the JSON

        // Print the token to the console (you can store it in a database)
        System.out.println("Received token: " + token);

        // Send the response back
        String response = "Token received successfully";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String extractTokenFromJson(String json) {
        // Simple JSON extraction for the token (you can use a library like Gson or Jackson for this)
        int start = json.indexOf("\"token\":\"") + 9;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
/**/