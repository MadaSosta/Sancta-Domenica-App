package entiteti;

public class Lokacija {

    private Long id;
    private String ulica;
    private String grad;
    private String postanskiBroj;

    public Lokacija(Long id, String ulica, String grad, String postanskiBroj) {
        this.id = id;
        this.ulica = ulica;
        this.grad = grad;
        this.postanskiBroj = postanskiBroj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getPostanskiBroj() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(String postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }

    public static class LokacijaBuilder {

        private Long id;
        private String ulica;
        private String grad;
        private String postanskiBroj;

        public LokacijaBuilder Id(Long id) {
            this.id = id;
            return this;
        }

        public LokacijaBuilder Ulica(String ulica) {
            this.ulica = ulica;
            return this;
        }

        public LokacijaBuilder Grad(String grad) {
            this.grad = grad;
            return this;
        }

        public LokacijaBuilder PostanskiBroj(String postanskiBroj) {
            this.postanskiBroj = postanskiBroj;
            return this;
        }

        public Lokacija build() {
            Lokacija lokacija = new Lokacija(id, ulica, grad, postanskiBroj);
            return lokacija;
        }

    }

    @Override
    public String toString() {
        return ulica + ", " + grad + ", " + postanskiBroj;
    }
}
