package tn.esprit.api;

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


public class EquipeNotif {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

	public EquipeNotif() {
		Dotenv dotenv = Dotenv.load();
		String ACCOUNT_SID = dotenv.get("TWILIO_ACCOUNT_SID");
		String AUTH_TOKEN = dotenv.get("TWILIO_AUTH_TOKEN");
	}


	//twilio sms

    public void SendSmS() {


		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+21629334273"),
                        new com.twilio.type.PhoneNumber("+14782219213"),
                        "vous avez ete ajouté a l'equipe")
                .create();

        System.out.println(message.getSid());
    }
	//endregion
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
/**/