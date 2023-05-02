package javaFX;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import niti.TrenutniKorisnik;
import niti.ZadnjaPromjena;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("prijava.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 320);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Sancta Domenica prijava");
        stage.setScene(scene);
        stage.show();
    }

    public static void setMainStageTitle(String noviNaslov){

        mainStage.setTitle(noviNaslov);
        mainStage.show();

    }

    public static void main(String[] args) {

        Timeline threadovi = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.runLater(new TrenutniKorisnik());
            }
        }));

        threadovi.getKeyFrames().add(new KeyFrame(Duration.seconds(20), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.runLater(new ZadnjaPromjena());
            }
        }));

        threadovi.setCycleCount(Timeline.INDEFINITE);
        threadovi.play();
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}