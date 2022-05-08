package main.java.hr.java.covidportal.niti;

import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;

import java.io.IOException;
import java.sql.SQLException;

public class SpremiOsobu implements Runnable{

    private Osoba osoba;

    public SpremiOsobu(Osoba osoba) {
        this.osoba = osoba;
    }

    @Override
    public void run() {

        try {
            spremi(this.osoba);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public synchronized void spremi(Osoba o) throws IOException, SQLException {
        while (BazaPodataka.aktivnaVezaSBazomPodataka == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;

        BazaPodataka.spremiNovuOsobuUBazu(o);

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
    }
}
