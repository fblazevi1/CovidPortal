package main.java.hr.java.covidportal.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

/**
 * Predstavlja entitet osobe koji je definiran imenom i prezimenom, starosti, županijom, bolesti i listom osoba s
 * kojima je bio u kontaktu.
 */

public class Osoba {
    private String ime, prezime;
    private LocalDate datum_rodjenja;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe;

    /**
     * Implementira Builder pattern metode za klasu <code>Osoba</code>.
     */

    public static class Builder{

        private String ime, prezime;
        private LocalDate datum_rodjenja;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe;

        /**
         * Inicijalizira ime osobe.
         *
         * @param ime podatak o imenu osobe
         * @return objekt inicijaliziran sa <code>id</code> i <code>ime</code>
         */

        public Builder(String ime) {
            this.ime = ime;
        }


        /**
         * Inicijalizira prezime osobe.
         *
         * @param prezime podatak o prezimenu osobe
         * @return objekt inicijaliziran sa <code>id</code>, <code>ime</code> i <code>prezime</code>
         */

        public Builder saPrezimenom(String prezime){
            this.prezime = prezime;
            return this;
        }

        /**
         * Inicijalizira starosti osobe.
         *
         * @param datum_rodjenja podatak o starosti osobe
         * @return objekt klase <code>Osoba</code> inicijaliziran sa <code>id</code>, <code>ime</code>,
         * <code>prezime</code> i <code>starost</code>
         */

        public Builder saGodinama(LocalDate datum_rodjenja){
            this.datum_rodjenja = datum_rodjenja;
            return this;
        }

        /**
         * Inicijalizira županiju osobe.
         *
         * @param zupanija podatak o županiji osobe
         * @return objekt klase <code>Osoba</code> inicijaliziran sa <code>id</code>, <code>ime</code>,
         * <code>prezime</code>, <code>starost</code> i <code>zupanija</code>
         */

        public Builder uZupaniji(Zupanija zupanija){
            this.zupanija = zupanija;
            return this;
        }

        /**
         * Inicijalizira bolest ili virus osobe.
         *
         * @param zarazenBolescu podatak o bolesti osobe
         * @return objekt klase <code>Osoba</code> inicijaliziran sa <code>id</code>, <code>ime</code>,
         * <code>prezime</code>,<code>starost</code>, <code>zupanija</code> i <code>zarazenBolescu</code>.
         */

        public Builder saBolescu(Bolest zarazenBolescu){
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        /**
         * Inicijalizira polje osoba s kojima je osoba, čije podatke unosimo, bila u kontaktu.
         *
         * @param kontaktiraneOsobe polje koje sadrži osobe s kojima je osoba, čije podatke unosimo, bila u kontaktu
         * @return objekt klase <code>Osoba</code> inicijaliziran sa <code>id</code>, <code>ime</code>,
         * <code>prezime</code>, <code>starost</code>, <code>zupanija</code>, <code>zarazenBolescu</code> i
         * <code>kontaktiraneOsobe</code>.
         */

        public Builder saOsobama(List<Osoba> kontaktiraneOsobe){
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        /**
         * Kreira osobu.
         *
         * @return objekt klase <code>Osoba</code>
         */

        public Osoba build(){
            Osoba osoba = new Osoba();
            osoba.ime = this.ime;
            osoba.prezime = this.prezime;
            osoba.datum_rodjenja = this.datum_rodjenja;
            osoba.zupanija = this.zupanija;
            osoba.zarazenBolescu = this.zarazenBolescu;
            osoba.kontaktiraneOsobe = this.kontaktiraneOsobe;

            if(osoba.zarazenBolescu instanceof Virus virus){

                for(int i = 0; i < kontaktiraneOsobe.size(); i++){
                    if (osoba.kontaktiraneOsobe.get(i) == null){ break;}
                    virus.prelazakZarazeNaOsobu(kontaktiraneOsobe.get(i));
                }
            }
            return osoba;
        }
    }

    private Osoba(){}

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public LocalDate getDatum_rodjenja() {
        return datum_rodjenja;
    }

    public void setDatum_rodjenja(LocalDate datum_rodjenja) {
        this.datum_rodjenja = datum_rodjenja;
    }

    public Integer getStarost(){

        LocalDate datum = getDatum_rodjenja();
        LocalDate sada = LocalDate.now();
        Period period = Period.between(datum, sada);
        return period.getYears();
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Osoba)) return false;
        Osoba osoba = (Osoba) o;
        return  ime.equals(osoba.ime) &&
                prezime.equals(osoba.prezime) &&
                datum_rodjenja.equals(osoba.datum_rodjenja) &&
                zupanija.equals(osoba.zupanija) &&
                zarazenBolescu.equals(osoba.zarazenBolescu) &&
                Objects.equals(kontaktiraneOsobe, osoba.kontaktiraneOsobe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ime, prezime, datum_rodjenja, zupanija, zarazenBolescu, kontaktiraneOsobe);
    }

    @Override
    public String toString() {
        return getIme() +  " " + getPrezime();
    }
}
