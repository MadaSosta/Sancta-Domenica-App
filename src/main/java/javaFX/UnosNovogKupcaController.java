package javaFX;

import baza.BazaPodataka;
import entiteti.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import static javaFX.Application.logger;

public class UnosNovogKupcaController {

    @FXML
    private TextField imeTextField;

    @FXML
    private TextField prezimeTextField;

    @FXML
    private ComboBox<Artikl> artiklComboBox;

    @FXML
    private TextField potrosenoNovacaTextField;

    @FXML
    private TextField datumIzdavanjaTextField;

    @FXML
    private ListView<Artikl> artiklListView;

    List<Artikl> artikli = new ArrayList<>();

   // ObservableList<Artikl> dodaniArtikl;

    public void initialize() {

        List<Artikl> listaArtikla = IzbornikController.getListaArtikala();
        ObservableList<Artikl> artiklObservableList = FXCollections.observableList(listaArtikla);
        artiklComboBox.setItems(artiklObservableList);
        artiklComboBox.getSelectionModel().select(0);

    }

    public void dodajArtikl() {

        Artikl odabraniArtikl = artiklComboBox.getSelectionModel().getSelectedItem();
        artikli.add(odabraniArtikl);
        ObservableList<Artikl> dodaniArtikl = FXCollections.observableList(artikli);
        artiklListView.setItems(dodaniArtikl);

        Double suma = Double.valueOf(0);

        for(int i = 0; i < dodaniArtikl.size(); i++){
            suma += dodaniArtikl.get(i).getCijenaProdaje();
        }

        potrosenoNovacaTextField.setText(suma.toString());

    }

    public void danasnjeVrijeme() {

        datumIzdavanjaTextField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")));

    }

    public void spremiKupca() {

        String ime = imeTextField.getText();
        String prezime = prezimeTextField.getText();
        String potrosenoNovaca = potrosenoNovacaTextField.getText();
        String datumIzdavanja = datumIzdavanjaTextField.getText();

        if(ime.isBlank() == true || prezime.isBlank() == true || potrosenoNovaca.isBlank() == true ||
        datumIzdavanja.isBlank() == true){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Došlo je do pogreške!");
            alert.setContentText("Nijedno polje ne smije ostati prazno!");

            alert.showAndWait();

        } else if ((ime.isBlank() == false && prezime.isBlank() == false && potrosenoNovaca.isBlank() == false &&
                datumIzdavanja.isBlank() == false)) {

            OptionalLong maskimalniID = IzbornikController.getListaKupaca().stream()
                    .mapToLong(kupac -> kupac.getId()).max();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            LocalDateTime parsiranoDatumIzdavnje = LocalDateTime.parse(datumIzdavanja, dtf);

            Kupac noviKupac = new Kupac(maskimalniID.getAsLong()+1, ime, prezime, artikli,
                    Double.valueOf(potrosenoNovaca), parsiranoDatumIzdavnje);

            IzbornikController.getListaKupaca().add(noviKupac);

            try {
                BazaPodataka.dodajNovogKupcaUBazu(noviKupac);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                System.out.println("Ne znam!");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje kupca");
            alert.setHeaderText("Uspješno dodan novi kupac!");
            alert.setContentText("Kupac: " + ime + " " + prezime + " je uspješno dodan!");
            alert.showAndWait();
            logger.info("Kupac: " + ime + " " + prezime + " je uspješno dodan!");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("kupac.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga Kupaca");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

}
