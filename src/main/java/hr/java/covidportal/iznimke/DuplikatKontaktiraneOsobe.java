package main.java.hr.java.covidportal.iznimke;

/**
 * Sprječava unos istih kontakt osoba za osobu, čije podatke unosimo.
 */

public class DuplikatKontaktiraneOsobe extends Exception{

    /**
     * Inicijalizacija poruke zbog bacanja iznimke.
     *
     * @param poruka poruka koja pojašnjava zašto je iznimka bačena
     */

    public DuplikatKontaktiraneOsobe(String poruka){
        super(poruka);
    }

    /**
     * Inicijalizacija uzroka bacanja iznimke.
     *
     * @param uzrok uzrok bacanja iznimke
     */

    public DuplikatKontaktiraneOsobe(Throwable uzrok){
        super(uzrok);
    }

    /**
     * Inicijalizacija uzroka i poruke kod bacanja iznimke.
     *
     * @param poruka poruka koja pojašnjava zašto je iznimka bačena
     * @param uzrok uzrok bacanja iznimke
     */

    public DuplikatKontaktiraneOsobe(String poruka, Throwable uzrok){
        super(poruka, uzrok);
    }

}
