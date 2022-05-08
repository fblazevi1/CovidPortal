package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.SpremiOsobu;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DodavanjeNoveOsobeController implements Initializable {

    @FXML
    private TextField ime;

    @FXML
    private TextField prezime;

    @FXML
    private DatePicker starost;

    @FXML
    private ChoiceBox izborZupanijeOsobe;

    @FXML
    private ChoiceBox izborBolestiOsobe;

    @FXML
    private MenuButton izboriKontakataOsobe;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PretragaOsobaController.citanjeOsobe();

        for (Zupanija z : PretragaZupanijaController.getZupanije()) {

            izborZupanijeOsobe.getItems().add(z);
        }

        izborZupanijeOsobe.setValue(PretragaZupanijaController.getZupanije().get(0));


        for (Bolest b : PretragaBolestiController.getBolesti()) {

            izborBolestiOsobe.getItems().add(b);
        }

        izborBolestiOsobe.setValue(PretragaBolestiController.getBolesti().get(0));


        for (Osoba o : PretragaOsobaController.getOsobe()) {

            CheckMenuItem osoba = new CheckMenuItem(o.getIme() + " " + o.getPrezime());

            izboriKontakataOsobe.getItems().add(osoba);
        }

    }

    public void dodajNovuOsobu() {
        try{
            String imeOsobe = ime.getText();
            String prezimeOsobe = prezime.getText();
            LocalDate datum_rodjenja = starost.getValue();

            Zupanija zupanijaOsobe = (Zupanija) izborZupanijeOsobe.getValue();
            Bolest bolestOsobe = (Bolest) izborBolestiOsobe.getValue();
            List<Osoba> listaKontaktOsoba = new ArrayList<>();


            for (MenuItem kontakt : izboriKontakataOsobe.getItems()){
                CheckMenuItem check = (CheckMenuItem) kontakt;
                if (check.isSelected()) {
                    Osoba kontaktOsoba = PretragaOsobaController.getOsobe().stream()
                            .filter( s -> check.getText().toLowerCase().contains(s.getIme().toLowerCase())).findFirst()
                            .get();
                    listaKontaktOsoba.add(kontaktOsoba);
                }
            }

            Osoba oso = new Osoba.Builder(imeOsobe)
                    .saPrezimenom(prezimeOsobe)
                    .saGodinama(datum_rodjenja)
                    .uZupaniji(zupanijaOsobe)
                    .saBolescu(bolestOsobe)
                    .saOsobama(listaKontaktOsoba)
                    .build();

            SpremiOsobu nitOsoba = new SpremiOsobu(oso);
            ExecutorService executorServiceOsoba = Executors.newCachedThreadPool();
            executorServiceOsoba.execute(nitOsoba);
            executorServiceOsoba.awaitTermination(1, TimeUnit.SECONDS);

            if (!(imeOsobe.equals("")) && !(prezimeOsobe.equals(""))) {
                PretragaOsobaController.getOsobe().add(oso);
                PretragaOsobaController.getObservableListaOsoba().add(oso);
            }

            if(PretragaOsobaController.getOsobe().contains(oso)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje nove osobe");
                alert.setHeaderText ("Uspješno dodavanje osobe");
                alert.setContentText("Uspješno ste dodali novu osobu u bazu podataka!");
                alert.showAndWait();

                ime.clear();
                prezime.clear();
                starost.getEditor().clear();
            }

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove osobe");
            alert.setHeaderText ("Neuspješno dodavanje osobe");
            alert.setContentText("Dogodila se greška!\nNiste uspjeli unjeti novu osobu.\n" +
                    "Molimo vas, da ponovno provjerite podatke!");
            alert.showAndWait();
        }
    }
}