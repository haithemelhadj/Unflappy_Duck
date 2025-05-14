package tn.esprit.api;

import com.twilio.Twilio;
import io.github.cdimascio.dotenv.Dotenv;

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

public class SendSmS {

    //region twilio sms


    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC54b12d1ffba053-5a93e383d1141793b0";//System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN ="e16eb77f04ecb0f7-4ce17c67841fae4c";// System.getenv("TWILIO_AUTH_TOKEN");

/*
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC54b12d1ffba053-5a93e383d1141793b0";
    public static final String AUTH_TOKEN = "e16eb77f04ecb0f7-4ce17c67841fae4c";
	/**/
    public SendSmS() {


        Dotenv dotenv = Dotenv.load();
        String ACCOUNT_SID = "AC54b12d1ffba053-5a93e383d1141793b0";//dotenv.get("TWILIO_ACCOUNT_SID");
        String AUTH_TOKEN ="e16eb77f04ecb0f7-4ce17c67841fae4c";// dotenv.get("TWILIO_AUTH_TOKEN");
		/*
		String ACCOUNT_SID = sd;
		String AUTH_TOKEN = st;
		/**/
    }




	
    public void SendSms(String msg) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+21629334273"),
                        new com.twilio.type.PhoneNumber("+14782219213"),
                        msg)
                .create();

        System.out.println(message.getSid());
    }
	/**/
    //endregion

}
