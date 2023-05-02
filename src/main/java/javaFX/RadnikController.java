package javaFX;

import baza.BazaPodataka;
import entiteti.BiljeskePromjena;
import entiteti.Lokacija;
import entiteti.Radnik;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static baza.BazaPodataka.dohvatiLokacijeIzBaze;
import static baza.BazaPodataka.toSingleton;
import static javaFX.Application.logger;

public class RadnikController {

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

    @FXML
    private TableView<Radnik> radnikTableView;

    @FXML
    private TableColumn<Radnik, String> imeTableColumn;

    @FXML
    private TableColumn<Radnik, String> prezimeTableColumn;

    @FXML
    private TableColumn<Radnik, String> datumRodjenjaTableColumn;

    @FXML
    private TableColumn<Radnik, String> lokacijaTableColumn;

    @FXML
    private TableColumn<Radnik, String> placaTableColumn;

    List<Radnik> radnikID;

    {
        try {
            radnikID = BazaPodataka.dohvatiRadnikeIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {

        ObservableList<Lokacija> lokacijaObservableList = FXCollections.
                observableList(IzbornikController.getListaLokacija());
        lokacijaComboBox.setItems(lokacijaObservableList);

        imeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getIme()));

        prezimeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPrezime()));

        datumRodjenjaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDatumRodjenja()
                                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))));

        lokacijaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getLokacija().getUlica() + ", " +
                                cellData.getValue().getLokacija().getGrad() + ", " +
                                cellData.getValue().getLokacija().getPostanskiBroj()));

        placaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPlaca().toString()));

        radnikTableView.setItems(FXCollections.observableList(IzbornikController.getListaRadnika()));

        dohvatiRadnike();

    }

    int index = -1;
    public void getSelected(MouseEvent event) {

        index = radnikTableView.getSelectionModel().getSelectedIndex();

        if(index <= -1){
            return;
        }

        imeTextField.setText(imeTableColumn.getCellData(index).toString());
        prezimeTextField.setText(prezimeTableColumn.getCellData(index).toString());
        datumRodjenjaTextField.setText(datumRodjenjaTableColumn.getCellData(index).toString());
        lokacijaComboBox.getSelectionModel().select(radnikID.get(index).getLokacija());
        placaTextField.setText(placaTableColumn.getCellData(index).toString());

    }

    public void dohvatiRadnike() {

        String ime = imeTextField.getText();
        String prezime = prezimeTextField.getText();
        String datumRodjenja = datumRodjenjaTextField.getText();
        Lokacija lokacija = lokacijaComboBox.getValue();
        String placa = placaTextField.getText();

        if(lokacija == null){
            List<Radnik> filtriraniRadnici = IzbornikController.getListaRadnika().stream()
                    .filter(radnik -> radnik.getIme().contains(ime))
                    .filter(radnik -> radnik.getPrezime().contains(prezime))
                    .filter(radnik -> radnik.getDatumRodjenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))
                            .contains(datumRodjenja))
                    .filter(radnik -> radnik.getPlaca().toString().contains(placa))
                    .toList();

            radnikTableView.setItems(FXCollections.observableList(filtriraniRadnici));

        }

        else {

            List<Radnik> filtriraniRadnici = IzbornikController.getListaRadnika().stream()
                    .filter(radnik -> radnik.getIme().contains(ime))
                    .filter(radnik -> radnik.getPrezime().contains(prezime))
                    .filter(radnik -> radnik.getDatumRodjenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))
                            .contains(datumRodjenja))
                    .filter(radnik -> radnik.getLokacija().getGrad().contains(lokacija.getGrad()) &&
                            radnik.getLokacija().getUlica().contains(lokacija.getUlica()) &&
                            radnik.getLokacija().getPostanskiBroj().contains(lokacija.getPostanskiBroj()))
                    .filter(radnik -> radnik.getPlaca().toString().contains(placa))
                    .toList();

            radnikTableView.setItems(FXCollections.observableList(filtriraniRadnici));

        }

    }

    public void updateRadnik() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izmijeniti odabranog radnika?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            String ime = imeTextField.getText();
            String prezime = prezimeTextField.getText();
            String datumRodjenja = datumRodjenjaTextField.getText();
            Lokacija lokacija = lokacijaComboBox.getValue();
            String placa = placaTextField.getText();

            Radnik stariRadnik = radnikID.get(index);

            String testRadnik = stariRadnik.getIme()  + " " + stariRadnik.getPrezime() + "\n"
                    + stariRadnik.getLokacija().toString() + "\n" +
            stariRadnik.getDatumRodjenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + "\n" +
                    stariRadnik.getPlaca() + " kn";

            index = radnikTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = radnikID.get(index).getId();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            LocalDate parsiraniDatumRodjenja = LocalDate.parse(datumRodjenja, dtf);

            Radnik radnik = new Radnik(id, ime, prezime, parsiraniDatumRodjenja, lokacija, Double.parseDouble(placa));

            String noviRadnik = radnik.getIme() + " " + radnik.getPrezime() + "\n"
                    + radnik.getLokacija().toString() + "\n" +
                    radnik.getDatumRodjenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + "\n" +
                    radnik.getPlaca() + " kn";

            String prijavljeniKorisnik = PrijavaController.getKorisnik().getKorisnickoIme();

            String vrijemePromjene = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

            BiljeskePromjena.zapisiPromjenu(testRadnik, noviRadnik, prijavljeniKorisnik,
                    vrijemePromjene);

            IzbornikController.getListaRadnika().remove(index);
            IzbornikController.getListaRadnika().add(index, radnik);

            try {
                BazaPodataka.updateRadnikaUBazu(radnik);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Izmjena radnika");
            alert.setHeaderText("Uspješno izmjenjen radnik!");
            alert.setContentText("Radnik: " + ime + " " + prezime + " uspješno izmjenjen!");
            alert.showAndWait();
            logger.info("Korisnik je izmjenio radnika: " + ime + ", " + prezime + ".");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("radnik.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga radnika");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

    public void deleteRadnik() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izbrisati odabranog radnika?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            String ime = imeTextField.getText();
            String prezime = prezimeTextField.getText();
            String datumRodjenja = datumRodjenjaTextField.getText();
            Lokacija lokacija = lokacijaComboBox.getValue();
            String placa = placaTextField.getText();

            index = radnikTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = radnikID.get(index).getId();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            LocalDate parsiraniDatumRodjenja = LocalDate.parse(datumRodjenja, dtf);

            Radnik radnik = new Radnik(id, ime, prezime, parsiraniDatumRodjenja, lokacija, Double.parseDouble(placa));

            IzbornikController.getListaRadnika().remove(index);

            try {
                BazaPodataka.deleteRadnikaIzBaze(radnik);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje radnika");
            alert.setHeaderText("Uspješno izbrisan radnik!");
            alert.setContentText("Radnik: " + ime + ", " + prezime + " uspješno izbrisan!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso radnika: " + ime + ", " + prezime + ".");

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

    }


}
