package tn.esprit.api;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.github.plushaze.traynotification.notification.TrayNotification;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notification;
import com.github.plushaze.traynotification.notification.Notifications;
import com.github.plushaze.traynotification.notification.TrayNotification;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import io.github.cdimascio.dotenv.Dotenv;
import javax.swing.*;
import java.util.concurrent.CountDownLatch;


public class SendNotification {
    //region tray notif
    private volatile TrayNotification tray;

    public void initializeTray() {
        Platform.runLater(() -> tray = new TrayNotification());
    }

    public void creatingANewTrayNotification() {
        TrayNotificationAlert.notif("SUCCESS", "Le service a été ajouté avec succès !",
                Notifications.SUCCESS, Animations.POPUP, Duration.millis(5000));
    }

    public class TrayNotificationAlert {

        public static void notif(String title, String msg, Notifications notifType, Animations notifAnimation,
                                 Duration notifDuration) {
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);

            tray.setMessage(msg);
            tray.setNotification(notifType);
            tray.setAnimation(notifAnimation);
            tray.showAndDismiss(notifDuration);
        }

        public static void notifimage(String title, String msg, Animations notifAnimation,
                                      Duration notifDuration, String code, Image img) {
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);

            tray.setMessage(msg);
            tray.setRectangleFill(Paint.valueOf(code));
            tray.setImage(img);
            tray.setAnimation(notifAnimation);
            tray.showAndDismiss(notifDuration);
        }
    }
    //endregion
}
