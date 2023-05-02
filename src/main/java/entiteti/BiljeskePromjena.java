package entiteti;

import niti.CitanjeDatotekeNit;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BiljeskePromjena implements Serializable {

    private String staraVrijednost;
    private String novaVrijednost;
    private String korisnik;
    private String datumIVrijeme;

    public BiljeskePromjena(String staraVrijednost, String novaVrijednost, String korisnik, String datumIVrijeme) {
        this.staraVrijednost = staraVrijednost;
        this.novaVrijednost = novaVrijednost;
        this.korisnik = korisnik;
        this.datumIVrijeme = datumIVrijeme;
    }

    public String getStaraVrijednost() {
        return staraVrijednost;
    }

    public void setStaraVrijednost(String staraVrijednost) {
        this.staraVrijednost = staraVrijednost;
    }

    public String getNovaVrijednost() {
        return novaVrijednost;
    }

    public void setNovaVrijednost(String novaVrijednost) {
        this.novaVrijednost = novaVrijednost;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getDatumIVrijeme() {
        return datumIVrijeme;
    }

    public void setDatumIVrijeme(String datumIVrijeme) {
        this.datumIVrijeme = datumIVrijeme;
    }


    public static void zapisiPromjenu(String staraVrijednost, String novaVrijednost,
                                      String korisnik, String datumIVrijeme){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
        LocalDateTime datumIVrijemePromjene = LocalDateTime.parse(datumIVrijeme, dtf);

        BiljeskePromjena noveBiljeska = new BiljeskePromjena(staraVrijednost, novaVrijednost,
                korisnik, datumIVrijemePromjene.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")));

        List<BiljeskePromjena> biljeskeLista = new ArrayList<>();
        biljeskeLista = getBiljeskaList();
        biljeskeLista.add(noveBiljeska);

        savePromjena(biljeskeLista);

    }

    private static void savePromjena(List<BiljeskePromjena> biljeskeLista){
        try(ObjectOutputStream objectWriter = new ObjectOutputStream(new FileOutputStream(
                "dat/serializacija.dat"))) {

            objectWriter.writeObject(biljeskeLista);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<BiljeskePromjena> getBiljeskaList(){

        List<BiljeskePromjena> biljeskeLista = new ArrayList<>();
        try (ObjectInputStream objetctReader = new ObjectInputStream(new FileInputStream(
                "dat/serializacija.dat"))){

            biljeskeLista = (List<BiljeskePromjena>) objetctReader.readObject();

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
            
        return biljeskeLista;

    }

    @Override
    public String toString() {
        return "BiljeskePromjena{" +
                "staraVrijednost='" + staraVrijednost + '\'' +
                ", novaVrijednost='" + novaVrijednost + '\'' +
                ", korisnik='" + korisnik + '\'' +
                ", datumIVrijeme='" + datumIVrijeme + '\'' +
                '}';
    }

}
