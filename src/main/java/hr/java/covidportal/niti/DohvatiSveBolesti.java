package main.java.hr.java.covidportal.niti;

import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public class DohvatiSveBolesti implements Callable<List<Bolest>> {
    @Override
    public List<Bolest> call() throws Exception {

        List<Bolest> lista = dohvati();
        return lista;
    }

    public synchronized List<Bolest> dohvati() throws IOException, SQLException {
        while (BazaPodataka.aktivnaVezaSBazomPodataka == true) {
            try {
                System.out.println("Dohvat bolesti čeka!");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;

        System.out.println("Dohvat bolesti!");
        List<Bolest> listaBolesti = BazaPodataka.dohvatiSveBolestiIzBaze();

        BazaPodataka.aktivnaVezaSBazomPodataka = false;

        return listaBolesti;
    }
}
