package main.java.hr.java.covidportal.model;

import java.util.List;
import java.util.Objects;

/**
 * Predstavlja entitet bolesti koji je definiran nazivom,identifikatorom i simptomima.
 */

public class Bolest extends ImenovaniEntitet {
    private List<Simptom> simptomi;
    private Boolean virus;

    /**
     * Inicijalizacija podataka o nazivu i identifikatoru bolesti te njenim simptomima.
     * @param simptomi lista objekata klase <code>Simptom</code> za bolest
     */

    public Bolest(String naziv, List<Simptom> simptomi, Boolean virus) {
        super(naziv);
        this.simptomi = simptomi;
        this.virus = virus;
    }

    public Boolean getVirus() {
        return virus;
    }

    public void setVirus(Boolean virus) {
        this.virus = virus;
    }

    public List<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(List<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bolest)) return false;
        if (!super.equals(o)) return false;
        Bolest bolest = (Bolest) o;
        return getSimptomi().equals(bolest.getSimptomi());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSimptomi());
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
