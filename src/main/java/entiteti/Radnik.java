package entiteti;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Radnik extends Osoba implements IRadnik{

    private LocalDate datumRodjenja;
    private Lokacija lokacija;
    private Double placa;

    public Radnik(Long id, String ime, String prezime, LocalDate datumRodjenja, Lokacija lokacija, Double placa) {
        super(id, ime, prezime);
        this.datumRodjenja = datumRodjenja;
        this.lokacija = lokacija;
        this.placa = placa;
    }

    public Radnik() {

    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public Lokacija getLokacija() {
        return lokacija;
    }

    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }

    public Double getPlaca() {
        return placa;
    }

    public void setPlaca(Double placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return getIme() + " " + getPrezime();
    }

    @Override
    public List<Radnik> rodjendanRadnikaNaDanasnjiDan(List<Radnik> radnici) {

        List<Radnik> specijalniRadnici = new ArrayList<>();

        for (Radnik rad : radnici){
            if(rad.getDatumRodjenja().getDayOfMonth() == LocalDate.now().getDayOfMonth()
            && rad.getDatumRodjenja().getMonth() == LocalDate.now().getMonth()){
                specijalniRadnici.add(rad);
            }
        }

        return specijalniRadnici;
    }
}
