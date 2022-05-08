package main.java.hr.java.covidportal.iznimke;

/**
 * Ograničava da broj kontakt osoba bude veći od broja osoba.
 */

public class OgranicenjeBrojaKontaktOsoba extends Exception{

    /**
     * Inicijalizacija poruke zbog bacanja iznimke.
     *
     * @param poruka poruka koja pojašnjava zašto je iznimka bačena
     */

    public OgranicenjeBrojaKontaktOsoba(String poruka){
        super(poruka);
    }

    /**
     * Inicijalizacija uzroka bacanja iznimke.
     *
     * @param uzrok uzrok bacanja iznimke
     */

    public OgranicenjeBrojaKontaktOsoba(Throwable uzrok){
        super(uzrok);
    }

    /**
     * Inicijalizacija uzroka i poruke kod bacanja iznimke.
     *
     * @param poruka poruka koja pojašnjava zašto je iznimka bačena
     * @param uzrok uzrok bacanja iznimke
     */

    public OgranicenjeBrojaKontaktOsoba(String poruka, Throwable uzrok){
        super(poruka, uzrok);
    }

}
