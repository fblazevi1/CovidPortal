package main.java.hr.java.covidportal.model;

import java.util.Objects;

/**
 * Predstavlja entitet simptoma koji je definiran nazivom, identifikatorom i vrijednošću.
 */

public class Simptom extends ImenovaniEntitet {
    private String vrijednost;

    /**
     * Inicijalizira podatak o nazivu, identifikatoru i vrijednosti simptoma.
     *
     * @param naziv podatak o nazivu simptoma
     * @param vrijednost podatak o vrijednosti (RIJETKO, SREDNJE ili ČESTO)
     */

    public Simptom(String naziv, String vrijednost) {
        super(naziv);
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Simptom)) return false;
        if (!super.equals(o)) return false;
        Simptom simptom = (Simptom) o;
        return getVrijednost().equals(simptom.getVrijednost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getVrijednost());
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
