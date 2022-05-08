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
import main.java.hr.java.covidportal.model.Simptom;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaVirusaController implements Initializable {

    private static ObservableList<Bolest> observableListaVirusa;

    private static List<Bolest> virusi = new ArrayList<>();


    @FXML
    private TextField naziv;

    @FXML
    private TableView<Bolest> tablicaVirusa;

    @FXML
    private TableColumn<Bolest, String> stupacNazivVirusa;

    @FXML
    private TableColumn<List<Simptom>, String> stupacSimptomiVirusa;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tablicaVirusa.setPlaceholder(new Label("Nema rezultata pretraživanja."));

        stupacNazivVirusa.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));

        stupacSimptomiVirusa.setCellValueFactory(new PropertyValueFactory<List<Simptom>, String>("simptomi"));

        naziv.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent kE) {
                if(kE.getCode().equals(KeyCode.ENTER)){
                    pretragaVir();
                }
            }
        });

        ucitajViruse();

        tablicaVirusa.setItems(observableListaVirusa);
    }

    public static ObservableList<Bolest> getObservableListaVirusa() {
        return observableListaVirusa;
    }

    public static List<Bolest> getVirusi() {
        return virusi;
    }

    public void pretragaVir(){
        String nazivVirusa = naziv.getText();

        observableListaVirusa.clear();
        observableListaVirusa.addAll(FXCollections.observableArrayList(virusi));

        List<Bolest> filtriranaListaVirusa = observableListaVirusa.stream()
                .filter( bol -> bol.getNaziv().toLowerCase().contains(nazivVirusa.toLowerCase()))
                .collect(Collectors.toList());

        observableListaVirusa.clear();
        observableListaVirusa.addAll(FXCollections.observableArrayList(filtriranaListaVirusa));
    }

    public static void ucitajViruse(){
        if(observableListaVirusa == null) {

            PretragaSimptomaController.ucitajSimptome();

            PretragaBolestiController.ucitajBolesti();

            PretragaBolestiController.getBolesti().stream()
                    .filter(b -> b.getVirus().equals(Boolean.TRUE))
                    .forEach(b -> virusi.add(b));

            observableListaVirusa = FXCollections.observableArrayList(virusi);
        }
    }
}