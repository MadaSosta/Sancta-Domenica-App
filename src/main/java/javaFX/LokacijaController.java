package javaFX;

import baza.BazaPodataka;
import entiteti.BiljeskePromjena;
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
import prijava.Prijava;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javaFX.Application.logger;

public class LokacijaController {

    @FXML
    private TextField ulicaTextField;
    @FXML
    private TextField gradTextField;
    @FXML
    private TextField postanskiBrojTextField;

    @FXML
    private TableView<Lokacija> lokacijaTableView;
    @FXML
    private TableColumn<Lokacija, String> ulicaTableColumn;
    @FXML
    private TableColumn<Lokacija, String> gradTableColumn;
    @FXML
    private TableColumn<Lokacija, String> postanskiBrojTableColumn;

    List<Lokacija> lokacijaID;

    {
        try {
            lokacijaID = BazaPodataka.dohvatiLokacijeIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {

        ulicaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUlica()));

        gradTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getGrad()));

        postanskiBrojTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPostanskiBroj()));

        lokacijaTableView.setItems(FXCollections.observableList(IzbornikController.getListaLokacija()));

        dohvatiLokacije();

    }

    int index = -1;
    public void getSelected(MouseEvent event) {

        index = lokacijaTableView.getSelectionModel().getSelectedIndex();

        if(index <= -1){
            return;
        }

        ulicaTextField.setText(ulicaTableColumn.getCellData(index).toString());
        gradTextField.setText(gradTableColumn.getCellData(index).toString());
        postanskiBrojTextField.setText(postanskiBrojTableColumn.getCellData(index).toString());

    }

    public void dohvatiLokacije() {
        String ulica = ulicaTextField.getText();
        String grad = gradTextField.getText();
        String postanskiBroj = postanskiBrojTextField.getText();

        List<Lokacija> filtriraneLokacije = IzbornikController.getListaLokacija().stream()
                .filter(lokacija -> lokacija.getUlica().contains(ulica))
                .filter(lokacija -> lokacija.getGrad().contains(grad))
                .filter(lokacija ->  lokacija.getPostanskiBroj().contains(postanskiBroj))
                .toList();

        lokacijaTableView.setItems(FXCollections.observableList(filtriraneLokacije));

    }

    public void updateLokacija() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izmijeniti odabranu lokaciju?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            String ulica = ulicaTextField.getText();
            String grad = gradTextField.getText();
            String postanskiBroj = postanskiBrojTextField.getText();
            Lokacija staraLokacija = lokacijaID.get(index);
            index = lokacijaTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = lokacijaID.get(index).getId();

            Lokacija lokacija = new Lokacija(id, ulica, grad, postanskiBroj);

            Lokacija novaLokacija = lokacija;

            String prijavljeniKorisnik = PrijavaController.getKorisnik().getKorisnickoIme();

            String vrijemePromjene = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));

            BiljeskePromjena.zapisiPromjenu(staraLokacija.toString(), novaLokacija.toString(),
                    prijavljeniKorisnik, vrijemePromjene);

            IzbornikController.getListaLokacija().remove(index);
            IzbornikController.getListaLokacija().add(index, lokacija);

            try {
                BazaPodataka.updateLokacijeUBazu(lokacija);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Izmjena lokacije");
            alert.setHeaderText("Uspješno izmijenjena lokacija!");
            alert.setContentText("Lokacija: " + ulica + ", " + grad + ", " + postanskiBroj + " uspješno izmjenjena!");
            alert.showAndWait();
            logger.info("Korisnik je izmjenio lokaciju: " + ulica + ", " + grad + ", " + postanskiBroj + ".");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("lokacija.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga Lokacija");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();

        }

    }

    public void deleteLokacija() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Želite izbrisati odabranu lokaciju?");
        alert.setContentText("Slažete li se sa time?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            String ulica = ulicaTextField.getText();
            String grad = gradTextField.getText();
            String postanskiBroj = postanskiBrojTextField.getText();

            index = lokacijaTableView.getSelectionModel().getSelectedIndex();

            if(index <= -1){
                return;
            }

            Long id = lokacijaID.get(index).getId();

            Lokacija lokacija = new Lokacija(id, ulica, grad, postanskiBroj);

            IzbornikController.getListaLokacija().remove(index);

            try {
                BazaPodataka.deleteLokacijeIzBaze(lokacija);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje lokacije");
            alert.setHeaderText("Uspješno izbrisana lokacija!");
            alert.setContentText("Lokacija: " + ulica + ", " + grad + ", " + postanskiBroj + " uspješno izbrisana!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso lokaciju: " + ulica + ", " + grad + ", " + postanskiBroj + ".");

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("lokacija.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.getMainStage().setTitle("Pretraga Lokacija");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        }

    }

}
