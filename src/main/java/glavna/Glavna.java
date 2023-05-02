package glavna;

import baza.BazaPodataka;
import entiteti.*;
import javaFX.IzbornikController;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Glavna {

    public static void main(String[] args) {

        List<Kategorija> kategorije;
        List<Artikl> artikli;

        try {
            kategorije = BazaPodataka.dohvatiKategorijeIzBaze();
            artikli = BazaPodataka.dohvatiArtikleIzBaze();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Lokacija lokacija = new Lokacija(Long.parseLong("1"), "Jankomir 33", "Zagreb", "10090");
        Lokacija lokacija2 = new Lokacija(Long.parseLong("2"), "Trg Ivana Kukuljevića 12", "Zagreb",
                "10090");

        Set<Radnik> radnici = new HashSet<>();
        List<Radnik> listaRadnika = new ArrayList<>();
        List<Artikl> listaArtikla = new ArrayList<>();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        String datum = "12.12.1982.";
        LocalDate datumRodjenja = LocalDate.parse(datum, dtf);

        Radnik radnik = new Radnik(Long.parseLong("1"), "Marinko", "Pranjić",
                LocalDate.now().minusYears(20), lokacija2, 7500.00);
        Radnik radnik2 = new Radnik(Long.parseLong("2"), "Darinka", "Loeb",
                LocalDate.now().minusYears(15), lokacija, 6000.00);
        Radnik radnik3 = new Radnik(Long.parseLong("3"), "Miroslav", "Budućnost",
                datumRodjenja, lokacija, 16000.00);

        radnici.add(radnik);
        radnici.add(radnik2);
        radnici.add(radnik2);

        listaRadnika.add(radnik);
        listaRadnika.add(radnik2);
        listaRadnika.add(radnik3);

        List<Radnik> specijalniRadnici = new ArrayList<>();

        specijalniRadnici = new Radnik().rodjendanRadnikaNaDanasnjiDan(listaRadnika);

        System.out.println("Radnici koji imaju danas rođendan: ");

        for(Radnik rad : specijalniRadnici){
            System.out.println(rad.getIme() + " " + rad.getPrezime() + " " +
                    rad.getDatumRodjenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        }

        System.out.println("");

        System.out.println("Ispis liste radnika: ");

        for(Radnik rad : listaRadnika){
            System.out.println(rad.getIme() + " " + rad.getPrezime() + " " +
                    rad.getDatumRodjenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + " ima plaću " +
                    rad.getPlaca() + "kn.");
        }

        System.out.println("");


        System.out.println("Ispis seta radnika: ");

        for(Radnik rad : radnici){
            System.out.println(rad.getIme() + " " + rad.getPrezime() + " " +
                    rad.getDatumRodjenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + " ima plaću " +
                    rad.getPlaca() + "kn.");
        }

        System.out.println("");

        Kategorija kategorija = new Kategorija(Long.parseLong("1"), "Laptop");
        Kategorija kategorija2 = new Kategorija(Long.parseLong("2"), "Mobitel");

        Artikl artikl = new Artikl(Long.parseLong("1"), "Lenovo Legion 5", kategorija, 1550.00,
                1450.00, 0.520, 1.2, 1250.00, 7500.00, 2);

        Artikl artikl2 = new Artikl(Long.parseLong("2"), "Sony Xperia XZ Premium", kategorija2, 156.00,
                77.00, 0.079, 0.195, 1000.00, 6500.00, 2);

        listaArtikla.add(artikl);
        listaArtikla.add(artikl2);

        Double izracunProfita;

        System.out.println("Izračuni profita: ");

        for (int i = 0; i < listaArtikla.size(); i++){
            izracunProfita = new Artikl().izracunProfita(listaArtikla.get(i).getCijenaProizvodnje(),
                    listaArtikla.get(i).getCijenaProdaje());

            System.out.println("Izračun profita za " + listaArtikla.get(i).getNaziv() + " iznosi " + izracunProfita +
                    "kn");

        }

        System.out.println("");

        System.out.println("Primjer mape:");

        Map<Kategorija, List<Artikl>> mapa = new HashMap<>();

        for (int i = 0; i < kategorije.size(); i++) {
            List<Artikl> list = new ArrayList<>(0);
            for (int j = 0; j < artikli.size(); j++) {
                if(kategorije.get(i).getNaziv()
                        .equals(artikli.get(j).getKategorija().getNaziv())){
                    list.add(artikli.get(j));
                }

                mapa.put(kategorije.get(i), list);
            }
        }

        System.out.println(mapa);

        System.out.println("");


        Skladiste skladiste = new Skladiste(Long.parseLong("1"), "Sancta Domenia skladište",
                lokacija, listaRadnika, listaArtikla);

        Ducan ducan = new Ducan(Long.parseLong("1"), "Sancta Domenica Špansko", lokacija2, listaRadnika,
                listaArtikla);

        Kupac kupac = new Kupac(Long.parseLong("1"), "Dmitar", "Zvonimir", listaArtikla,
                6000.85, LocalDateTime.now());

        VanjskoSkladiste<Skladiste> vs = new VanjskoSkladiste<>(skladiste);
        VanjskoSkladiste<Ducan> vd = new VanjskoSkladiste<>(ducan);
        VanjskoSkladiste<Osoba> rd = new VanjskoSkladiste<>(radnik);

        System.out.println(vs.getVanjskoSkladiste().getNaziv() + " " + vs.getVanjskoSkladiste().getLokacija());
        System.out.println(vd.getVanjskoSkladiste().getNaziv() + " " + vd.getVanjskoSkladiste().getLokacija());
        System.out.println(rd.getVanjskoSkladiste().getIme() + " " + rd.getVanjskoSkladiste().getPrezime());

        Racun<Radnik, Kupac> radnikKupacRacun = new Racun<>(radnik, kupac);
        System.out.println(radnikKupacRacun.getIzdavacRacuna().getIme() + " je potrošio "
                + radnikKupacRacun.getPrimateljRacuna().getPotrosenoNovaca() + "kn.");

    }

}
