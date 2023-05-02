package baza;

import entiteti.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class BazaPodataka {

    private static Connection connectToDatabase() throws SQLException, IOException {

        Properties configuration = new Properties();
        configuration.load(new FileReader("dat/database.properties"));

        String databaseURL = configuration.getProperty("databaseURL");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");

        Connection connection = DriverManager
                .getConnection(databaseURL, databaseUsername, databasePassword);

        return connection;

    }

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if(list.size() != 1) {
                        System.out.println("Pronašo sam više objekta studenta s istim id.");
                    }
                    return list.get(0);
                }
        );
    }

    public static List<Lokacija> dohvatiLokacijeIzBaze() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Lokacija> listaLokacija = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM LOKACIJA";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Long id = rs.getLong("ID");
            String ulica = rs.getString("ULICA");
            String grad = rs.getString("GRAD");
            String postanskiBroj = rs.getString("POSTANSKI_BROJ");

            Lokacija lokacija = new Lokacija(id, ulica, grad, postanskiBroj);
            listaLokacija.add(lokacija);

        }

        return listaLokacija;

    }

    public static void dodajNovuLokacijuUBazu(Lokacija lokacija) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("INSERT INTO LOKACIJA(ULICA, GRAD, POSTANSKI_BROJ) VALUES (?, ?, ?)");
        ps.setString(1, lokacija.getUlica());
        ps.setString(2, lokacija.getGrad());
        ps.setString(3, lokacija.getPostanskiBroj());
        ps.executeUpdate();

        connection.close();

    }

    public static void updateLokacijeUBazu(Lokacija lokacija) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("UPDATE LOKACIJA SET ULICA = ?, GRAD = ?, POSTANSKI_BROJ = ? WHERE ID = ?");
        ps.setString(1, lokacija.getUlica());
        ps.setString(2, lokacija.getGrad());
        ps.setString(3, lokacija.getPostanskiBroj());
        ps.setLong(4, lokacija.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteLokacijeIzBaze(Lokacija lokacija) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM LOKACIJA WHERE ID = ?");
        ps.setLong(1, lokacija.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static List<Radnik> dohvatiRadnikeIzBaze() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Radnik> listaRadnika = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM RADNIK";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Long id = rs.getLong("ID");
            String ime = rs.getString("IME");
            String prezime = rs.getString("PREZIME");
            Date datumRodjenja = rs.getDate("DATUM_RODJENJA");
            Long lokacija_id = rs.getLong("LOKACIJA_ID");
            Double placa = rs.getDouble("PLACA");

            Lokacija lokacija = dohvatiLokacijeIzBaze().stream()
                    .filter(lokacija1 -> lokacija1.getId().equals(lokacija_id)).collect(toSingleton());

            Radnik radnik = new Radnik(id, ime, prezime, datumRodjenja.toLocalDate(), lokacija, placa);
            listaRadnika.add(radnik);

        }

        return listaRadnika;

    }



    public static void dodajNovogRadnikaUBazu(Radnik radnik) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("INSERT INTO RADNIK(IME, PREZIME, DATUM_RODJENJA, LOKACIJA_ID, PLACA) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, radnik.getIme());
        ps.setString(2, radnik.getPrezime());
        ps.setString(3, String.valueOf(radnik.getDatumRodjenja()));
        ps.setLong(4, radnik.getLokacija().getId());
        ps.setDouble(5, radnik.getPlaca());
        ps.executeUpdate();

        connection.close();

    }

    public static void updateRadnikaUBazu(Radnik radnik) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("UPDATE RADNIK SET IME = ?, PREZIME = ?, DATUM_RODJENJA = ?, LOKACIJA_ID = ?, PLACA = ? WHERE ID = ?");
        ps.setString(1, radnik.getIme());
        ps.setString(2, radnik.getPrezime());
        ps.setString(3, String.valueOf(radnik.getDatumRodjenja()));
        ps.setLong(4, radnik.getLokacija().getId());
        ps.setDouble(5, radnik.getPlaca());
        ps.setLong(6, radnik.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteRadnikaIzBaze(Radnik radnik) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM RADNIK WHERE ID = ?");
        ps.setLong(1, radnik.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static List<Kategorija> dohvatiKategorijeIzBaze() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Kategorija> listaKategorija = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM KATEGORIJA";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");

            Kategorija kategorija = new Kategorija(id, naziv);
            listaKategorija.add(kategorija);

        }

        return listaKategorija;

    }

    public static void dodajNovuKategorijuUBazu(Kategorija kategorija) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("INSERT INTO KATEGORIJA(NAZIV) VALUES (?)");
        ps.setString(1, kategorija.getNaziv());

        ps.executeUpdate();

        connection.close();

    }

    public static void updateKategorijeUBazu(Kategorija kategorija) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("UPDATE KATEGORIJA SET NAZIV = ? WHERE ID = ?");
        ps.setString(1, kategorija.getNaziv());
        ps.setLong(2, kategorija.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteKategorijaIzBaze(Kategorija kategorija) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM KATEGORIJA WHERE ID = ?");
        ps.setLong(1, kategorija.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static List<Artikl> dohvatiArtikleIzBaze() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Artikl> listaArtikla = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM ARTIKL";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            Long kategorijaID = rs.getLong("KATEGORIJA_ID");
            Double sirina = rs.getDouble("SIRINA");
            Double visina = rs.getDouble("VISINA");
            Double duljina = rs.getDouble("DULJINA");
            Double masa = rs.getDouble("MASA");
            Double cijenaProizvodnje = rs.getDouble("CIJENA_PROIZVODNJE");
            Double cijenaProdaje = rs.getDouble("CIJENA_PRODAJE");
            Integer garancija = rs.getInt("GARANCIJA");

            Kategorija kategorija = dohvatiKategorijeIzBaze().stream()
                    .filter(kategorija1 -> kategorija1.getId().equals(kategorijaID)).collect(toSingleton());

            Artikl artikl = new Artikl(id, naziv, kategorija, sirina, visina, duljina, masa, cijenaProizvodnje,
                    cijenaProdaje, garancija);

            listaArtikla.add(artikl);

        }

        return listaArtikla;

    }



    public static void dodajNoviArtiklUBazu(Artikl artikl) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("INSERT INTO ARTIKL(NAZIV, KATEGORIJA_ID, SIRINA, VISINA, DULJINA, MASA, CIJENA_PROIZVODNJE, " +
                        "CIJENA_PRODAJE, GARANCIJA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

        ps.setString(1, artikl.getNaziv());
        ps.setLong(2, artikl.getKategorija().getId());
        ps.setDouble(3, artikl.getSirina());
        ps.setDouble(4, artikl.getVisina());
        ps.setDouble(5, artikl.getDuljina());
        ps.setDouble(6, artikl.getMasa());
        ps.setDouble(7, artikl.getCijenaProizvodnje());
        ps.setDouble(8, artikl.getCijenaProdaje());
        ps.setInt(9, artikl.getGarancija());

        ps.executeUpdate();

        connection.close();

    }

    public static void updateArtiklUBazu(Artikl artikl) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("UPDATE ARTIKL SET NAZIV = ?, KATEGORIJA_ID = ?, SIRINA = ?, VISINA = ?, DULJINA = ?, MASA = ? ," +
                        "CIJENA_PROIZVODNJE = ?, CIJENA_PRODAJE = ?, GARANCIJA = ? WHERE ID = ?");
        ps.setString(1, artikl.getNaziv());
        ps.setLong(2, artikl.getKategorija().getId());
        ps.setDouble(3, artikl.getSirina());
        ps.setDouble(4, artikl.getVisina());
        ps.setDouble(5, artikl.getDuljina());
        ps.setDouble(6, artikl.getMasa());
        ps.setDouble(7, artikl.getCijenaProizvodnje());
        ps.setDouble(8, artikl.getCijenaProdaje());
        ps.setInt(9, artikl.getGarancija());
        ps.setLong(10, artikl.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteArtiklIzBaze(Artikl artikl) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM ARTIKL WHERE ID = ?");
        ps.setLong(1, artikl.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static List<Ducan> dohvatiDucaneIzBaze() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Ducan> listaDucana = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM DUCAN";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            Long lokacijaID = rs.getLong("LOKACIJA_ID");
            List<Radnik> listaRadnika = dohvatiRadnikaPomocuIdIzBaze(id);
            List<Artikl> listaArtikala = dohvatiArtiklPomocuIdIzBaze(id);

            Lokacija lokacija = dohvatiLokacijeIzBaze().stream()
                    .filter(lokacija1 -> lokacija1.getId().equals(lokacijaID)).collect(toSingleton());

            Ducan ducan = new Ducan(id, naziv, lokacija, listaRadnika, listaArtikala);

            listaDucana.add(ducan);

        }

        connection.close();

        return listaDucana;

    }

    private static List<Artikl> dohvatiArtiklPomocuIdIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Artikl> listaArtikala = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM DUCAN_ARTIKL";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()){

            Long ducanID = rs.getLong("DUCAN_ID");
            Long artiklID = rs.getLong("ARTIKL_ID");

            if(ducanID.equals(id)){
                Artikl artikl = dohvatiArtikleIzBaze().stream()
                        .filter(artikl1 -> artikl1.getId().equals(artiklID)).collect(toSingleton());
                listaArtikala.add(artikl);
            }

        }

        connection.close();

        return listaArtikala;
    }

    private static List<Radnik> dohvatiRadnikaPomocuIdIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Radnik> listaRadnika = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM DUCAN_RADNIK";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()){
            Long ducanID = rs.getLong("DUCAN_ID");
            Long radnikID = rs.getLong("RADNIK_ID");

            if(ducanID.equals(id)){
                Radnik radnik = dohvatiRadnikeIzBaze().stream()
                        .filter(radnik1 -> radnik1.getId().equals(radnikID)).collect(toSingleton());
                listaRadnika.add(radnik);
            }

        }

        return listaRadnika;

    }

    public static void dodajNoviDucanUBazu(Ducan ducan) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO DUCAN(NAZIV, LOKACIJA_ID) VALUES (?, ?)");

        ps.setString(1, ducan.getNaziv());
        ps.setLong(2, ducan.getLokacija().getId());

        List<Radnik> radniciDucana = new ArrayList<>(ducan.getRadnici());
        List<Artikl> artikliDucana = new ArrayList<>(ducan.getArtikli());

        ps.executeUpdate();

        ducan.setId(getZadnjiDucanIndex());

        for(int i = 0; i < radniciDucana.size(); i++){
            dodajNoviDucanRadnikUBazu(ducan, radniciDucana.get(i).getId());
        }

        for(int i = 0; i < artikliDucana.size(); i++){
            dodajNoviDucanArtiklUBazu(ducan, artikliDucana.get(i).getId());
        }

        connection.close();

    }

    public static void dodajNoviDucanArtiklUBazu(Ducan ducan, Long artiklID) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO DUCAN_ARTIKL(DUCAN_ID, ARTIKL_ID) VALUES (?, ?)");

        ps.setLong(1, ducan.getId());
        ps.setLong(2, artiklID);

        ps.executeUpdate();

        connection.close();

    }

    public static void dodajNoviDucanRadnikUBazu(Ducan ducan, Long radnikID) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO DUCAN_RADNIK(DUCAN_ID, RADNIK_ID) VALUES (?, ?)");

        ps.setLong(1, ducan.getId());
        ps.setLong(2, radnikID);

        ps.executeUpdate();

        connection.close();

    }

    public static Long getZadnjiDucanIndex() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM DUCAN");

        Long ducanID = 0L;

        while (rs.next()){
            ducanID = rs.getLong("MAX(ID)");
        }

        return ducanID;

    }

    public static void updateDucanUBazu(Ducan ducan) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("UPDATE DUCAN SET NAZIV = ?, LOKACIJA_ID = ? WHERE ID = ?");
        ps.setString(1, ducan.getNaziv());
        ps.setLong(2, ducan.getLokacija().getId());
        ps.setLong(3, ducan.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteDucanIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM DUCAN WHERE ID = ?");
        ps.setLong(1, id);

        deleteDucanRadnikIzBaze(id);
        deleteDucanArtiklIzBaze(id);

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteDucanArtiklIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM DUCAN_ARTIKL WHERE DUCAN_ID = ?");
        ps.setLong(1, id);

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteDucanRadnikIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM DUCAN_RADNIK WHERE DUCAN_ID = ?");
        ps.setLong(1, id);

        ps.executeUpdate();

        connection.close();

    }

    public static List<Skladiste> dohvatiSkladisteIzBaze() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Skladiste> listaSkladista = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM SKLADISTE";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            Long lokacijaID = rs.getLong("LOKACIJA_ID");
            List<Radnik> listaRadnika = dohvatiRadnikaSkladistaPomocuIdIzBaze(id);
            List<Artikl> listaArtikala = dohvatiArtiklSkladistaPomocuIdIzBaze(id);

            Lokacija lokacija = dohvatiLokacijeIzBaze().stream()
                    .filter(lokacija1 -> lokacija1.getId().equals(lokacijaID)).collect(toSingleton());

            Skladiste skladiste = new Skladiste(id, naziv, lokacija, listaRadnika, listaArtikala);

            listaSkladista.add(skladiste);

        }

        return listaSkladista;

    }

    private static List<Artikl> dohvatiArtiklSkladistaPomocuIdIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Artikl> listaArtikala = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM SKLADISTE_ARTIKL";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()){

            Long skladisteID = rs.getLong("SKLADISTE_ID");
            Long artiklID = rs.getLong("ARTIKL_ID");

            if(skladisteID.equals(id)){
                Artikl artikl = dohvatiArtikleIzBaze().stream()
                        .filter(artikl1 -> artikl1.getId().equals(artiklID)).collect(toSingleton());
                listaArtikala.add(artikl);
            }

        }

        return listaArtikala;
    }

    private static List<Radnik> dohvatiRadnikaSkladistaPomocuIdIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Radnik> listaRadnika = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM SKLADISTE_RADNIK";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()){
            Long skladisteID = rs.getLong("SKLADISTE_ID");
            Long radnikID = rs.getLong("RADNIK_ID");

            if(skladisteID.equals(id)){
                Radnik radnik = dohvatiRadnikeIzBaze().stream()
                        .filter(radnik1 -> radnik1.getId().equals(radnikID)).collect(toSingleton());
                listaRadnika.add(radnik);
            }

        }

        return listaRadnika;

    }

    public static void dodajNovoSkladisteUBazu(Skladiste skladiste) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO SKLADISTE(NAZIV, LOKACIJA_ID) VALUES (?, ?)");

        ps.setString(1, skladiste.getNaziv());
        ps.setLong(2, skladiste.getLokacija().getId());

        List<Radnik> radniciSkladista = new ArrayList<>(skladiste.getRadnici());
        List<Artikl> artikliSkladista = new ArrayList<>(skladiste.getArtikli());

        ps.executeUpdate();

        skladiste.setId(getZadnjiSkladisteIndex());

        for(int i = 0; i < radniciSkladista.size(); i++){
            dodajNovoSkladisteRadnikUBazu(skladiste, radniciSkladista.get(i).getId());
        }

        for(int i = 0; i < artikliSkladista.size(); i++){
            dodajNovoSkladisteArtiklUBazu(skladiste, artikliSkladista.get(i).getId());
        }

        connection.close();

    }

    public static Long getZadnjiSkladisteIndex() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM SKLADISTE");

        Long skladisteID = 0L;

        while (rs.next()){
            skladisteID = rs.getLong("MAX(ID)");
        }

        return skladisteID;

    }

    public static void dodajNovoSkladisteArtiklUBazu(Skladiste skladiste, Long artiklID) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO SKLADISTE_ARTIKL(SKLADISTE_ID, ARTIKL_ID) VALUES (?, ?)");

        ps.setLong(1, skladiste.getId());
        ps.setLong(2, artiklID);

        ps.executeUpdate();

        connection.close();

    }

    public static void dodajNovoSkladisteRadnikUBazu(Skladiste skladiste, Long radnikID) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO SKLADISTE_RADNIK(SKLADISTE_ID, RADNIK_ID) VALUES (?, ?)");

        ps.setLong(1, skladiste.getId());
        ps.setLong(2, radnikID);

        ps.executeUpdate();

        connection.close();

    }

    public static void updateSkladisteUBazu(Skladiste skladiste) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("UPDATE SKLADISTE SET NAZIV = ?, LOKACIJA_ID = ? WHERE ID = ?");
        ps.setString(1, skladiste.getNaziv());
        ps.setLong(2, skladiste.getLokacija().getId());
        ps.setLong(3, skladiste.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteSkladisteIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM SKLADISTE WHERE ID = ?");
        ps.setLong(1, id);

        deleteSkladisteRadnikIzBaze(id);
        deleteSkladisteArtiklIzBaze(id);

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteSkladisteArtiklIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM SKLADISTE_ARTIKL WHERE SKLADISTE_ID = ?");
        ps.setLong(1, id);

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteSkladisteRadnikIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM SKLADISTE_RADNIK WHERE SKLADISTE_ID = ?");
        ps.setLong(1, id);

        ps.executeUpdate();

        connection.close();

    }

    public static List<Kupac> dohvatiKupcaIzBaze() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Kupac> listaKupaca = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM KUPAC";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Long id = rs.getLong("ID");
            String ime = rs.getString("IME");
            String prezime = rs.getString("PREZIME");
            Double potrosenoNovaca = rs.getDouble("POTROSENO_NOVACA");
            String datumIVrijeme = rs.getString("DATUM_IZDAVANJA");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime datumIVrijemeIzdavanja = LocalDateTime.parse(datumIVrijeme, dtf);

            List<Artikl> listaArtikala = dohvatiArtiklKupacPomocuIdIzBaze(id);

            Kupac kupac = new Kupac(id, ime, prezime, listaArtikala, potrosenoNovaca, datumIVrijemeIzdavanja);

            listaKupaca.add(kupac);

        }

        return listaKupaca;

    }

    private static List<Artikl> dohvatiArtiklKupacPomocuIdIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        List<Artikl> listaArtikla = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM ARTIKL_KUPAC";
        ResultSet rs = statement.executeQuery(query);

        while(rs.next()){
            Long kupacId = rs.getLong("KUPAC_ID");
            Long artiklId = rs.getLong("ARTIKL_ID");
            if(kupacId.equals(id)){
                Artikl artikl = dohvatiArtikleIzBaze().stream()
                        .filter(artikl1 -> artikl1.getId().equals(artiklId)).collect(toSingleton());
                listaArtikla.add(artikl);
            }

        }

        return listaArtikla;

    }

    public static void dodajNovogKupcaUBazu(Kupac kupac) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO KUPAC(IME, PREZIME, POTROSENO_NOVACA, DATUM_IZDAVANJA) VALUES  (?, ?, ?, ?)");
        ps.setString(1, kupac.getIme());
        ps.setString(2, kupac.getPrezime());
        ps.setDouble(3, kupac.getPotrosenoNovaca());
        ps.setString(4, String.valueOf(kupac.getDatumIzdavanja()));

        List<Artikl> artikli = new ArrayList<>(kupac.getKupljeniArtikli());

        ps.executeUpdate();

        kupac.setId(getZadnjiKupacIndex());

        for(int i = 0; i < artikli.size(); i++){
            dodajNoviArtiklKupacUBazu(kupac, artikli.get(i).getId());
        }

        connection.close();

    }

    public static Long getZadnjiKupacIndex() throws SQLException, IOException {

        Connection connection = connectToDatabase();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM KUPAC");

        Long kupacID = 0L;

        while (rs.next()){
            kupacID = rs.getLong("MAX(ID)");
        }

        return kupacID;

    }

    private static void dodajNoviArtiklKupacUBazu(Kupac kupac, Long artiklID) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO ARTIKL_KUPAC(KUPAC_ID, ARTIKL_ID) VALUES  (?, ?)");
        ps.setLong(1, kupac.getId());
        ps.setLong(2, artiklID);
        ps.executeUpdate();


    }

    public static void updateKupcaUBazu(Kupac kupac) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("UPDATE KUPAC SET IME = ?, PREZIME = ?, POTROSENO_NOVACA = ?, DATUM_IZDAVANJA = ? WHERE ID = ?");
        ps.setString(1, kupac.getIme());
        ps.setString(2, kupac.getPrezime());
        ps.setDouble(3, kupac.getPotrosenoNovaca());
        ps.setString(4, String.valueOf(kupac.getDatumIzdavanja()));
        ps.setLong(5, kupac.getId());

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteKupacIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM KUPAC WHERE ID = ?");
        ps.setLong(1, id);

        deleteKupacArtiklIzBaze(id);

        ps.executeUpdate();

        connection.close();

    }

    public static void deleteKupacArtiklIzBaze(Long id) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        PreparedStatement ps = connection.prepareStatement
                ("DELETE FROM ARTIKL_KUPAC WHERE KUPAC_ID = ?");
        ps.setLong(1, id);

        ps.executeUpdate();

        connection.close();

    }

}
