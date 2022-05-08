package main.java.hr.java.covidportal.sort;

import main.java.hr.java.covidportal.model.Zupanija;

import java.util.Comparator;

/**
 * Uspoređuje postotke zaraze u različitim županijama i sortira županije silazno.
 */

public class CovidSorter implements Comparator<Zupanija> {

    @Override
    public int compare(Zupanija z1, Zupanija z2) {

        return z2.getPostotakZaraze().compareTo(z1.getPostotakZaraze());

    }
}
