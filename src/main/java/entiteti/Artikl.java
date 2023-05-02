package entiteti;

public class Artikl implements IArtikl {

    private Long id;
    private String naziv;
    private Kategorija kategorija;
    private Double sirina;
    private Double visina;
    private Double duljina;
    private Double masa;
    private Double cijenaProizvodnje;
    private Double cijenaProdaje;
    private Integer garancija;

    public Artikl(Long id, String naziv, Kategorija kategorija, Double sirina, Double visina, Double duljina,
                  Double masa, Double cijenaProizvodnje, Double cijenaProdaje, Integer garancija) {
        this.id = id;
        this.naziv = naziv;
        this.kategorija = kategorija;
        this.sirina = sirina;
        this.visina = visina;
        this.duljina = duljina;
        this.masa = masa;
        this.cijenaProizvodnje = cijenaProizvodnje;
        this.cijenaProdaje = cijenaProdaje;
        this.garancija = garancija;
    }

    public Artikl() {

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

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    public Double getSirina() {
        return sirina;
    }

    public void setSirina(Double sirina) {
        this.sirina = sirina;
    }

    public Double getVisina() {
        return visina;
    }

    public void setVisina(Double visina) {
        this.visina = visina;
    }

    public Double getDuljina() {
        return duljina;
    }

    public void setDuljina(Double duljina) {
        this.duljina = duljina;
    }

    public Double getMasa() {
        return masa;
    }

    public void setMasa(Double masa) {
        this.masa = masa;
    }

    public Double getCijenaProizvodnje() {
        return cijenaProizvodnje;
    }

    public void setCijenaProizvodnje(Double cijenaProizvodnje) {
        this.cijenaProizvodnje = cijenaProizvodnje;
    }

    public Double getCijenaProdaje() {
        return cijenaProdaje;
    }

    public void setCijenaProdaje(Double cijenaProdaje) {
        this.cijenaProdaje = cijenaProdaje;
    }

    public Integer getGarancija() {
        return garancija;
    }

    public void setGarancija(Integer garancija) {
        this.garancija = garancija;
    }

    @Override
    public String toString() {
        return naziv;
    }

    @Override
    public Double izracunProfita(Double cijenaIzrade, Double cijenaProdaje) {
        return cijenaProdaje - cijenaIzrade;
    }
}
