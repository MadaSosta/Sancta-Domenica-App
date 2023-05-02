package javaFX;

import baza.BazaPodataka;
import entiteti.Lokacija;
import entiteti.Radnik;
import iznimke.MaloljetnikRadnikException;
import iznimke.PrevisokaPlacaException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.OptionalLong;

import static javaFX.Application.logger;

public class UnosNovogRadnikaController {

   @FXML
   private TextField imeTextField;

    @FXML
    private TextField prezimeTextField;

    @FXML
    private TextField datumRodjenjaTextField;

    @FXML
    private ComboBox<Lokacija> lokacijaComboBox;

    @FXML
    private TextField placaTextField;

    public void initialize() {

        ObservableList<Lokacija> lokacijaObservableList = FXCollections.
                observableList(IzbornikController.getListaLokacija());
        lokacijaComboBox.setItems(lokacijaObservableList);
        lokacijaComboBox.getSelectionModel().select(0);

    }

    public void spremiRadnika() {

        String ime = imeTextField.getText();
        String prezime = prezimeTextField.getText();
        String datumRodjenja = datumRodjenjaTextField.getText();
        Lokacija lokacija = lokacijaComboBox.getValue();
        String placa = placaTextField.getText();

        if(ime.isBlank() == true || prezime.isBlank() == true || datumRodjenja.isBlank() == true ||
                placa.isBlank() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Došlo je do pogreške!");
            alert.setContentText("Ni jedno polje ne smije ostati prazno!");
            alert.showAndWait();
            logger.error("Korisnik nije upisao sva polja!");
        }

        else if(ime.isBlank() == false && prezime.isBlank() == false && datumRodjenja.isBlank() == false &&
                placa.isBlank() == false) {

            OptionalLong maksimalniID = IzbornikController.getListaRadnika().stream()
                    .mapToLong(radnik -> radnik.getId()).max();




            try {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                LocalDate parsiraniDatumRodjenja = LocalDate.parse(datumRodjenja, dtf);

                provjeraMaloljetnogRadnika(parsiraniDatumRodjenja);

                provjeraPrevisokePlace(placa);

                Radnik noviRadnik = new Radnik(maksimalniID.getAsLong()+1, ime, prezime, parsiraniDatumRodjenja,
                        lokacija, Double.parseDouble(placa));

                IzbornikController.getListaRadnika().add(noviRadnik);
                dodajNovogRadnikaUBazu(noviRadnik);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Spremanje radnika");
                alert.setHeaderText("Uspješno dodana novi radnik!");
                alert.setContentText("Radnik: " + ime + " " + prezime + " uspješno dodan!");
                alert.showAndWait();
                logger.info("Radnik: " + ime + " " + prezime + " uspješno dodan!");
                prikazRadnika();

            } catch (MaloljetnikRadnikException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("Ova osoba nije dovoljno stara!");
                alert.setContentText("Tražimo 18+ ljude.");
                alert.showAndWait();
                logger.error("Osoba je premlada!" + ex);

            } catch (PrevisokaPlacaException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("Niko osim gazde ne smije imati plaću preko 10 000kn!");
                alert.setContentText("Potraži drugi posao!");
                alert.showAndWait();
                logger.error("Osoba je pohlepna!" + ex);
            }

        }

    }

    private static void provjeraPrevisokePlace(String placa) {
        if(Double.parseDouble(placa) >= Double.valueOf(10000)){
            throw new PrevisokaPlacaException();
        }
    }

    private void prikazRadnika() {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("radnik.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 650, 650);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Application.getMainStage().setTitle("Pretraga Radnika");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    private static void dodajNovogRadnikaUBazu(Radnik noviRadnik) {
        try {
            BazaPodataka.dodajNovogRadnikaUBazu(noviRadnik);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void provjeraMaloljetnogRadnika(LocalDate parsiraniDatumRodjenja) {
        if (LocalDate.now().getYear() - parsiraniDatumRodjenja.getYear() <= 18){
            throw new MaloljetnikRadnikException();
        }
    }

}
