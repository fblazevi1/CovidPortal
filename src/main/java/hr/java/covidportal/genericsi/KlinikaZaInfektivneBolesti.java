package main.java.hr.java.covidportal.genericsi;

import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Virus;

import java.util.List;

/**
 * Predstavlja klinike infektivnih bolesti koje su definirane zaraženim osobama i njihovim virusima.
 *
 * @param <T> generički parametar čija je domena klasa Virus ili njene podklase
 * @param <S> generički parametar čija je domena klasa Osoba ili njene podklase
 */

public class KlinikaZaInfektivneBolesti<T extends Virus, S extends Osoba> {

    private List<T> sviVirusi;
    private List<S> sveZarazeneOsobe;

    /**
     * Inicijalizira podatak o svim virusima i svim zaraženim osobama.
     *
     * @param sviVirusi lista objekata klase <code>Virus</code>, koji su zarazili osobe
     * @param sveZarazeneOsobe lista objekata klase <code>Osobe</code>, koje su zaražene
     */

    public KlinikaZaInfektivneBolesti(List<T> sviVirusi, List<S> sveZarazeneOsobe) {
        this.sviVirusi = sviVirusi;
        this.sveZarazeneOsobe = sveZarazeneOsobe;

    }

    /**
     * Dodaje virus u listu svih virusa.
     *
     * @param element objekt klase <code>Virus</code> koji se dodaje
     */

    public void dodajVirus(T element){
        sviVirusi.add(element);
    }

    /**
     * Dodaje osobu u listu svih zaraženih osoba.
     *
     * @param element objekt klase <code>Osoba</code> koji se dodaje
     */

    public void dodajOsobu(S element){
        sveZarazeneOsobe.add(element);
    }

    public List<T> getSviVirusi() {
        return sviVirusi;
    }

    public List<S> getSveZarazeneOsobe() {
        return sveZarazeneOsobe;
    }
}
