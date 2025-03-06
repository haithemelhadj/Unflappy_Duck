package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import tn.esprit.models.ArticleBoutique;
import tn.esprit.services.ServiceArticleBoutique;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static javafx.scene.paint.Color.*;


public class UIItemArticleController implements Initializable {
    @FXML
    private Button mod_btn;
    @FXML
    private ImageView qrCodeView;
    @FXML
    private Label categ;

    @FXML
    private Text descrp;


    @FXML
    private Label id;

    @FXML
    private Label quest;
    @FXML
    private Rectangle aaa;
    @FXML
    private Label titre;
    private UIArticleBoutique uiArticleBoutique;
    private ArticleBoutique articleBoutique;
    @FXML
    private ImageView imagev;

    @FXML

    void supp_btn_clicked() throws SQLException {
ServiceArticleBoutique articleBoutique1  = new ServiceArticleBoutique();
        articleBoutique1.delete(Integer.parseInt(id.getText()));
        uiArticleBoutique.itemlist.getChildren().clear();
        uiArticleBoutique.show();
    }
    private BufferedImage convertToBufferedImage(Image image) {
        if (image == null) {
            return null;
        }

        // Cast width and height to integers
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Create a new BufferedImage
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Get the PixelReader
        PixelReader pixelReader = image.getPixelReader();

        // Loop through each pixel and set it in the BufferedImage
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Get the color of the pixel
                Color fxColor = pixelReader.getColor(x, y);

                // Convert JavaFX Color to AWT Color
                java.awt.Color awtColor = new java.awt.Color((float) fxColor.getRed(), (float) fxColor.getGreen(), (float) fxColor.getBlue(), (float) fxColor.getOpacity());

                // Set the pixel in the BufferedImage
                bufferedImage.setRGB(x, y, awtColor.getRGB());
            }
        }

        return bufferedImage;
    }
    private void generatePDF() {
        // Create file chooser for saving the PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    // Add Text to PDF
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 750);
                    contentStream.showText("Article Details");
                    contentStream.endText();

                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 700);
                    contentStream.showText("ID: " + articleBoutique.getId());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Name: " + articleBoutique.getNom());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Price: " + articleBoutique.getPrix() + " $");
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Category: " + articleBoutique.getType());
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Description: " + articleBoutique.getType().toString());
                    contentStream.endText();

                    // Add QR Code if available
                    if (qrCodeView.getImage() != null) {
                        // Convert the JavaFX Image to BufferedImage
                        BufferedImage bufferedImage = convertToBufferedImage(qrCodeView.getImage());

                        // Save the image to a temporary file
                        File qrFile = new File("qr_temp.png");
                        ImageIO.write(bufferedImage, "PNG", qrFile);

                        // Add the image to the PDF
                        PDImageXObject qrImage = PDImageXObject.createFromFile(qrFile.getAbsolutePath(), document);
                        contentStream.drawImage(qrImage, 50, 500, 150, 150);  // Adjust size and position

                        // Delete temporary file
                        qrFile.delete();
                    }
                }

                // Save PDF document
                document.save(file);
                System.out.println("PDF created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Image generateQRCodeImage(String data) {
        int size = 150;  // Size of the QR Code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, size, size, hints);
            BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(java.awt.Color.white);  // Background
            graphics.fillRect(0, 0, size, size);
            graphics.setColor(java.awt.Color.black);  // QR Code color

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            // Convert BufferedImage to JavaFX Image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            return new Image(inputStream);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mod_btn.setOnMouseClicked(e -> {

            generatePDF();

        });




    }


    public void setDATA(ArticleBoutique articleBoutique, UIArticleBoutique uiArticleBoutique) {
        id.setText(String.valueOf(articleBoutique.getId()));
        titre.setText(articleBoutique.getNom());
        quest.setText(String.valueOf(articleBoutique.getPrix()));
        descrp.setText(articleBoutique.getType().toString());

        InputStream inStream1 = getClass().getResourceAsStream("/FrontOffice/GestionStore/1.jpg");
        Image imageObject1 = new Image(inStream1);
        imagev.setImage(imageObject1);

        this.uiArticleBoutique = uiArticleBoutique;
        this.articleBoutique = articleBoutique;

        aaa.setArcWidth(30.0);   // Corner radius
        aaa.setArcHeight(30.0);

        ImagePattern pattern = new ImagePattern(
                new Image("/FrontOffice/GestionStore/1.jpg", 244, 168, false, false) // Resizing
        );
        aaa.setFill(pattern);

        // Generate QR Code with article information
        String qrData = "ID: " + articleBoutique.getId() +
                "\nName: " + articleBoutique.getNom() +
                "\nPrice: " + articleBoutique.getPrix() +
                "\nType: " + articleBoutique.getType();

        Image qrImage = generateQRCodeImage(qrData);
        if (qrImage != null) {
            qrCodeView.setImage(qrImage);
        }
    }



}
