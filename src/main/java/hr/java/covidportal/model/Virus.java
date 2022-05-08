package main.java.hr.java.covidportal.model;

import java.util.List;

/**
 * Predstavlja entitet virus koji je definiran nazivom, identifikatorom i listom simptoma.
 */


public class Virus extends Bolest implements Zarazno {

    /**
     * Inicijalizira podataka o nazivu i identifikatoru bolesti te njenim simptomima.
     *
     * @param naziv podatak o nazivu virusa
     * @param simptomi set objekata klase <code>Simptom</code> za virus
     */

    public Virus(String naziv, List<Simptom> simptomi, Boolean virus){
        super(naziv, simptomi, virus);
    }

    /**
     * Postavlja zarazu virusom svim osobama koji su bili u kontaktu s osobom koja je zaražena tim virusom.
     *
     * @param zarazenik osoba koje je bila u kontaktu sa zaraženom osobom
     */

    @Override
    public void prelazakZarazeNaOsobu(Osoba zarazenik) {
        zarazenik.setZarazenBolescu(this);
    }

}
