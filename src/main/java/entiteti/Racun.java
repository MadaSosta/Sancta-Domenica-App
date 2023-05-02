package entiteti;

public class Racun<T, V> {

    private T izdavacRacuna;
    private V primateljRacuna;

    public Racun(T izdavacRacuna, V primateljRacuna) {
        this.izdavacRacuna = izdavacRacuna;
        this.primateljRacuna = primateljRacuna;
    }

    public T getIzdavacRacuna() {
        return izdavacRacuna;
    }

    public void setIzdavacRacuna(T izdavacRacuna) {
        this.izdavacRacuna = izdavacRacuna;
    }

    public V getPrimateljRacuna() {
        return primateljRacuna;
    }

    public void setPrimateljRacuna(V primateljRacuna) {
        this.primateljRacuna = primateljRacuna;
    }
}
