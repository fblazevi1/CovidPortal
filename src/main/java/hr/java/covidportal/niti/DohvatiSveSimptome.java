package main.java.hr.java.covidportal.niti;

import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public class DohvatiSveSimptome implements Callable<List<Simptom>> {

    @Override
    public List<Simptom> call() throws Exception {

        List<Simptom> lista = dohvati();
        return lista;
    }

    public synchronized List<Simptom> dohvati() throws IOException, SQLException {
        while (BazaPodataka.aktivnaVezaSBazomPodataka == true) {
            try {
                System.out.println("Dohvat simptoma čeka!");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;

        System.out.println("Dohvat simptoma");
        List<Simptom> listaSimptoma = BazaPodataka.dohvatiSveSimptomeIzBaze();

        BazaPodataka.aktivnaVezaSBazomPodataka = false;

        return listaSimptoma;
    }
}
