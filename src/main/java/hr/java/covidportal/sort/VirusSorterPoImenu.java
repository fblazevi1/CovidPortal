package main.java.hr.java.covidportal.sort;

import main.java.hr.java.covidportal.model.Virus;

import java.util.Comparator;

/**
 * Uspoređuje i sortira viruse po imenu u obrnutom redosljedu od abecede.
 */

public class VirusSorterPoImenu implements Comparator<Virus> {

    @Override
    public int compare(Virus v1, Virus v2) {
        return v2.getNaziv().compareTo(v1.getNaziv());
    }
}

