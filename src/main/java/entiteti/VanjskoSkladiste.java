package entiteti;

public class VanjskoSkladiste<T> {

    private T vanjskoSkladiste;

    public VanjskoSkladiste(T brojArtikala) {
        this.vanjskoSkladiste = brojArtikala;
    }

    public T getVanjskoSkladiste() {
        return vanjskoSkladiste;
    }

    public void setVanjskoSkladiste(T vanjskoSkladiste) {
        this.vanjskoSkladiste = vanjskoSkladiste;
    }
}
