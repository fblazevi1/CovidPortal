package main.java.hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Dodjeljuje naziv entitetima klasa.
 */

public abstract class ImenovaniEntitet implements Serializable {
    private String naziv;

    /**
     * Inicijalizira naziv i identifikator.
     *
     * @param naziv podatak o nazivu entiteta
     */

    public ImenovaniEntitet(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImenovaniEntitet)) return false;
        ImenovaniEntitet that = (ImenovaniEntitet) o;
        return naziv.equals(that.naziv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naziv);
    }
}
