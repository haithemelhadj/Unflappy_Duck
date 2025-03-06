package tn.esprit.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class ServiceMail {

    private static final String HOST = "smtp.gmail.com"; // Use Gmail's SMTP
    private static final int PORT = 587;
    private static final String FROM_EMAIL = "youremail@gmail.com"; // Your email
    private static final String PASSWORD = "yourpassword"; // Your email password

    public static void sendVerificationEmail(String toEmail, String subject, String content) throws MessagingException {
        // Set email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        // Create a session with an authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        // Create a new email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(content);

        // Send the message
        Transport.send(message);
    }
}
