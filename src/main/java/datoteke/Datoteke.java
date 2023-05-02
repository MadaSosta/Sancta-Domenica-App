package datoteke;

import entiteti.*;
import prijava.Prijava;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static javaFX.Application.logger;

public class Datoteke {

    private static final Integer BROJ_ZAPISA_LOGINOVA = 3;

    Scanner unos = new Scanner(System.in);

    static List<Prijava> listaPrijava = new ArrayList<>();

    public static List<Prijava> provjeraPrijave() {

        try {
            FileReader readerPrijava = new FileReader(new File("dat/prijava.txt"));
            BufferedReader bufReaderPrijava = new BufferedReader(readerPrijava);
            List<String> datotekaPrijava = bufReaderPrijava.lines().collect(Collectors.toList());

            for(int i = 0; i < datotekaPrijava.size() / BROJ_ZAPISA_LOGINOVA; i++){
                String korisnickoIme = datotekaPrijava.get(i * BROJ_ZAPISA_LOGINOVA);
                String lozinka = datotekaPrijava.get(i * BROJ_ZAPISA_LOGINOVA + 1);
                String rola = datotekaPrijava.get(i * BROJ_ZAPISA_LOGINOVA + 2);
            //    String hashiranaLozinka = hashLozinke(lozinka);

            //    zapisiKorisnika(korisnickoIme,hashiranaLozinka);

                listaPrijava.add(new Prijava(korisnickoIme, lozinka, Long.parseLong(rola)));

            }

        } catch (FileNotFoundException e) {
            System.out.println("Datoteka se nije otvorila ili ne postoji takva datoteka.");
            logger.error("Ne postoji takva datoteka ili se datoteka nije otvorila.");
        }

    /*    for (int i = 0; i < listaPrijava.size(); i++){
            zapisiKorisnika(listaPrijava.get(i).getKorisnickoIme(), listaPrijava.get(i).getLozinka());
        }*/

        return listaPrijava;
    }

    public static String hashLozinke(String lozinka) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(lozinka.getBytes());
            byte[] bajtPolje = messageDigest.digest();

            StringBuilder sb = new StringBuilder();
            for(byte b : bajtPolje){
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
    public static void zapisiKorisnika(String korisnickoIme, String lozinka) {

        try {
            PrintWriter printWriter = new PrintWriter("dat/prijava.txt");
            for(int i = 0; i < listaPrijava.size(); i++){
                printWriter.write(listaPrijava.get(i).getKorisnickoIme());
                printWriter.write("\n");
                printWriter.write(listaPrijava.get(i).getLozinka());
                if(i < listaPrijava.size() - 1){
                    printWriter.write("\n");
                }
            }

            printWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
