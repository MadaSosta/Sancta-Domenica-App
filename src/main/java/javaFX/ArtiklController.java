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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static javaFX.Application.logger;

public class ArtiklController {
    @FXML
    private ComboBox<Kategorija> kategorijeComboBox;

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

    @FXML
    private TableView<Artikl> artiklTableView;

    @FXML
    private TableColumn<Artikl, String> kategorijeTableColumn;

    @FXML
    private TableColumn<Artikl, String> nazivTableColumn;

    @FXML
    private TableColumn<Artikl, String> sirinaTableColumn;

    @FXML
    private TableColumn<Artikl, String> visinaTableColumn;

    @FXML
    private TableColumn<Artikl, String> duljinaTableColumn;

    @FXML
    private TableColumn<Artikl, String> masaTableColumn;

    @FXML
    private TableColumn<Artikl, String> cijenaProizvodnjeTableColumn;

    @FXML
    private TableColumn<Artikl, String> cijenaProdajeTableColumn;

    @FXML
    private TableColumn<Artikl, String> garancijaTableColumn;

    List<Artikl> artiklID;

    {
        try {
            artiklID = BazaPodataka.dohvatiArtikleIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {

        ObservableList<Kategorija> kategorijaObservableList = FXCollections.
                observableList(IzbornikController.getListaKategorija());
        kategorijeComboBox.setItems(kategorijaObservableList);

        kategorijeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getKategorija().getNaziv()));

        nazivTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getNaziv()));

        sirinaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getSirina().toString()));

        visinaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getVisina().toString()));

        duljinaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDuljina().toString()));

        masaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getMasa().toString()));

        cijenaProizvodnjeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getCijenaProizvodnje().toString()));

        cijenaProdajeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getCijenaProdaje().toString()));

        garancijaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getGarancija().toString()));

        artiklTableView.setItems(FXCollections.observableList(IzbornikController.getListaArtikala()));

        dohvatiArtikle();

    }

    int index = -1;
    public void getSelected(MouseEvent event) {

        index = artiklTableView.getSelectionModel().getSelectedIndex();

        if(index <= -1){
            return;
        }

        kategorijeComboBox.getSelectionModel().select(artiklID.get(index).getKategorija());
        nazivTextField.setText(nazivTableColumn.getCellData(index).toString());
        sirinaTextField.setText(sirinaTableColumn.getCellData(index).toString());
        visinaTextField.setText(visinaTableColumn.getCellData(index).toString());
        duljinaTextField.setText(duljinaTableColumn.getCellData(index).toString());
        masaTextField.setText(masaTableColumn.getCellData(index).toString());
        cijenaProizvodnjeTextField.setText(cijenaProizvodnjeTableColumn.getCellData(index).toString());
        cijenaProdajeTextField.setText(cijenaProdajeTableColumn.getCellData(index).toString());
        garancijaTextField.setText(garancijaTableColumn.getCellData(index).toString());

    }

    public void dohvatiArtikle() {

        Kategorija kategorija = kategorijeComboBox.getValue();
        String naziv = nazivTextField.getText();
        String sirina = sirinaTextField.getText();
        String visina = visinaTextField.getText();
        String duljina = duljinaTextField.getText();
        String masa = masaTextField.getText();
        String cijenaProizvodnje = cijenaProizvodnjeTextField.getText();
        String cijenaProdaje = cijenaProdajeTextField.getText();
        String garancija = garancijaTextField.getText();

        if(kategorija == null){

            List<Artikl> filtriraniArtikli = IzbornikController.getListaArtikala().stream()
                    .filter(artikl -> artikl.getNaziv().contains(naziv))
                    .filter(artikl -> artikl.getSirina().toString().contains(sirina))
                    .filter(artikl -> artikl.getVisina().toString().contains(visina))
                    .filter(artikl -> artikl.getDuljina().toString().contains(duljina))
                    .filter(artikl -> artikl.getMasa().toString().contains(masa))
                    .filter(artikl -> artikl.getCijenaProizvodnje().toString().contains(cijenaProizvodnje))
                    .filter(artikl -> artikl.getCijenaProdaje().toString().contains(cijenaProdaje))
                    .filter(artikl -> artikl.getGarancija().toString().contains(garancija))
                    .toList();

            artiklTableView.setItems(FXCollections.observableList(filtriraniArtikli));

        }

        else {

            List<Artikl> filtriraniArtikli = IzbornikController.getListaArtikala().stream()
                    .filter(artikl -> artikl.getKategorija().getNaziv().contains(kategorija.getNaziv()))
                    .filter(artikl -> artikl.getNaziv().contains(naziv))
                    .filter(artikl -> artikl.getSirina().toString().contains(sirina))
                    .filter(artikl -> artikl.getVisina().toString().contains(visina))
                    .filter(artikl -> artikl.getDuljina().toString().contains(duljina))
                    .filter(artikl -> artikl.getMasa().toString().contains(masa))
                    .filter(artikl -> artikl.getCijenaProizvodnje().toString().contains(cijenaProizvodnje))
                    .filter(artikl -> artikl.getCijenaProdaje().toString().contains(cijenaProdaje))
                    .filter(artikl -> artikl.getGarancija().toString().contains(garancija))
                    .toList();

            artiklTableView.setItems(FXCollections.observableList(filtriraniArtikli));

        }

    }

    public void updateArtikl() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izmijeniti odabrani artikl?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            Kategorija kategorija = kategorijeComboBox.getValue();
            String naziv = nazivTextField.getText();
            String sirina = sirinaTextField.getText();
            String visina = visinaTextField.getText();
            String duljina = duljinaTextField.getText();
            String masa = masaTextField.getText();
            String cijenaProizvodnje = cijenaProizvodnjeTextField.getText();
            String cijenaProdaje = cijenaProdajeTextField.getText();
            String garancija = garancijaTextField.getText();

            Artikl stariArtikl = artiklID.get(index);
            String testArtikl = stariArtikl.getKategorija().getNaziv() + " " + stariArtikl.getNaziv() + "\n"
                    + stariArtikl.getSirina() + " x " + stariArtikl.getVisina() + " x " + stariArtikl.getDuljina() +
                    " mm" + "\n" + stariArtikl.getMasa() + " kg" + "\n" + "Cijena proizvodnje: " +
                    stariArtikl.getCijenaProizvodnje() + " kn" + "\n" + "Cijena prodaje: " +
                    stariArtikl.getCijenaProdaje() + " kn" + "\n" + "Garancija: " + stariArtikl.getGarancija();

            index = artiklTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = artiklID.get(index).getId();

            Artikl artikl = new Artikl(id, naziv, kategorija, Double.parseDouble(sirina), Double.parseDouble(visina),
                    Double.parseDouble(duljina), Double.parseDouble(masa), Double.parseDouble(cijenaProizvodnje),
                    Double.parseDouble(cijenaProdaje), Integer.parseInt(garancija));

            String noviArtikl = artikl.getKategorija().getNaziv() + " " + artikl.getNaziv() + "\n"
                    + artikl.getSirina() + " x " + artikl.getVisina() + " x " + artikl.getDuljina() + " mm" + "\n" +
                    artikl.getMasa() + " kg" + "\n" + "Cijena proizvodnje: " + artikl.getCijenaProizvodnje() + " kn" +
                    "\n" + "Cijena prodaje: " + artikl.getCijenaProdaje() + " kn" + "\n" + "Garancija: " +
                    artikl.getGarancija();

            String prijavljeniKorisnik = PrijavaController.getKorisnik().getKorisnickoIme();

            String vrijemePromjene = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

            BiljeskePromjena.zapisiPromjenu(testArtikl, noviArtikl, prijavljeniKorisnik,
                    vrijemePromjene);

            IzbornikController.getListaArtikala().remove(index);
            IzbornikController.getListaArtikala().add(index, artikl);

            try {
                BazaPodataka.updateArtiklUBazu(artikl);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Izmjena artikla");
            alert.setHeaderText("Uspješno izmjenjen artikl!");
            alert.setContentText("Artikl: " + naziv + " uspješno izmjenjen!");
            alert.showAndWait();
            logger.info("Korisnik je izmjenio artikl: " + naziv + ".");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("artikl.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga artikla");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

    public void deleteArtikl() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izbrisati odabrani artikl?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            Kategorija kategorija = kategorijeComboBox.getValue();
            String naziv = nazivTextField.getText();
            String sirina = sirinaTextField.getText();
            String visina = visinaTextField.getText();
            String duljina = duljinaTextField.getText();
            String masa = masaTextField.getText();
            String cijenaProizvodnje = cijenaProizvodnjeTextField.getText();
            String cijenaProdaje = cijenaProdajeTextField.getText();
            String garancija = garancijaTextField.getText();

            index = artiklTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = artiklID.get(index).getId();

            Artikl artikl = new Artikl(id, naziv, kategorija, Double.parseDouble(sirina), Double.parseDouble(visina),
                    Double.parseDouble(duljina), Double.parseDouble(masa), Double.parseDouble(cijenaProizvodnje),
                    Double.parseDouble(cijenaProdaje), Integer.parseInt(garancija));

            IzbornikController.getListaArtikala().remove(index);

            try {
                BazaPodataka.deleteArtiklIzBaze(artikl);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje artikla");
            alert.setHeaderText("Uspješno izbrisan artikl!");
            alert.setContentText("Artikl: " + naziv + " uspješno izbrisan!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso artikl: " +naziv + ".");

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

    }

}
