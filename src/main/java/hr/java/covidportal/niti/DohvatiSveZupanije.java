package main.java.hr.java.covidportal.niti;

import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public class DohvatiSveZupanije implements Callable<List<Zupanija>> {

    @Override
    public List<Zupanija> call() throws Exception {

        List<Zupanija> lista = dohvati();
        return lista;
    }

    public synchronized List<Zupanija> dohvati() throws IOException, SQLException {
        while (BazaPodataka.aktivnaVezaSBazomPodataka == true) {
            try {
                System.out.println("Dohvat županija čeka!");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;

        System.out.println("Dohvat županija");
        List<Zupanija> listZupanija = BazaPodataka.dohvatiSveZupanijeIzBaze();

        BazaPodataka.aktivnaVezaSBazomPodataka = false;

        return listZupanija;
    }
}
