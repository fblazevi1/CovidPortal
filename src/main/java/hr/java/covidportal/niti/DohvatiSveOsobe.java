package main.java.hr.java.covidportal.niti;

import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public class DohvatiSveOsobe implements Callable<List<Osoba>> {

    @Override
    public List<Osoba> call() throws Exception {

        List<Osoba> lista = dohvati();
        return lista;
    }

    public synchronized List<Osoba> dohvati() throws IOException, SQLException {
        while (BazaPodataka.aktivnaVezaSBazomPodataka == true) {
            try {
                System.out.println("Dohvat osoba čeka!");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;

        System.out.println("Dohvat osoba");
        List<Osoba> listaOsoba = BazaPodataka.dohvatiSveOsobeIzBaze();

        BazaPodataka.aktivnaVezaSBazomPodataka = false;

        return listaOsoba;
    }
}
