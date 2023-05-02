package niti;

import entiteti.BiljeskePromjena;
import javaFX.Application;

import java.util.List;

public class ZadnjaPromjena implements Runnable{
    @Override
    public void run() {

        List<BiljeskePromjena> listaPromjena = BiljeskePromjena.getBiljeskaList();

        int indeksZadnjePromjene = listaPromjena.size() - 1;

        String zadnjaPromjena = listaPromjena.get(indeksZadnjePromjene).getNovaVrijednost();

        Application.setMainStageTitle("Zadnja promjena: " + zadnjaPromjena);

    }
}
