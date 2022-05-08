package main.java.hr.java.covidportal.enume;

/**
 * Enumeracija vrijednosti simptoma (RIJETKO, SREDNJE ili ČESTO).
 */

public enum VrijednostSimptoma {

    RIJETKO( "RIJETKO"),
    SREDNJE( "SREDNJE"),
    CESTO( "ČESTO");

    private String opis;

    private VrijednostSimptoma(String opis){
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
