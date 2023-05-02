package javaFX;

import baza.BazaPodataka;
import entiteti.Artikl;
import entiteti.Ducan;
import entiteti.Lokacija;
import entiteti.Radnik;
import iznimke.DuplikatRadnikaException;
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

public class UnosNovogDucanaController {

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
        try {
            provjeraDuplikataRadnika(odabraniRadnik);
            radnici.add(odabraniRadnik);
            ObservableList<Radnik> dodaniRadnik = FXCollections.observableList(radnici);
            radnikListView.setItems(dodaniRadnik);
        }catch (DuplikatRadnikaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Već ste odabrali tog radnika!");
            alert.setContentText("Odaberite drugog!");
            alert.showAndWait();
            logger.error("Korisnik je odabrao 2 puta istog radnika!" + ex);
        }
    }

    private void provjeraDuplikataRadnika(Radnik odabraniRadnik) throws DuplikatRadnikaException {
        for(Radnik radnik : radnici){
            if(radnik.getId().equals(odabraniRadnik.getId())){
                throw new DuplikatRadnikaException();
            }
        }
    }

    public void dodajArtikl() {

        Artikl odabraniArtikl = artiklComboBox.getSelectionModel().getSelectedItem();
        artikli.add(odabraniArtikl);
        ObservableList<Artikl> dodaniArtikl = FXCollections.observableList(artikli);
        artiklListView.setItems(dodaniArtikl);

    }

    public void spremiDucan() {

        String naziv = nazivTextField.getText();
        Lokacija lokacija = lokacijaComboBox.getValue();

        if(naziv.isBlank() == true){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Došlo je do pogreške!");
            alert.setContentText("Nijedno polje ne smije ostati prazno!");

            alert.showAndWait();

        } else if (naziv.isBlank() == false) {

            OptionalLong maskimalniID = IzbornikController.getListaDucana().stream()
                    .mapToLong(ducan -> ducan.getId()).max();

            Ducan noviDucan = new Ducan(maskimalniID.getAsLong()+1, naziv, lokacija, radnici, artikli);

            IzbornikController.getListaDucana().add(noviDucan);

            try {
                BazaPodataka.dodajNoviDucanUBazu(noviDucan);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje dućana");
            alert.setHeaderText("Uspješno dodan novi dućan!");
            alert.setContentText("Dućan: " + naziv + " je uspješno dodan!");
            alert.showAndWait();
            logger.info("Dućan: " + naziv + " je uspješno dodan!");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("ducan.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga Dućana");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

}
