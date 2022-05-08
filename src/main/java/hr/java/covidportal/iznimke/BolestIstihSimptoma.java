package main.java.hr.java.covidportal.iznimke;

/**
 * Sprječava unos bolesti sa istim simptomima.
 */

public class BolestIstihSimptoma extends RuntimeException{

    /**
     * Inicijalizacija poruke zbog bacanja iznimke.
     *
     * @param poruka poruka koja pojašnjava zašto je iznimka bačena
     */

    public BolestIstihSimptoma(String poruka) {
        super(poruka);
    }

    /**
     * Inicijalizacija uzroka bacanja iznimke.
     *
     * @param uzrok uzrok bacanja iznimke
     */

    public BolestIstihSimptoma(Throwable uzrok) {
        super(uzrok);
    }

    /**
     * Inicijalizacija uzroka i poruke kod bacanja iznimke.
     *
     * @param poruka poruka koja pojašnjava zašto je iznimka bačena
     * @param uzrok uzrok bacanja iznimke
     */

    public BolestIstihSimptoma(String poruka, Throwable uzrok) {
        super(poruka, uzrok);
    }
}
