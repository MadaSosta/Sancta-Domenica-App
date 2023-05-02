package javaFX;

import baza.BazaPodataka;
import entiteti.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import prijava.Prijava;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static javaFX.Application.logger;

public class IzbornikController {

    private static List<Lokacija> listaLokacija;
    private static List<Radnik> listaRadnika;
    private static List<Kategorija> listaKategorija;
    private static List<Artikl> listaArtikala;
    private static List<Ducan> listaDucana;
    private static List<Skladiste> listaSkladista;
    private static List<Kupac> listaKupaca;

    public void showLokacijaPretraga() throws IOException {
        dodajLokacije();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("lokacija.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Pretraga Lokacija");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    public void showLokacijaUnos() throws IOException {
        dodajLokacije();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("unosNoveLokacije.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Unos Lokacije");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    private void dodajLokacije() {
        try {
            listaLokacija = BazaPodataka.dohvatiLokacijeIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Lokacija> getListaLokacija() {
        return listaLokacija;
    }

    public void showRadnikPretraga() throws IOException {
        dodajRadnike();
        dodajLokacije();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("radnik.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Pretraga Radnika");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    public void showRadnikUnos() throws IOException {
        dodajRadnike();
        dodajLokacije();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("unosNovogRadnika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Unos Radnika");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    private void dodajRadnike() {
        try {
            listaRadnika = BazaPodataka.dohvatiRadnikeIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Radnik> getListaRadnika() {
        return listaRadnika;
    }

    public void showKategorijaPretraga() throws IOException {
        dodajKategorije();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("kategorija.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Pregled Kategorija");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    public void showKategorijaUnos() throws IOException {
        dodajKategorije();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("unosNoveKategorije.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Unos Kategorije");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    private void dodajKategorije() {
        try {
            listaKategorija = BazaPodataka.dohvatiKategorijeIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Kategorija> getListaKategorija() {
        return listaKategorija;
    }

    public void showArtiklPretraga() throws IOException {
        dodajKategorije();
        dodajArtikle();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("artikl.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Pretraga Artikla");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    public void showArtiklUnos() throws IOException {
        dodajKategorije();
        dodajArtikle();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("unosNovogArtikla.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Unos Artikla");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    private void dodajArtikle() {
        try {
            listaArtikala = BazaPodataka.dohvatiArtikleIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Artikl> getListaArtikala() {
        return listaArtikala;
    }

    public void showDucanPretraga() throws IOException {
        dodajDucan();
        dodajLokacije();
        dodajArtikle();
        dodajRadnike();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("ducan.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Pretraga Dućana");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    public void showDucanUnos() throws IOException {
        dodajDucan();
        dodajLokacije();
        dodajArtikle();
        dodajRadnike();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("unosNovogDucana.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Unos Dućana");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    private void dodajDucan() {
        try {
            listaDucana = BazaPodataka.dohvatiDucaneIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Ducan> getListaDucana() {
        return listaDucana;
    }

    public void showSkladistePretraga() throws IOException {
        dodajSkladiste();
        dodajLokacije();
        dodajArtikle();
        dodajRadnike();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("skladiste.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Pretraga Skladišta");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    public void showSkladisteUnos() throws IOException {
        dodajSkladiste();
        dodajLokacije();
        dodajArtikle();
        dodajRadnike();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("unosNovogSkladista.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Unos Skladišta");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    private void dodajSkladiste() {

        try {
            listaSkladista = BazaPodataka.dohvatiSkladisteIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Skladiste> getListaSkladista() {
        return listaSkladista;
    }

    public void showKupacPretraga() throws IOException {
        dodajKupce();
        dodajArtikle();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("kupac.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Pretraga Kupaca");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    public void showKupacUnos() throws IOException {
        dodajKupce();
        dodajArtikle();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("unosNovogKupca.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Application.getMainStage().setTitle("Unos Kupca");
        Application.getMainStage().setScene(scene);
        Application.getMainStage().show();
    }

    private void dodajKupce() {

        try {
            listaKupaca = BazaPodataka.dohvatiKupcaIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Kupac> getListaKupaca() {
        return listaKupaca;
    }

    public void showPromjene() throws IOException {
        if(PrijavaController.getKorisnik().getRola() == (1)) {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("promjene.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 650, 650);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            Application.getMainStage().setTitle("Promjene");
            Application.getMainStage().setScene(scene);
            Application.getMainStage().show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Samo Admin može pristupiti logu!");
            alert.setContentText("Pitajte Admina za promjene!");
            alert.showAndWait();
            logger.error("Korisnik koji nije admin je pokušao pristupiti promjenama!");
        }
    }

}
