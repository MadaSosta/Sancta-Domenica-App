package javaFX;

import baza.BazaPodataka;
import entiteti.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javaFX.Application.logger;

public class KupacController {

    @FXML
    private TextField imeTextField;

    @FXML
    private TextField prezimeTextField;

    @FXML
    private TextField artikliTextField;

    @FXML
    private TextField potrosenNovacTextField;

    @FXML
    private TextField datumIzdavanjaTextField;

    @FXML
    private TableView kupacTableView;

    @FXML
    private TableColumn<Kupac, String> imeTableColumn;

    @FXML
    private TableColumn<Kupac, String> prezimeTableColumn;

    @FXML
    private TableColumn<Kupac, String> artikliTableColumn;

    @FXML
    private TableColumn<Kupac, String> potrosenoNovacaTableColumn;

    @FXML
    private TableColumn<Kupac, String> datumIzdavanjaTableColumn;

    List<Kupac> kupacID;

    {
        try {
            kupacID = BazaPodataka.dohvatiKupcaIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {

        imeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getIme()));

        prezimeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPrezime()));

        artikliTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getKupljeniArtikli().toString()));

        potrosenoNovacaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPotrosenoNovaca().toString()));

        datumIzdavanjaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDatumIzdavanja()
                                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"))));

        dohvatiKupce();

    }

    int index = -1;
    public void getSelected(MouseEvent event) {

        index = kupacTableView.getSelectionModel().getSelectedIndex();

        if(index <= -1){
            return;
        }

        imeTextField.setText(imeTableColumn.getCellData(index));
        prezimeTextField.setText(prezimeTableColumn.getCellData(index));
        artikliTextField.setText(artikliTableColumn.getCellData(index));
        potrosenNovacTextField.setText(potrosenoNovacaTableColumn.getCellData(index));
        datumIzdavanjaTextField.setText(datumIzdavanjaTableColumn.getCellData(index));

    }

    public void dohvatiKupce() {

        String ime = imeTextField.getText();
        String prezime = prezimeTextField.getText();
        String artikli = artikliTextField.getText();
        String potrosenNovac = potrosenNovacTextField.getText();
        String datumIVrijeme = datumIzdavanjaTextField.getText();

        List<Kupac> filtriraniKupci = IzbornikController.getListaKupaca().stream()
                .filter(kupac -> kupac.getIme().contains(ime))
                .filter(kupac -> kupac.getPrezime().contains(prezime))
                .filter(kupac -> kupac.getKupljeniArtikli().toString().contains(artikli))
                .filter(kupac -> kupac.getPotrosenoNovaca().toString().contains(potrosenNovac))
                .filter(kupac -> kupac.getDatumIzdavanja()
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"))
                        .contains(datumIVrijeme))
                .toList();

        kupacTableView.setItems(FXCollections.observableList(filtriraniKupci));

    }

    public void updateKupac() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izmijeniti odabranog kupca?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            String ime = imeTextField.getText();
            String prezime = prezimeTextField.getText();
            String artikli = artikliTextField.getText();
            String potrosenNovac = potrosenNovacTextField.getText();
            String datumIVrijeme = datumIzdavanjaTextField.getText();
            List<Artikl> listaArtikala = kupacID.get(index).getKupljeniArtikli();

            Kupac stariKupac = kupacID.get(index);

            String testKupac = stariKupac.getIme() + " " + stariKupac.getPrezime() + "\n"
                    + stariKupac.getKupljeniArtikli().toString() + "\n" +
                    "Iznos: " + stariKupac.getPotrosenoNovaca() + " kn" + "\n" +
                    stariKupac.getDatumIzdavanja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

            index = kupacTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = kupacID.get(index).getId();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            LocalDateTime parsiraniDatumIzdavanja = LocalDateTime.parse(datumIVrijeme, dtf);

            Kupac kupac = new Kupac(id, ime, prezime, listaArtikala, Double.parseDouble(potrosenNovac),
                    parsiraniDatumIzdavanja);

            String noviKupac = kupac.getIme() + " " + kupac.getPrezime() + "\n"
                    + kupac.getKupljeniArtikli().toString() + "\n" +
                    "Iznos: " + kupac.getPotrosenoNovaca() + " kn" + "\n" +
                    kupac.getDatumIzdavanja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

            String prijavljeniKorisnik = PrijavaController.getKorisnik().getKorisnickoIme();

            String vrijemePromjene = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

            BiljeskePromjena.zapisiPromjenu(testKupac, noviKupac, prijavljeniKorisnik,
                    vrijemePromjene);

            IzbornikController.getListaKupaca().remove(index);
            IzbornikController.getListaKupaca().add(index, kupac);

            try {
                BazaPodataka.updateKupcaUBazu(kupac);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Izmjena kupca");
            alert.setHeaderText("Uspješno izmjenjen kupac!");
            alert.setContentText("Kupac: " + ime + " " + prezime + " uspješno izmjenjen!");
            alert.showAndWait();
            logger.info("Korisnik je izmjenio kupca: " + ime + ", " + prezime + ".");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("kupac.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga kupaca");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

    public void deleteKupac() {

        String ime = imeTextField.getText();
        String prezime = prezimeTextField.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izbrisati odabranog kupca?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            index = kupacTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = kupacID.get(index).getId();


            IzbornikController.getListaKupaca().remove(index);

            try {
                BazaPodataka.deleteKupacIzBaze(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje kupca");
            alert.setHeaderText("Uspješno izbrisan kupac!");
            alert.setContentText("Kupac: " + ime + " " + prezime + " uspješno izbrisan!");
            alert.showAndWait();
            logger.info("Kupac: " + ime + " " + prezime + " uspješno izbrisan!");

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
