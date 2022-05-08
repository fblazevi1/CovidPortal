package main.java.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.DohvatiSveOsobe;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PretragaOsobaController implements Initializable {

    private static ObservableList<Osoba> observableListaOsoba;

    static List<Osoba> osobe = new ArrayList<>();

    @FXML
    private TextField ime;

    @FXML
    private TextField prezime;

    @FXML
    private TableView<Osoba> tablicaOsoba;

    @FXML
    private TableColumn<Osoba, String> stupacImeOsobe;

    @FXML
    private TableColumn<Osoba, String> stupacPrezimeOsobe;

    @FXML
    private TableColumn<Osoba, Integer> stupacStarostOsobe;

    @FXML
    private TableColumn<Osoba, Zupanija> stupacZupanijaOsobe;

    @FXML
    private TableColumn<Osoba, Bolest> stupacBolestOsobe;

    @FXML
    private TableColumn<Osoba, List<Osoba>> stupacKontaktiOsobe;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tablicaOsoba.setPlaceholder(new Label("Nema rezultata pretraživanja."));

        stupacImeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));

        stupacPrezimeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));

        stupacStarostOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, Integer>("starost"));

        stupacZupanijaOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, Zupanija>("zupanija"));

        stupacBolestOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, Bolest>("zarazenBolescu"));

        stupacKontaktiOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, List<Osoba>>("kontaktiraneOsobe"));

        ime.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent kE) {
                if(kE.getCode().equals(KeyCode.ENTER)){
                    pretragaOso();
                }
            }
        });

        prezime.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent kE) {
                if(kE.getCode().equals(KeyCode.ENTER)){
                    pretragaOso();
                }
            }
        });

        citanjeOsobe();

        tablicaOsoba.setItems(observableListaOsoba);
    }

    public static ObservableList<Osoba> getObservableListaOsoba() {
        return observableListaOsoba;
    }

    public static List<Osoba> getOsobe() {
        return osobe;
    }

    public void pretragaOso(){
        String imeOsobe = ime.getText();
        String prezimeOsobe = prezime.getText();

        observableListaOsoba.clear();

        observableListaOsoba.addAll(FXCollections.observableArrayList(osobe));

        List<Osoba> filtriranaListaOsoba = observableListaOsoba.stream()
                .filter( oso -> oso.getIme().toLowerCase().contains(imeOsobe.toLowerCase()) &&
                        oso.getPrezime().toLowerCase().contains(prezimeOsobe.toLowerCase()))
                .collect(Collectors.toList());

        observableListaOsoba.clear();
        observableListaOsoba.addAll(FXCollections.observableArrayList(filtriranaListaOsoba));
    }

    public static void citanjeOsobe(){

        if(observableListaOsoba == null) {

            PretragaZupanijaController.ucitajZupanije();

            PretragaSimptomaController.ucitajSimptome();

            PretragaBolestiController.ucitajBolesti();

            DohvatiSveOsobe nitOsoba = new DohvatiSveOsobe();
            ExecutorService executorOsoba = Executors.newCachedThreadPool();
            Future<List<Osoba>> future = executorOsoba.submit(nitOsoba);

            try {
                osobe = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            observableListaOsoba = FXCollections.observableArrayList(osobe);
        }
    }
}