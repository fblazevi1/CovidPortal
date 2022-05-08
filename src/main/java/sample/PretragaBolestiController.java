package main.java.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.DohvatiSveBolesti;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PretragaBolestiController implements Initializable {

    private static ObservableList<Bolest> observableListaBolesti;

    private static List<Bolest> bolesti = new ArrayList<>();

    @FXML
    private TextField naziv;

    @FXML
    private TableView<Bolest> tablicaBolesti;

    @FXML
    private TableColumn<Bolest, String> stupacNazivBolesti;

    @FXML
    private TableColumn<List<Simptom>, String> stupacSimptomiBolesti;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tablicaBolesti.setPlaceholder(new Label("Nema rezultata pretraživanja."));

        stupacNazivBolesti.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));

        stupacSimptomiBolesti.setCellValueFactory(new PropertyValueFactory<List<Simptom>, String>("simptomi"));

        naziv.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent kE) {
                if(kE.getCode().equals(KeyCode.ENTER)){
                    pretragaBol();
                }
            }
        });

        ucitajBolesti();

        tablicaBolesti.setItems(observableListaBolesti);

    }

    public void pretragaBol(){
        String nazivBolesti = naziv.getText();

        observableListaBolesti.clear();
        observableListaBolesti.addAll(FXCollections.observableArrayList(bolesti));

        List<Bolest> filtriranaListaBolesti = observableListaBolesti.stream()
                .filter( bol -> bol.getNaziv().toLowerCase().contains(nazivBolesti.toLowerCase()))
                .collect(Collectors.toList());

        observableListaBolesti.clear();
        observableListaBolesti.addAll(FXCollections.observableArrayList(filtriranaListaBolesti));
    }

    public static ObservableList<Bolest> getObservableListaBolesti() {
        return observableListaBolesti;
    }

    public static List<Bolest> getBolesti() {
        return bolesti;
    }

    public static void ucitajBolesti(){

        if(observableListaBolesti == null) {

            PretragaSimptomaController.ucitajSimptome();

            DohvatiSveBolesti nitBolest = new DohvatiSveBolesti();
            ExecutorService executorOsoba = Executors.newCachedThreadPool();
            Future<List<Bolest>> future = executorOsoba.submit(nitBolest);

            try {
                bolesti = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            observableListaBolesti = FXCollections.observableArrayList(bolesti);
        }
    }

}