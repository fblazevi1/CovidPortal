package main.java.hr.java.covidportal.niti;

import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;

import java.io.IOException;
import java.sql.SQLException;

public class SpremiBolest implements Runnable{

    private Bolest bolest;

    public SpremiBolest(Bolest bolest) {
        this.bolest = bolest;
    }

    @Override
    public void run() {

        try {
            spremi(this.bolest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public synchronized void spremi(Bolest b) throws IOException, SQLException {
        while (BazaPodataka.aktivnaVezaSBazomPodataka == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;

        BazaPodataka.spremiNovuBolest(b);

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
    }
}
