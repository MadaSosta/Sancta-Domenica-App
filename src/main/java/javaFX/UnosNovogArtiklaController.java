package javaFX;

import baza.BazaPodataka;
import entiteti.Artikl;
import entiteti.Kategorija;
import entiteti.Lokacija;
import entiteti.Radnik;
import iznimke.DuplikatNazivaException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.OptionalLong;

import static javaFX.Application.logger;

public class UnosNovogArtiklaController {
    @FXML
    private ComboBox<Kategorija> kategorijaComboBox;

    @FXML
    private TextField nazivTextField;

    @FXML
    private TextField sirinaTextField;

    @FXML
    private TextField visinaTextField;

    @FXML
    private TextField duljinaTextField;

    @FXML
    private TextField masaTextField;

    @FXML
    private TextField cijenaProizvodnjeTextField;

    @FXML
    private TextField cijenaProdajeTextField;

    @FXML
    private TextField garancijaTextField;

    public void initialize() {

        ObservableList<Kategorija> kategorijaObservableList = FXCollections.
                observableList(IzbornikController.getListaKategorija());
        kategorijaComboBox.setItems(kategorijaObservableList);
        kategorijaComboBox.getSelectionModel().select(0);

    }

    public void spremiArtikl() {

        Kategorija kategorija = kategorijaComboBox.getValue();
        String naziv = nazivTextField.getText();
        String sirina = sirinaTextField.getText();
        String visina = visinaTextField.getText();
        String duljina = duljinaTextField.getText();
        String masa = masaTextField.getText();
        String cijenaProizvodnje = cijenaProizvodnjeTextField.getText();
        String cijenaProdaje = cijenaProdajeTextField.getText();
        String garancija = garancijaTextField.getText();

        if(naziv.isBlank() == true || sirina.isBlank() == true || visina.isBlank() == true ||
                duljina.isBlank() == true || masa.isBlank() == true || cijenaProizvodnje.isBlank() == true ||
                cijenaProdaje.isBlank() == true || garancija.isBlank() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Došlo je do pogreške!");
            alert.setContentText("Ni jedno polje ne smije ostati prazno!");
            alert.showAndWait();
            logger.error("Korisnik nije upisao sva polja!");
        } else if (naziv.isBlank() == false && sirina.isBlank() == false && visina.isBlank() == false &&
                duljina.isBlank() == false && masa.isBlank() == false && cijenaProizvodnje.isBlank() == false &&
                cijenaProdaje.isBlank() == false && garancija.isBlank() == false) {

            OptionalLong maksimalniID = IzbornikController.getListaArtikala().stream()
                    .mapToLong(artikl -> artikl.getId()).max();

            Artikl noviArtikl = new Artikl(
                    maksimalniID.getAsLong()+1,
                    naziv,
                    kategorija,
                    Double.parseDouble(sirina),
                    Double.parseDouble(visina),
                    Double.parseDouble(duljina),
                    Double.parseDouble(masa),
                    Double.parseDouble(cijenaProizvodnje),
                    Double.parseDouble(cijenaProdaje),
                    Integer.parseInt(garancija));

            try {
                provjeraDuplikataNaziva(naziv);
                IzbornikController.getListaArtikala().add(noviArtikl);

                dodavanjeNovogArtiklaUBazu(noviArtikl);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Spremanje artikla");
                alert.setHeaderText("Uspješno dodana novi artikl!");
                alert.setContentText("Artikl: " + naziv + " uspješno dodan!");
                alert.showAndWait();
                logger.info("Artikl: " + naziv + " uspješno dodan!");
                vratiNaEkranPretrage();

            }catch (DuplikatNazivaException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("Postoji već takav artikl!");
                alert.setContentText("Unesite drugi!");
                alert.showAndWait();
                logger.error("Korisnik nije upisao sva polja!" + ex);
            }
        }

    }

    private void vratiNaEkranPretrage() {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("artikl.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 650, 650);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Application.getMainStage().setTitle("Pretraga Artikla");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    private static void dodavanjeNovogArtiklaUBazu(Artikl noviArtikl) {
        try {
            BazaPodataka.dodajNoviArtiklUBazu(noviArtikl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void provjeraDuplikataNaziva(String naziv) throws DuplikatNazivaException {
        for(Artikl artikl : IzbornikController.getListaArtikala()){
            if(artikl.getNaziv().equals(naziv)){
                throw new DuplikatNazivaException();
            }
        }
    }


}
