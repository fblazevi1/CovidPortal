package main.java.database;

import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Služi za komunikaciju sa bazom podataka.
 */

public class BazaPodataka {


    public static final String KONFIGURACIJA_BAZE_PODATAKA = "src\\main\\resources\\database.properties";
    public static Boolean aktivnaVezaSBazomPodataka = false;

    /**
     * Kreira objekt sučelja <code>Connection</code> koji predstavlja vezu s bazom podataka objekta i prima parametre
     * koji definiraju URL baze podataka korisničko ime i lozinku za pristupanje bazi.
     *
     * @return <code>Connection</code> objekt koji predstavlja vezu s bazom podataka.
     * @throws SQLException
     * @throws IOException
     */

    private static synchronized Connection connectToDatabase() throws SQLException, IOException {

        Properties svojstva = new Properties();

        svojstva.load(new FileReader(KONFIGURACIJA_BAZE_PODATAKA));

        String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
        String korisnickoIme = svojstva.getProperty("korisnickoIme");
        String lozinka = svojstva.getProperty("lozinka");

        Connection veza = DriverManager.getConnection(urlBazePodataka, korisnickoIme, lozinka);

        return veza;
    }

    /**
     * Prekida vezu aplikacije s bazom podataka.
     *
     * @param connection <code>Connection</code> objekt koji se koristi za komunikaciju aplikacije s bazom podataka
     * @throws SQLException
     */

    private static void closeConnectionToDatabase(Connection connection) throws SQLException{
        connection.close();
    }

    /**
     * Dohvaća sve simptome koji se nalaze u bazi podataka.
     *
     * @return listu objekata klase <code>Simptom</code> koji se nalaze u bazi podataka
     * @throws SQLException
     * @throws IOException
     */

    public static List<Simptom> dohvatiSveSimptomeIzBaze() throws SQLException, IOException{
        List<Simptom> listaSimptoma = new ArrayList<>();

        Connection veza = connectToDatabase();

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM");

        while(rs.next()) {
            String naziv = rs.getString("naziv");
            String vrijednost = rs.getString("vrijednost");

            Simptom noviSimptom = new Simptom(naziv, vrijednost);

            listaSimptoma.add(noviSimptom);
        }

        closeConnectionToDatabase(veza);

        return listaSimptoma;
    }


    /**
     * Dohvaća simptom iz baze prema identifikatoru u bazi.
     *
     * @param traziId identifikator tipa <code>Long</code> s kojim je, u bazi, povezan traženi simptom
     * @return objekt klase <code>Simptom</code> koji se dohvaća
     * @throws SQLException
     * @throws IOException
     */

    public static Simptom dohvatiSimptomIzBaze(Long traziId) throws SQLException, IOException{
        Simptom noviSimptom = null;

        Connection veza = connectToDatabase();

        PreparedStatement ps =
                veza.prepareStatement(
                        "SELECT * FROM SIMPTOM WHERE ID = ?", Statement.RETURN_GENERATED_KEYS);

        ps.setLong(1, traziId);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            String naziv = rs.getString("naziv");
            String vrijednost = rs.getString("vrijednost");

            noviSimptom = new Simptom(naziv, vrijednost);
        }
        closeConnectionToDatabase(veza);

        return noviSimptom;
    }

    /**
     * Dodaje novi simptom u bazu podataka.
     *
     * @param noviSimptom objekt klase <code>Simptom</code> koji se dodaje u bazu podataka.
     * @throws SQLException
     * @throws IOException
     */

    public static void spremiNoviSimptomUBazu(Simptom noviSimptom) throws SQLException, IOException{
        Connection veza = connectToDatabase();

        PreparedStatement upit =
                veza.prepareStatement(
                        "INSERT INTO SIMPTOM(naziv, vrijednost) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);

        upit.setString(1, noviSimptom.getNaziv());
        upit.setString(2, noviSimptom.getVrijednost());

        upit.executeUpdate();

        closeConnectionToDatabase(veza);
    }


    public static List<Bolest> dohvatiSveBolestiIzBaze() throws SQLException, IOException{
        List<Bolest> listaBolesti = new ArrayList<>();

        Connection veza = connectToDatabase();

        Statement stmt_1 = veza.createStatement();
        ResultSet rs_1 = stmt_1.executeQuery("SELECT * FROM BOLEST");


        while(rs_1.next()) {
            List<Simptom> listaSimptomaBolesti = new ArrayList<>();
            Long id = rs_1.getLong("id");
            String naziv = rs_1.getString("naziv");
            Boolean virus = rs_1.getBoolean("virus");

            PreparedStatement stmt_2 =
                    veza.prepareStatement(
                            "SELECT * FROM BOLEST_SIMPTOM WHERE BOLEST_ID = ?");

            stmt_2.setLong(1, id);

            ResultSet rs_2 = stmt_2.executeQuery();

            while(rs_2.next()){
                Long idSimptoma = rs_2.getLong("simptom_id");

                listaSimptomaBolesti.add(dohvatiSimptomIzBaze(idSimptoma));
            }

            Bolest novaBolest = new Bolest(naziv, listaSimptomaBolesti, virus);

            listaBolesti.add(novaBolest);
        }

        closeConnectionToDatabase(veza);

        return listaBolesti;
    }

    public static Bolest dohvatiBolestIzBaze(Long id) throws SQLException, IOException{
        List<Simptom> listaSimptomaBolesti = new ArrayList<>();
        Bolest novaBolest = null;

        Connection veza = connectToDatabase();

        PreparedStatement stmt_1 =
                veza.prepareStatement(
                        "SELECT * FROM BOLEST WHERE ID = ?", Statement.RETURN_GENERATED_KEYS);
        stmt_1.setString(1, id.toString());
        ResultSet rs_1 = stmt_1.executeQuery();

        PreparedStatement stmt_2 =
                veza.prepareStatement(
                        "SELECT * FROM BOLEST_SIMPTOM WHERE BOLEST_ID = ?", Statement.RETURN_GENERATED_KEYS);

        stmt_2.setLong(1, id);
        ResultSet rs_2 = stmt_2.executeQuery();

        if(rs_1.next()) {
            String naziv = rs_1.getString("naziv");
            Boolean virus = rs_1.getBoolean("virus");

            while (rs_2.next()) {

                Long idSimptoma = rs_2.getLong("simptom_id");
                Simptom simptomBolesti = dohvatiSimptomIzBaze(idSimptoma);

                listaSimptomaBolesti.add(simptomBolesti);
            }

            novaBolest = new Bolest(naziv, listaSimptomaBolesti, virus);
        }

        closeConnectionToDatabase(veza);

        return novaBolest;
    }

    public static void spremiNovuBolest(Bolest novaBolest) throws SQLException, IOException{
        Long idBolesti = -2L;
        Connection veza = connectToDatabase();

        PreparedStatement upit_1 =
                veza.prepareStatement(
                        "INSERT INTO BOLEST(naziv, virus) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);

        upit_1.setString(1, novaBolest.getNaziv());
        upit_1.setBoolean(2, novaBolest.getVirus());

        upit_1.executeUpdate();

        ResultSet res_1 = upit_1.getGeneratedKeys();
        if (res_1.next()){idBolesti = res_1.getLong(1);}

        for(Simptom s : novaBolest.getSimptomi()) {

            PreparedStatement upit_2 =
                    veza.prepareStatement("INSERT INTO BOLEST_SIMPTOM(bolest_id, simptom_id) VALUES(?, ?)"
                            , Statement.RETURN_GENERATED_KEYS);

            upit_2.setLong(1, idBolesti);
            upit_2.setLong(2, getSimptomId(s));

            upit_2.executeUpdate();
        }
        closeConnectionToDatabase(veza);
    }


    public static List<Zupanija> dohvatiSveZupanijeIzBaze() throws SQLException, IOException{
        List<Zupanija> listaZupanija = new ArrayList<>();

        Connection veza = connectToDatabase();

        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA");

        while(rs.next()) {
            String naziv = rs.getString("naziv");
            Integer broj_stanovnika = rs.getInt("broj_stanovnika");
            Integer broj_zarazenih_stanovnika = rs.getInt("broj_zarazenih_stanovnika");

            Zupanija novaZupanija = new Zupanija(naziv, broj_stanovnika, broj_zarazenih_stanovnika);

            listaZupanija.add(novaZupanija);
        }

        closeConnectionToDatabase(veza);

        return listaZupanija;
    }

    public static Zupanija dohvatiZupanijuIzBaze(Long traziId) throws SQLException, IOException{
        Zupanija novaZupanija = null;

        Connection veza = connectToDatabase();

        PreparedStatement ps =
                veza.prepareStatement(
                        "SELECT * FROM ZUPANIJA WHERE ID = ?", Statement.RETURN_GENERATED_KEYS);

        ps.setLong(1, traziId);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            String naziv = rs.getString("naziv");
            Integer broj_stanovnika = rs.getInt("broj_stanovnika");
            Integer broj_zarazenih_stanovnika = rs.getInt("broj_zarazenih_stanovnika");

            novaZupanija = new Zupanija(naziv, broj_stanovnika, broj_zarazenih_stanovnika);
        }
        closeConnectionToDatabase(veza);

        return novaZupanija;
    }

    public static void spremiNovuZupanijuUBazu(Zupanija novaZupanija) throws SQLException, IOException{
        Connection veza = connectToDatabase();

        PreparedStatement upit =
                veza.prepareStatement(
                        "INSERT INTO ZUPANIJA(naziv, broj_stanovnika, broj_zarazenih_stanovnika) VALUES(?, ?, ?)"
                        , Statement.RETURN_GENERATED_KEYS);

        upit.setString(1, novaZupanija.getNaziv());
        upit.setInt(2, novaZupanija.getBrojStanovnika());
        upit.setInt(3, novaZupanija.getBrojZarazenih());

        upit.executeUpdate();

        closeConnectionToDatabase(veza);
    }


    public static List<Osoba> dohvatiSveOsobeIzBaze() throws SQLException, IOException{
        List<Osoba> listaOsoba = new ArrayList<>();
        Osoba novaOsoba;

        Connection veza = connectToDatabase();

        Statement stmt_1 = veza.createStatement();
        ResultSet rs_1 = stmt_1.executeQuery("SELECT * FROM OSOBA");


        while(rs_1.next()) {
            List<Osoba> listaKontaktOsoba = new ArrayList<>();
            Long id = rs_1.getLong("id");
            String ime = rs_1.getString("ime");
            String prezime = rs_1.getString("prezime");

            Date date = (Date) rs_1.getDate("datum_rodjenja");
            Instant instant = Instant.ofEpochMilli(date.getTime());
            LocalDate datum_rodjenja = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

            Long idZupanijaOsobe = rs_1.getLong("zupanija_id");
            Long idBolestOsobe = rs_1.getLong("bolest_id");

            PreparedStatement stmt_2 =
                    veza.prepareStatement(
                            "SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID = ?");

            stmt_2.setLong(1, id);

            ResultSet rs_2 = stmt_2.executeQuery();

            while(rs_2.next()){
                Long idKontaktOsobe = rs_2.getLong("kontaktirana_osoba_id");

                listaKontaktOsoba.add(dohvatiOsobuIzBaze(idKontaktOsobe));
            }

            novaOsoba = new Osoba.Builder(ime)
                    .saPrezimenom(prezime)
                    .saGodinama(datum_rodjenja)
                    .uZupaniji(dohvatiZupanijuIzBaze(idZupanijaOsobe))
                    .saBolescu(dohvatiBolestIzBaze(idBolestOsobe))
                    .saOsobama(listaKontaktOsoba)
                    .build();

            listaOsoba.add(novaOsoba);
        }

        closeConnectionToDatabase(veza);

        return listaOsoba;
    }

    public static Osoba dohvatiOsobuIzBaze(Long id) throws SQLException, IOException{
        Osoba kotaktOsoba;
        List<Osoba> listaKontaktOsoba = new ArrayList<>();
        Osoba novaOsoba = null;

        Connection veza = connectToDatabase();

        PreparedStatement stmt_1 =
                veza.prepareStatement(
                        "SELECT * FROM OSOBA WHERE ID = ?", Statement.RETURN_GENERATED_KEYS);

        stmt_1.setLong(1, id);
        ResultSet rs_1 = stmt_1.executeQuery();

        PreparedStatement stmt_2 =
                veza.prepareStatement(
                        "SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID = ?", Statement.RETURN_GENERATED_KEYS);
        stmt_2.setLong(1, id);
        ResultSet rs_2 = stmt_2.executeQuery();

        if(rs_1.next()) {
            String ime = rs_1.getString("ime");
            String prezime = rs_1.getString("prezime");

            Date date = (Date) rs_1.getDate("datum_rodjenja");
            Instant instant = Instant.ofEpochMilli(date.getTime());
            LocalDate datum_rodjenja = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

            Long idZupanijaOsobe = rs_1.getLong("zupanija_id");
            Long idBolestOsobe = rs_1.getLong("bolest_id");

            while (rs_2.next()) {

                Long idKontaktOsobe = rs_2.getLong("kontaktirana_osoba_id");
                kotaktOsoba = dohvatiOsobuIzBaze(idKontaktOsobe);

                listaKontaktOsoba.add(kotaktOsoba);
            }

            novaOsoba = new Osoba.Builder(ime)
                    .saPrezimenom(prezime)
                    .saGodinama(datum_rodjenja)
                    .uZupaniji(dohvatiZupanijuIzBaze(idZupanijaOsobe))
                    .saBolescu(dohvatiBolestIzBaze(idBolestOsobe))
                    .saOsobama(listaKontaktOsoba)
                    .build();
        }

        closeConnectionToDatabase(veza);

        return novaOsoba;
    }


    public static void spremiNovuOsobuUBazu(Osoba novaOsoba) throws SQLException, IOException{
        Long idOsoba = -2L;
        Connection veza = connectToDatabase();

        PreparedStatement dodajOsobu =
                veza.prepareStatement(
                        "INSERT INTO OSOBA(ime, prezime, datum_rodjenja, zupanija_id, bolest_id) VALUES(?, ?, ?, ?, ?)"
                        , Statement.RETURN_GENERATED_KEYS);

        dodajOsobu.setString(1, novaOsoba.getIme());
        dodajOsobu.setString(2, novaOsoba.getPrezime());
        dodajOsobu.setDate(3, Date.valueOf(novaOsoba.getDatum_rodjenja()));
        dodajOsobu.setLong(4, getZupanijaId(novaOsoba.getZupanija()));
        dodajOsobu.setLong(5, getBolestId(novaOsoba.getZarazenBolescu()));

        dodajOsobu.executeUpdate();

        ResultSet res_3 = dodajOsobu.getGeneratedKeys();
        if (res_3.next()){idOsoba = res_3.getLong(1);}


        if(!novaOsoba.getKontaktiraneOsobe().isEmpty()) for(Osoba o : novaOsoba.getKontaktiraneOsobe()) {
            PreparedStatement upit_2 =
                    veza.prepareStatement(
                            "INSERT INTO KONTAKTIRANE_OSOBE(osoba_id, kontaktirana_osoba_id) VALUES(?, ?)"
                            , Statement.RETURN_GENERATED_KEYS);

            upit_2.setLong(1, idOsoba);
            upit_2.setLong(2, getOsobaId(o));

            upit_2.executeUpdate();
        }

        closeConnectionToDatabase(veza);
    }


    public static Long getSimptomId(Simptom s) throws SQLException, IOException{
        Long idSimptom = -2L;
        Connection veza = connectToDatabase();

        PreparedStatement upit_1 =
                veza.prepareStatement("SELECT ID FROM SIMPTOM WHERE NAZIV = ?", Statement.RETURN_GENERATED_KEYS);

        upit_1.setString(1, s.getNaziv());
        upit_1.executeQuery();

        ResultSet res = upit_1.getResultSet();
        if (res.next()){idSimptom = res.getLong(1);}

        closeConnectionToDatabase(veza);

        return idSimptom;
    }

    public static Long getZupanijaId(Zupanija z) throws SQLException, IOException{
        Long idZupanija = -2L;
        Connection veza = connectToDatabase();

        PreparedStatement upit_1 =
                veza.prepareStatement("SELECT ID FROM ZUPANIJA WHERE NAZIV = ?", Statement.RETURN_GENERATED_KEYS);

        upit_1.setString(1, z.getNaziv());
        upit_1.executeQuery();

        ResultSet res = upit_1.getResultSet();
        if (res.next()){idZupanija = res.getLong(1);}

        closeConnectionToDatabase(veza);

        return idZupanija;
    }

    public static Long getBolestId(Bolest b) throws SQLException, IOException{
        Long idBolest = -2L;
        Connection veza = connectToDatabase();

        PreparedStatement upit_1 =
                veza.prepareStatement("SELECT ID FROM BOLEST WHERE NAZIV = ?", Statement.RETURN_GENERATED_KEYS);

        upit_1.setString(1, b.getNaziv());
        upit_1.executeQuery();

        ResultSet res = upit_1.getResultSet();
        if (res.next()){idBolest = res.getLong(1);}

        closeConnectionToDatabase(veza);

        return idBolest;
    }

    public static Long getOsobaId(Osoba b) throws SQLException, IOException{
        Long idOsoba = -2L;
        Connection veza = connectToDatabase();

        PreparedStatement upit_1 =
                veza.prepareStatement("SELECT ID FROM OSOBA WHERE IME = ?", Statement.RETURN_GENERATED_KEYS);

        upit_1.setString(1, b.getIme());
        upit_1.executeQuery();

        ResultSet res = upit_1.getResultSet();
        if (res.next()){idOsoba = res.getLong(1);}

        closeConnectionToDatabase(veza);

        return idOsoba;
    }


    public static Zupanija getZupanijaMaxPostotakZarazenih() throws IOException, SQLException {
        Zupanija novaZupanija = null;
        Double max = -2D;
        Connection veza = connectToDatabase();

        PreparedStatement upit_1 =
                veza.prepareStatement("SELECT id, MAX(CAST (BROJ_ZARAZENIH_STANOVNIKA AS double ) / CAST (BROJ_STANOVNIKA AS double )) AS najveci FROM ZUPANIJA GROUP BY ID ORDER BY najveci DESC LIMIT 1"
                        , Statement.RETURN_GENERATED_KEYS);

        upit_1.executeQuery();

        ResultSet res = upit_1.getResultSet();

        if (res.next()){
            Long idZupanija = res.getLong(1);

            novaZupanija = dohvatiZupanijuIzBaze(idZupanija);
        }

        closeConnectionToDatabase(veza);

        return novaZupanija;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getZupanijaMaxPostotakZarazenih());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
