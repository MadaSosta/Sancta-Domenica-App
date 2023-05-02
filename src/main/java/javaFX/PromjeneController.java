package javaFX;

import entiteti.BiljeskePromjena;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PromjeneController {

    @FXML
    private TableColumn<BiljeskePromjena, String> prijeVrijednostTableCoulumn;

    @FXML
    private TableColumn<BiljeskePromjena, String> poslijeVrijednostTableCoulumn;

    @FXML
    private TableColumn<BiljeskePromjena, String> korisnikTableCoulumn;

    @FXML
    private TableColumn<BiljeskePromjena, String> datumIVrijemeTableCoulumn;

    @FXML
    private TableView<BiljeskePromjena> biljeskePromjenaTableView;

    public void initialize(){

        prijeVrijednostTableCoulumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getStaraVrijednost()));

        poslijeVrijednostTableCoulumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getNovaVrijednost()));

        korisnikTableCoulumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getKorisnik()));

        datumIVrijemeTableCoulumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDatumIVrijeme()));

        biljeskePromjenaTableView.setItems(FXCollections.observableList(BiljeskePromjena.getBiljeskaList()));

    }

}
