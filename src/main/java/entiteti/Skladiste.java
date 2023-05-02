package entiteti;

import java.util.List;

public class Skladiste  {

    private Long id;
    private String naziv;
    private Lokacija lokacija;
    private List<Radnik> radnici;
    private List<Artikl> artikli;

    public Skladiste(Long id, String naziv, Lokacija lokacija, List<Radnik> radnici, List<Artikl> artikli) {
        this.id = id;
        this.naziv = naziv;
        this.lokacija = lokacija;
        this.radnici = radnici;
        this.artikli = artikli;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Lokacija getLokacija() {
        return lokacija;
    }

    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }

    public List<Radnik> getRadnici() {
        return radnici;
    }

    public void setRadnici(List<Radnik> radnici) {
        this.radnici = radnici;
    }

    public List<Artikl> getArtikli() {
        return artikli;
    }

    public void setArtikli(List<Artikl> artikli) {
        this.artikli = artikli;
    }
}
