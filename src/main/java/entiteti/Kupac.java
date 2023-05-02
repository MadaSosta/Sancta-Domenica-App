package entiteti;

import java.time.LocalDateTime;
import java.util.List;

public class Kupac extends Osoba {

    private List<Artikl> kupljeniArtikli;
    private Double potrosenoNovaca;
    private LocalDateTime datumIzdavanja;

    public Kupac(Long id, String ime, String prezime, List<Artikl> kupljeniArtikli, Double potrosenoNovaca,
                 LocalDateTime datumIzdavanja) {
        super(id, ime, prezime);
        this.kupljeniArtikli = kupljeniArtikli;
        this.potrosenoNovaca = potrosenoNovaca;
        this.datumIzdavanja = datumIzdavanja;
    }

    public List<Artikl> getKupljeniArtikli() {
        return kupljeniArtikli;
    }

    public void setKupljeniArtikli(List<Artikl> kupljeniArtikli) {
        this.kupljeniArtikli = kupljeniArtikli;
    }

    public Double getPotrosenoNovaca() {
        return potrosenoNovaca;
    }

    public void setPotrosenoNovaca(Double potrosenoNovaca) {
        this.potrosenoNovaca = potrosenoNovaca;
    }

    public LocalDateTime getDatumIzdavanja() {
        return datumIzdavanja;
    }

    public void setDatumIzdavanja(LocalDateTime datumIzdavanja) {
        this.datumIzdavanja = datumIzdavanja;
    }
}
