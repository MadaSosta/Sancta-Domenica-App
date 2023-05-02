package javaFX;

import baza.BazaPodataka;
import entiteti.Lokacija;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.OptionalLong;

import static javaFX.Application.logger;

public class UnosNoveLokacijeController {

    @FXML
    private TextField ulicaTextField;
    @FXML
    private TextField gradTextField;
    @FXML
    private TextField postanskiBrojTextField;

    public void spremiLokaciju() {

        String ulica = ulicaTextField.getText();
        String grad = gradTextField.getText();
        String postanskiBroj = postanskiBrojTextField.getText();

        if(ulica.isBlank() == true || grad.isBlank() == true || postanskiBroj.isBlank() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Došlo je do pogreške!");
            alert.setContentText("Ni jedno polje ne smije ostati prazno!");
            alert.showAndWait();
            logger.error("Korisnik nije upisao sva polja!");
        }

        else if(ulica.isBlank() == false && grad.isBlank() == false && postanskiBroj.isBlank() == false) {

            OptionalLong maksimalniID = IzbornikController.getListaLokacija().stream()
                    .mapToLong(lokacija -> lokacija.getId()).max();

            Lokacija novaLokacija = new Lokacija.LokacijaBuilder()
                    .Id(maksimalniID.getAsLong()+1)
                    .Ulica(ulica)
                    .Grad(grad)
                    .PostanskiBroj(postanskiBroj)
                    .build();

            IzbornikController.getListaLokacija().add(novaLokacija);

            try {
                BazaPodataka.dodajNovuLokacijuUBazu(novaLokacija);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje lokacije");
            alert.setHeaderText("Uspješno dodana nova lokacija!");
            alert.setContentText("Lokacija: " + ulica + ", " + grad + ", " + postanskiBroj + " uspješno dodana!");
            alert.showAndWait();
            logger.info("Korisnik je dodao novu lokaciju: " + ulica + ", " + grad + ", " + postanskiBroj + ".");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("lokacija.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga Lokacija");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

}
