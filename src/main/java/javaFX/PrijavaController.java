package javaFX;

import datoteke.Datoteke;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prijava.Prijava;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static javaFX.Application.logger;
public class PrijavaController {

    static Prijava prijavljeniKorisnik;

    @FXML
    ImageView imageView;

    @FXML
    private TextField korisnickoImeTextField;

    @FXML
    private PasswordField lozinkaPasswordField;

    public void initialize() {
        File file = new File("img/login.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    public void prijaviSe() {
        String korisnickoIme = korisnickoImeTextField.getText();
        String lozinka = lozinkaPasswordField.getText();

        String hashLozinka = Datoteke.hashLozinke(lozinka);

        List<Prijava> listaPrijava = Datoteke.provjeraPrijave();

        for(int i = 0; i < listaPrijava.size(); i++){
            if(korisnickoIme.equals(listaPrijava.get(i).getKorisnickoIme()) &&
                    hashLozinka.equals(listaPrijava.get(i).getLozinka())) {
                prijavljeniKorisnik = listaPrijava.get(i);
                logger.info(listaPrijava.get(i).getKorisnickoIme() + " se uspješno prijavio u " +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")) +"!");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Prijava");
                alert.setHeaderText("Dobrodošli " + korisnickoIme + ".");
                alert.setContentText("Uspješno ste prijavljeni!");
                alert.showAndWait();

                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("pocetna.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 650, 650);
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Application.getMainStage().setTitle("Početna");
                Application.getMainStage().setScene(scene);
                Application.getMainStage().show();
                break;



            }

            if(korisnickoIme.isBlank() == true && lozinka.isBlank() == true){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Prijava");
                alert.setHeaderText("Niste upisali korisničko ime i lozinku!");
                alert.setContentText("Morate upisati oba polja!");
                alert.showAndWait();
                break;
            }

            if(korisnickoIme.isBlank() == true){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Prijava");
                alert.setHeaderText("Niste upisali korisničko ime!");
                alert.setContentText("Morate upisati korisničko ime!");
                alert.showAndWait();
                break;
            }

            if(lozinka.isBlank() == true){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Prijava");
                alert.setHeaderText("Niste upisali lozinku!");
                alert.setContentText("Morate upisati lozinku!");
                alert.showAndWait();
                break;
            }

            else if (i == listaPrijava.size() - 1){
                System.out.println("Niste upisali dobro korisničko ime ili lozinku!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Prijava");
                alert.setHeaderText("Ne postoji " + korisnickoIme + " ili ste krivo upisali lozinku.");
                alert.setContentText("Neuspješna prijava!");
                alert.showAndWait();
                logger.error("Neuspješna prijava u " +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")) + "!");
            }

        }

    }

    public static Prijava getKorisnik() {
        return prijavljeniKorisnik;
    }


}