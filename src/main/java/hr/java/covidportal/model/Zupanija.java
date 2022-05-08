package main.java.hr.java.covidportal.model;

import java.util.Objects;

/**
 * Predstavlja entitet županije koji je definiran nazivom, identifikatorom i brojem stanovnika.
 */

public class Zupanija extends ImenovaniEntitet {
    private Integer brojStanovnika;
    private Integer brojZarazenih;

    /**
     * Inicijalizira podatak o nazivu, identifikatoru i broju stanovnika županije.
     *
     * @param naziv podatak o nazivu županije
     * @param brojStanovnika podatak o broju stanovnika
     * @param brojZarazenih podatak o broju zaraženih osoba u županiji
     */

    public Zupanija(String naziv, Integer brojStanovnika, Integer brojZarazenih) {
        super(naziv);
        this.brojStanovnika = brojStanovnika;
        this.brojZarazenih = brojZarazenih;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public Integer getBrojZarazenih() {
        return brojZarazenih;
    }

    public void setBrojZarazenih(Integer brojZarazenih) {
        this.brojZarazenih = brojZarazenih;
    }

    public Double getPostotakZaraze(){
        Double postotak = ((double) this.brojZarazenih/this.brojStanovnika) * 100;
        return postotak;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zupanija)) return false;
        if (!super.equals(o)) return false;
        Zupanija zupanija = (Zupanija) o;
        return getBrojStanovnika().equals(zupanija.getBrojStanovnika()) &&
                getBrojZarazenih().equals(zupanija.getBrojZarazenih());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBrojStanovnika(), getBrojZarazenih());
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
