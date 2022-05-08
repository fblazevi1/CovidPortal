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
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.DohvatiSveSimptome;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PretragaSimptomaController implements Initializable {

    private static ObservableList<Simptom> observableListaSimptoma;

    private static List<Simptom> simptomi = new ArrayList<>();

    @FXML
    private TextField naziv;

    @FXML
    private TableView<Simptom> tablicaSimptoma;

    @FXML
    private TableColumn<Simptom, String> stupacNazivSimptoma;

    @FXML
    private TableColumn<Simptom, Integer> stupacVrijednostSimptoma;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tablicaSimptoma.setPlaceholder(new Label("Nema rezultata pretraživanja."));

        stupacNazivSimptoma.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));

        stupacVrijednostSimptoma.setCellValueFactory(new PropertyValueFactory<Simptom, Integer>("vrijednost"));

        naziv.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent kE) {
                if(kE.getCode().equals(KeyCode.ENTER)){
                    pretragaSim();
                }
            }
        });

        if(observableListaSimptoma == null){
            ucitajSimptome();
        }

        tablicaSimptoma.setItems(observableListaSimptoma);
    }

    public static ObservableList<Simptom> getObservableListaSimptoma() {
        return observableListaSimptoma;
    }

    public static List<Simptom> getSimptomi() {
        return simptomi;
    }

    public void pretragaSim(){
        String nazivSimptoma = naziv.getText();

        observableListaSimptoma.clear();

        observableListaSimptoma.addAll(FXCollections.observableArrayList(simptomi));

        List<Simptom> filtriranaListaSimptoma = observableListaSimptoma.stream()
                .filter( sim -> sim.getNaziv().toLowerCase().contains(nazivSimptoma.toLowerCase()))
                .collect(Collectors.toList());

        observableListaSimptoma.clear();
        observableListaSimptoma.addAll(FXCollections.observableArrayList(filtriranaListaSimptoma));
    }

    public static void ucitajSimptome(){
        if(PretragaSimptomaController.getObservableListaSimptoma() == null){

            DohvatiSveSimptome nitSimptom = new DohvatiSveSimptome();
            ExecutorService executorSimptom = Executors.newCachedThreadPool();
            Future<List<Simptom>> future = executorSimptom.submit(nitSimptom);

            try {
                simptomi = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            observableListaSimptoma = FXCollections.observableArrayList(simptomi);
        }
    }
}