package main.java.hr.java.covidportal.niti;

import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NajviseZarazenihNit implements Runnable{
    @Override
    public void run() {
        List<Zupanija> listaZupanija = new ArrayList<>();
        try {
            listaZupanija = BazaPodataka.dohvatiSveZupanijeIzBaze();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        listaZupanija.stream()
                .max(Comparator.comparing(zupanija -> zupanija.getPostotakZaraze()))
                .ifPresent(zupanija -> System.out.println("Županija s najvećim postotkom zaraženih osoba je " +
                        zupanija.getNaziv() + " i to " + zupanija.getPostotakZaraze() + " %"));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
