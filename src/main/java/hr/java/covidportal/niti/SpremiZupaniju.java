package main.java.hr.java.covidportal.niti;

import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.SQLException;

public class SpremiZupaniju implements Runnable{

    private Zupanija zupanija;

    public SpremiZupaniju(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    @Override
    public void run() {

        try {
            spremi(this.zupanija);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public synchronized void spremi(Zupanija z) throws IOException, SQLException {
        while (BazaPodataka.aktivnaVezaSBazomPodataka == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;

        BazaPodataka.spremiNovuZupanijuUBazu(z);

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
    }
}
