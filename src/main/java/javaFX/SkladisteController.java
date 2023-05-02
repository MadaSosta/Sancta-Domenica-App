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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static javaFX.Application.logger;

public class SkladisteController {

    @FXML
    private TextField nazivTextField;

    @FXML
    private ComboBox<Lokacija> lokacijaComboBox;

    @FXML
    private TextField radnikTextField;

    @FXML
    private TextField artiklTextField;

    @FXML
    private TableView skladisteTableView;

    @FXML
    private TableColumn<Skladiste, String> nazivTableColumn;

    @FXML
    private TableColumn<Skladiste, String> lokacijaTableColumn;

    @FXML
    private TableColumn<Skladiste, String> radnikTableColumn;

    @FXML
    private TableColumn<Skladiste, String> artiklTableColumn;

    List<Skladiste> skladisteID;

    {
        try {
            skladisteID = BazaPodataka.dohvatiSkladisteIzBaze();
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
                        new SimpleStringProperty(cellData.getValue().getLokacija().toString()));

        radnikTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getRadnici().toString()));

        artiklTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getArtikli().toString()));

        dohvatiSkladista();

    }

    int index = -1;
    public void getSelected(MouseEvent event) {

        index = skladisteTableView.getSelectionModel().getSelectedIndex();

        if(index <= -1){
            return;
        }

        nazivTextField.setText(nazivTableColumn.getCellData(index).toString());
        lokacijaComboBox.getSelectionModel().select(skladisteID.get(index).getLokacija());
        radnikTextField.setText(radnikTableColumn.getCellData(index).toString());
        artiklTextField.setText(artiklTableColumn.getCellData(index).toString());

    }


    public void dohvatiSkladista() {

        String naziv = nazivTextField.getText();
        Lokacija lokacija = lokacijaComboBox.getValue();
        String radnik = radnikTextField.getText();
        String artikl = artiklTextField.getText();

        if (lokacija == null){

            List<Skladiste> filtriranoSkladiste = IzbornikController.getListaSkladista().stream()
                    .filter(ducan -> ducan.getNaziv().contains(naziv))
                    .filter(ducan -> ducan.getRadnici().toString().contains(radnik))
                    .filter(ducan -> ducan.getArtikli().toString().contains(artikl))
                    .toList();

            skladisteTableView.setItems(FXCollections.observableList(filtriranoSkladiste));

        }

        else {

            List<Skladiste> filtriranoSkladiste = IzbornikController.getListaSkladista().stream()
                    .filter(ducan -> ducan.getNaziv().contains(naziv))
                    .filter(ducan -> ducan.getLokacija().getGrad().contains(lokacija.getGrad()) &&
                            ducan.getLokacija().getUlica().contains(lokacija.getUlica()) &&
                            ducan.getLokacija().getPostanskiBroj().contains(lokacija.getPostanskiBroj()))
                    .filter(ducan -> ducan.getRadnici().toString().contains(radnik))
                    .filter(ducan -> ducan.getArtikli().toString().contains(artikl))
                    .toList();

            skladisteTableView.setItems(FXCollections.observableList(filtriranoSkladiste));

        }


    }

    public void updateSkladiste() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izmijeniti odabrano skladište?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            String naziv = nazivTextField.getText();
            Lokacija lokacija = lokacijaComboBox.getValue();
            String radnik = radnikTextField.getText();
            String artikl = artiklTextField.getText();
            List<Radnik> listaRadnika = skladisteID.get(index).getRadnici();
            List<Artikl> listaArtikala = skladisteID.get(index).getArtikli();

            Skladiste staroSkladiste = skladisteID.get(index);

            String testSkladiste = staroSkladiste.getNaziv() + "\n" +
                    staroSkladiste.getLokacija().toString() + "\n" +
                    staroSkladiste.getRadnici().toString() + "\n" +
                    staroSkladiste.getArtikli().toString();

            index = skladisteTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = skladisteID.get(index).getId();

            Skladiste skladiste = new Skladiste(id, naziv, lokacija, listaRadnika, listaArtikala);

            String novoSkladiste = skladiste.getNaziv() + "\n" +
                    skladiste.getLokacija().toString() + "\n" +
                    skladiste.getRadnici().toString() + "\n" +
                    skladiste.getArtikli().toString();

            String prijavljeniKorisnik = PrijavaController.getKorisnik().getKorisnickoIme();

            String vrijemePromjene = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

            BiljeskePromjena.zapisiPromjenu(testSkladiste, novoSkladiste, prijavljeniKorisnik,
                    vrijemePromjene);

            IzbornikController.getListaSkladista().remove(index);
            IzbornikController.getListaSkladista().add(index, skladiste);

            try {
                BazaPodataka.updateSkladisteUBazu(skladiste);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Izmjena skladišta");
            alert.setHeaderText("Uspješno izmjenjeno skladište!");
            alert.setContentText("Skladište: " + naziv + " uspješno izmjenjen!");
            alert.showAndWait();
            logger.info("Korisnik je izmjenio skladište: " + naziv + ".");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("skladiste.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga skladišta");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

    public void deleteSkladiste() {

        String naziv = nazivTextField.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izbrisati odabrano skladište?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            index = skladisteTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = skladisteID.get(index).getId();


            IzbornikController.getListaSkladista().remove(index);

            try {
                BazaPodataka.deleteSkladisteIzBaze(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje skladišta");
            alert.setHeaderText("Uspješno izbrisano skladište!");
            alert.setContentText("Skladište: " + naziv + " uspješno izbrisan!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso skladište: " + naziv + ".");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("skladiste.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga Skladišta");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();


        }

    }

}
