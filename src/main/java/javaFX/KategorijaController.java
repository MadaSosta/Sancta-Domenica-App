package javaFX;

import baza.BazaPodataka;
import entiteti.BiljeskePromjena;
import entiteti.Kategorija;
import entiteti.Lokacija;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static javaFX.Application.logger;

public class KategorijaController {

    @FXML
    private TextField nazivTextField;

    @FXML
    private TableView<Kategorija> kategorijaTableView;

    @FXML
    private TableColumn<Kategorija, String> nazivTableColumn;

    List<Kategorija> kategorijaID;

    {
        try {
            kategorijaID = BazaPodataka.dohvatiKategorijeIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void initialize() {

        nazivTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getNaziv()));

        kategorijaTableView.setItems(FXCollections.observableList(IzbornikController.getListaKategorija()));

        dohvatiKategorije();

    }

    int index = -1;
    public void getSelected(MouseEvent event) {

        index = kategorijaTableView.getSelectionModel().getSelectedIndex();

        if(index <= -1){
            return;
        }

        nazivTextField.setText(nazivTableColumn.getCellData(index).toString());

    }

    public void dohvatiKategorije() {

        String naziv = nazivTextField.getText();

        List<Kategorija> filtriranaKategorija = IzbornikController.getListaKategorija().stream()
                .filter(kategorija -> kategorija.getNaziv().contains(naziv))
                .toList();

        kategorijaTableView.setItems(FXCollections.observableList(filtriranaKategorija));

    }

    public void updateKategorija() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izmijeniti odabranu kategoriju?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            String naziv = nazivTextField.getText();

            Kategorija staraKategorija = kategorijaID.get(index);

            index = kategorijaTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = kategorijaID.get(index).getId();

            Kategorija kategorija = new Kategorija(id, naziv);

            Kategorija novaKategorija = kategorija;

            String prijavljeniKorisnik = PrijavaController.getKorisnik().getKorisnickoIme();

            String vrijemePromjene = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

            BiljeskePromjena.zapisiPromjenu(staraKategorija.getNaziv(), novaKategorija.getNaziv(), prijavljeniKorisnik,
                    vrijemePromjene);

            IzbornikController.getListaKategorija().remove(index);
            IzbornikController.getListaKategorija().add(index, kategorija);

            try {
                BazaPodataka.updateKategorijeUBazu(kategorija);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Izmjena kategorije");
            alert.setHeaderText("Uspješno izmijenjena kategorija!");
            alert.setContentText("Kategorija: " + naziv +  " uspješno izmjenjena!");
            alert.showAndWait();
            logger.info("Kategorija: " + naziv +  " uspješno izmjenjena!");

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

    public void deleteKategorija() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izbrisati odabranu kategoriju?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            String naziv = nazivTextField.getText();

            index = kategorijaTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = kategorijaID.get(index).getId();

            Kategorija kategorija = new Kategorija(id, naziv);

            IzbornikController.getListaKategorija().remove(index);

            try {
                BazaPodataka.deleteKategorijaIzBaze(kategorija);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje kategorije");
            alert.setHeaderText("Uspješno izbrisana kategorija!");
            alert.setContentText("Kategorija: " + naziv +  " je uspješno izbrisana!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso kategoriju: " + naziv  + ".");

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
