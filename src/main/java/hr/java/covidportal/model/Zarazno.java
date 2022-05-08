package main.java.hr.java.covidportal.model;

/**
 * Predstavlja sučelje koje sadrži metodu <code>prelazakZarazeNaOsobu</code> za prelazak zaraze na osobe u kontaktu.
 */

public interface Zarazno {

    void prelazakZarazeNaOsobu(Osoba zarazenik);
}
