package entiteti;

import java.util.List;

public class Tvrtka {

    private Long id;
    private String naziv;
    private List<Ducan> ducani;
    private List<Skladiste> skladista;

    public Tvrtka(Long id, String naziv, List<Ducan> ducani, List<Skladiste> skladista) {
        this.id = id;
        this.naziv = naziv;
        this.ducani = ducani;
        this.skladista = skladista;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ducan> getDucani() {
        return ducani;
    }

    public void setDucani(List<Ducan> ducani) {
        this.ducani = ducani;
    }

    public List<Skladiste> getSkladista() {
        return skladista;
    }

    public void setSkladista(List<Skladiste> skladista) {
        this.skladista = skladista;
    }
}
