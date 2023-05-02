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
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import static javaFX.Application.logger;

public class UnosNovogSkladistaController {

    @FXML
    private TextField nazivTextField;

    @FXML
    private ComboBox<Lokacija> lokacijaComboBox;

    @FXML
    private ComboBox<Radnik> radnikComboBox;

    @FXML
    private ComboBox<Artikl> artiklComboBox;

    @FXML
    private ListView<Radnik> radnikListView;

    @FXML
    private ListView<Artikl> artiklListView;


    List<Radnik> radnici = new ArrayList<>();
    List<Artikl> artikli = new ArrayList<>();

    public void initialize() {

        ObservableList<Lokacija> lokacijaObservableList = FXCollections.observableList(
                IzbornikController.getListaLokacija());
        lokacijaComboBox.setItems(lokacijaObservableList);
        lokacijaComboBox.getSelectionModel().select(0);

        List<Radnik> listaRadnika = IzbornikController.getListaRadnika();
        ObservableList<Radnik> radnikObservableList = FXCollections.observableList(listaRadnika);
        radnikComboBox.setItems(radnikObservableList);
        radnikComboBox.getSelectionModel().select(0);

        List<Artikl> listaArtikla = IzbornikController.getListaArtikala();
        ObservableList<Artikl> artiklObservableList = FXCollections.observableList(listaArtikla);
        artiklComboBox.setItems(artiklObservableList);
        artiklComboBox.getSelectionModel().select(0);

    }

    public void dodajRadnika() {

        Radnik odabraniRadnik = radnikComboBox.getSelectionModel().getSelectedItem();
        radnici.add(odabraniRadnik);
        ObservableList<Radnik> dodaniRadnik = FXCollections.observableList(radnici);
        radnikListView.setItems(dodaniRadnik);

    }

    public void dodajArtikl() {

        Artikl odabraniArtikl = artiklComboBox.getSelectionModel().getSelectedItem();
        artikli.add(odabraniArtikl);
        ObservableList<Artikl> dodaniArtikl = FXCollections.observableList(artikli);
        artiklListView.setItems(dodaniArtikl);

    }

    public void spremiSkladiste() {

        String naziv = nazivTextField.getText();
        Lokacija lokacija = lokacijaComboBox.getValue();

        if(naziv.isBlank() == true){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Došlo je do pogreške!");
            alert.setContentText("Nijedno polje ne smije ostati prazno!");

            alert.showAndWait();

        } else if (naziv.isBlank() == false) {

            OptionalLong maskimalniID = IzbornikController.getListaSkladista().stream()
                    .mapToLong(skladiste -> skladiste.getId()).max();

            Skladiste novoSkladiste = new Skladiste(maskimalniID.getAsLong()+1, naziv, lokacija, radnici, artikli);

            IzbornikController.getListaSkladista().add(novoSkladiste);

            try {
                BazaPodataka.dodajNovoSkladisteUBazu(novoSkladiste);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje skladišta");
            alert.setHeaderText("Uspješno dodano novo skladište!");
            alert.setContentText("Skladište: " + naziv + " je uspješno dodano!");
            alert.showAndWait();
            logger.info("Skladište: " + naziv + " je uspješno dodano!");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("skladiste.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga Skladište");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

}
