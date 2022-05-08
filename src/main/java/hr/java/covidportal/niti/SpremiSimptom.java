package main.java.hr.java.covidportal.niti;

import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.SQLException;

public class SpremiSimptom implements Runnable {

    private Simptom simptom;

    public SpremiSimptom(Simptom simptom) {
        this.simptom = simptom;
    }

    @Override
    public void run() {

        try {
            spremi(this.simptom);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public synchronized void spremi(Simptom s) throws IOException, SQLException {
        while (BazaPodataka.aktivnaVezaSBazomPodataka == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;

        BazaPodataka.spremiNoviSimptomUBazu(s);

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
    }
}
