package javaFX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

public class PocetnaController {

    @FXML
    ImageView imageView;

    public void initialize() {
        File file = new File("img/sancta.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    public static void showPocetnaStranica() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("pocetna.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        Application.getMainStage().setTitle("Sancta Domenica");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

}
