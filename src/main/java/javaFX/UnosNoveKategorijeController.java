package javaFX;

import baza.BazaPodataka;
import entiteti.Kategorija;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.OptionalLong;

import static javaFX.Application.logger;

public class UnosNoveKategorijeController {

    @FXML
    private TextField nazivTextField;

    public void spremiKategoriju() {

        String naziv = nazivTextField.getText();

        if(naziv.isBlank() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Došlo je do pogreške!");
            alert.setContentText("Niste upisali naziv kategorije!");
            logger.error("Korisnik nije upisao naziv kategorije!");

            alert.showAndWait();
        }

        else if (naziv.isBlank() == false) {

            OptionalLong maksimalniID = IzbornikController.getListaKategorija().stream()
                    .mapToLong(kategorija -> kategorija.getId()).max();

            Kategorija novaKategorija = new Kategorija(maksimalniID.getAsLong()+1, naziv);

            IzbornikController.getListaKategorija().add(novaKategorija);

            try {
                BazaPodataka.dodajNovuKategorijuUBazu(novaKategorija);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje kategorije");
            alert.setHeaderText("Uspješno dodana nova kategorija!");
            alert.setContentText(naziv + " ste uspješno dodali u kategorije!");
            logger.info(naziv + " uspješno dodan u kategorije!");

            alert.showAndWait();

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("kategorija.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga Kategorija");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

}
