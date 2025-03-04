package tn.esprit.api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import java.io.IOException;
import java.io.InputStream;

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
