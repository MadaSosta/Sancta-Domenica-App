package javaFX;

import baza.BazaPodataka;
import entiteti.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.List;
import java.util.Optional;

import static javaFX.Application.logger;

public class DucanController {

    @FXML
    private TextField nazivTextField;

    @FXML
    private ComboBox<Lokacija> lokacijaComboBox;

    @FXML
    private TextField radnikTextField;

    @FXML
    private TextField artiklTextField;

    @FXML
    private TableView ducanTableView;

    @FXML
    private TableColumn<Ducan, String> nazivTableColumn;

    @FXML
    private TableColumn<Ducan, String> lokacijaTableColumn;

    @FXML
    private TableColumn<Ducan, String> radnikTableColumn;

    @FXML
    private TableColumn<Ducan, String> artiklTableColumn;

    List<Ducan> ducanID;

    {
        try {
            ducanID = BazaPodataka.dohvatiDucaneIzBaze();
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

        nazivTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getNaziv()));

        lokacijaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getLokacija().getUlica() + ", " +
                                cellData.getValue().getLokacija().getGrad() + ", " +
                                cellData.getValue().getLokacija().getPostanskiBroj()));

        radnikTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getRadnici().toString()));

        artiklTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getArtikli().toString()));

        dohvatiDucane();

    }

    int index = -1;
    public void getSelected(MouseEvent event) {

        index = ducanTableView.getSelectionModel().getSelectedIndex();

        if(index <= -1){
            return;
        }

        nazivTextField.setText(nazivTableColumn.getCellData(index).toString());
        lokacijaComboBox.getSelectionModel().select(ducanID.get(index).getLokacija());
        radnikTextField.setText(radnikTableColumn.getCellData(index).toString());
        artiklTextField.setText(artiklTableColumn.getCellData(index).toString());

    }


    public void dohvatiDucane() {

        String naziv = nazivTextField.getText();
        Lokacija lokacija = lokacijaComboBox.getValue();
        String radnik = radnikTextField.getText();
        String artikl = artiklTextField.getText();

        if (lokacija == null){

            List<Ducan> filtriraniDucan = IzbornikController.getListaDucana().stream()
                    .filter(ducan -> ducan.getNaziv().contains(naziv))
                    .filter(ducan -> ducan.getRadnici().toString().contains(radnik))
                    .filter(ducan -> ducan.getArtikli().toString().contains(artikl))
                    .toList();

            ducanTableView.setItems(FXCollections.observableList(filtriraniDucan));

        }

        else {

            List<Ducan> filtriraniDucan = IzbornikController.getListaDucana().stream()
                    .filter(ducan -> ducan.getNaziv().contains(naziv))
                    .filter(ducan -> ducan.getLokacija().getGrad().contains(lokacija.getGrad()) &&
                            ducan.getLokacija().getUlica().contains(lokacija.getUlica()) &&
                            ducan.getLokacija().getPostanskiBroj().contains(lokacija.getPostanskiBroj()))
                    .filter(ducan -> ducan.getRadnici().toString().contains(radnik))
                    .filter(ducan -> ducan.getArtikli().toString().contains(artikl))
                    .toList();

            ducanTableView.setItems(FXCollections.observableList(filtriraniDucan));

        }

    }

    public void updateDucan() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izmijeniti odabrani dućan?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            String naziv = nazivTextField.getText();
            Lokacija lokacija = lokacijaComboBox.getValue();
            String radnik = radnikTextField.getText();
            String artikl = artiklTextField.getText();
            List<Radnik> listaRadnika = ducanID.get(index).getRadnici();
            List<Artikl> listaArtikala = ducanID.get(index).getArtikli();

            Ducan stariDucan = ducanID.get(index);

            String testDucan = stariDucan.getNaziv() + "\n" +
                    stariDucan.getLokacija().toString() + "\n" +
                    stariDucan.getRadnici().toString() + "\n" +
                    stariDucan.getArtikli().toString();

            index = ducanTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = ducanID.get(index).getId();

            Ducan ducan = new Ducan(id, naziv, lokacija, listaRadnika, listaArtikala);

            String noviDucan = ducan.getNaziv() + "\n" +
                    ducan.getLokacija().toString() + "\n" +
                    ducan.getRadnici().toString() + "\n" +
                    ducan.getArtikli().toString();

            String prijavljeniKorisnik = PrijavaController.getKorisnik().getKorisnickoIme();

            String vrijemePromjene = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

            BiljeskePromjena.zapisiPromjenu(testDucan, noviDucan, prijavljeniKorisnik,
                    vrijemePromjene);

            IzbornikController.getListaDucana().remove(index);
            IzbornikController.getListaDucana().add(index, ducan);

            try {
                BazaPodataka.updateDucanUBazu(ducan);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Izmjena dućana");
            alert.setHeaderText("Uspješno izmjenjen dućan!");
            alert.setContentText("Dućan: " + naziv + " uspješno izmjenjen!");
            alert.showAndWait();
            logger.info("Korisnik je izmjenio dućan: " + naziv + ".");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("ducan.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga dućana");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

    public void deleteDucan() {

        String naziv = nazivTextField.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izbrisati odabrani dućan?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            index = ducanTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = ducanID.get(index).getId();


            IzbornikController.getListaDucana().remove(index);

            try {
                BazaPodataka.deleteDucanIzBaze(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje dućana");
            alert.setHeaderText("Uspješno izbrisan dućan!");
            alert.setContentText("Dućan: " + naziv + " uspješno izbrisan!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso dućan: " + naziv + ".");

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
