package prijava;

public class Prijava {

    private String korisnickoIme;
    private String lozinka;

    private Long rola;

    public Prijava(String korisnickoIme, String lozinka, Long rola) {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.rola = rola;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public Long getRola() {
        return rola;
    }

    public void setRola(Long rola) {
        this.rola = rola;
    }
}
