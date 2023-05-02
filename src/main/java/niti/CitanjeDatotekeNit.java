package niti;

import entiteti.BiljeskePromjena;

import java.util.List;

public class CitanjeDatotekeNit implements Runnable{

    public static final int MAX_VRIJEME_CEKANJA = 10000;
    List<BiljeskePromjena> listaPromjena = BiljeskePromjena.getBiljeskaList();

    @Override
    public void run() {

        while (true){

            int indeksZadnjePromjene = listaPromjena.size() - 1;

            BiljeskePromjena zadnjaPromjena = listaPromjena.get(indeksZadnjePromjene);

            if(listaPromjena.isEmpty()){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            notifyAll();

        }


    }
}
