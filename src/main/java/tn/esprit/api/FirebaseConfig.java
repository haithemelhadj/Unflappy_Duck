
package tn.esprit.api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseConfig {
    private static FirebaseApp firebaseApp;

    public static void initializeFirebase() {
        if (firebaseApp != null) return; // Avoid re-initialization

        try (InputStream serviceAccount = new FileInputStream("D:\\Users\\Kuruttakao\\Documents\\GitHub\\env\\firebase-config.json")) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            firebaseApp = FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FirebaseMessaging getFirebaseMessaging() {
        if (firebaseApp == null) {
            throw new IllegalStateException("Firebase is not initialized!");
        }
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
/**/

/*
public class FirebaseConfig {


    private static FirebaseApp firebaseApp;
    public static String creds;
    public static void initializeFirebase() throws IOException {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        creds = credentialsPath;
        if (credentialsPath == null) {
            throw new IllegalStateException("GOOGLE_APPLICATION_CREDENTIALS environment variable is not set.");
        }
        FileInputStream serviceAccount = new FileInputStream(credentialsPath);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        System.out.println("Firebase Initialized Successfully!");

    }
    public static String returnCreds()
    {
        return creds;
    }
}
/**/
