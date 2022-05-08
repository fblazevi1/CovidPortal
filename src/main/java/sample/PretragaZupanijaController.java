package main.java.sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import main.java.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.DohvatiSveZupanije;
import main.java.hr.java.covidportal.niti.NajviseZarazenihNit;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PretragaZupanijaController implements Initializable {

    private static ObservableList<Zupanija> observableListaZupanija;
    private static List<Zupanija> zupanije = new ArrayList<>();

    @FXML
    private TextField naziv;

    @FXML
    private TableView<Zupanija> tablicaZupanija;

    @FXML
    private TableColumn<Zupanija, String> stupacNazivZupanije;

    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojStanovnikaZupanije;

    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojZarazenihZupanije;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tablicaZupanija.setPlaceholder(new Label("Nema rezultata pretraživanja."));

        stupacNazivZupanije.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));

        stupacBrojStanovnikaZupanije.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));

        stupacBrojZarazenihZupanije.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenih"));

        naziv.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent kE) {
                if (kE.getCode().equals(KeyCode.ENTER)) {
                    pretragaZup();
                }
            }
        });

        ucitajZupanije();

        tablicaZupanija.setItems(observableListaZupanija);

        NajviseZarazenihNit nit = new NajviseZarazenihNit();
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(nit);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {

                    try {
                        Main.getMainStage().setTitle(BazaPodataka.getZupanijaMaxPostotakZarazenih().getNaziv());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                })
        );
        timeline.play();

    }

    public static ObservableList<Zupanija> getObservableListaZupanija() {
        return observableListaZupanija;
    }

    public static List<Zupanija> getZupanije() {
        return zupanije;
    }

    public void pretragaZup() {
        String nazivZupanije = naziv.getText();

        observableListaZupanija.clear();

        observableListaZupanija.addAll(FXCollections.observableArrayList(zupanije));

        List<Zupanija> filtriranaListaZupanija = observableListaZupanija.stream()
                .filter(zup -> zup.getNaziv().toLowerCase().contains(nazivZupanije.toLowerCase()))
                .collect(Collectors.toList());

        observableListaZupanija.clear();
        observableListaZupanija.addAll(FXCollections.observableArrayList(filtriranaListaZupanija));
    }

    public static void ucitajZupanije(){

        if(observableListaZupanija == null) {

            DohvatiSveZupanije nitZupanija = new DohvatiSveZupanije();
            ExecutorService executorZupanija = Executors.newCachedThreadPool();
            Future<List<Zupanija>> future = executorZupanija.submit(nitZupanija);

            try {
                zupanije = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            observableListaZupanija = FXCollections.observableArrayList(zupanije);
        }
    }
}