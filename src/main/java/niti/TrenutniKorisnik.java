package niti;

import javaFX.Application;
import javaFX.PrijavaController;

import static javaFX.Application.logger;

public class TrenutniKorisnik implements Runnable{
    @Override
    public void run() {

        try{

            String prijavljeniKorisnik = PrijavaController.getKorisnik().getKorisnickoIme();

            Application.setMainStageTitle("Prijavljeni korisnik: " + prijavljeniKorisnik);
        }catch (RuntimeException ex){
            logger.info("Osoba se nije još prijavila pa ne mogu dohvatit korisnika!");
        }


    }
}
