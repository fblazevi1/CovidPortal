package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.SpremiZupaniju;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DodavanjeNoveZupanijeController implements Initializable {
    @FXML
    private TextField naziv;

    @FXML
    private TextField brojStanovnika;

    @FXML
    private TextField brojZarazenih;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void dodajNovuZupaniju(){
        try{
            String nazivZupanije = naziv.getText();
            Integer brojStanovnikaZupanije = Integer.parseInt(brojStanovnika.getText());
            Integer brojZarazenihZupanije = Integer.parseInt(brojZarazenih.getText());

            if(PretragaZupanijaController.getObservableListaZupanija() == null){
                PretragaZupanijaController.ucitajZupanije();
            }

            Zupanija zup = new Zupanija(nazivZupanije, brojStanovnikaZupanije, brojZarazenihZupanije);

            SpremiZupaniju nitZupanija = new SpremiZupaniju(zup);
            ExecutorService executorServiceZupanija = Executors.newCachedThreadPool();
            executorServiceZupanija.execute(nitZupanija);
            executorServiceZupanija.awaitTermination(1, TimeUnit.SECONDS);

            if (!(nazivZupanije.equals(""))) {
                PretragaZupanijaController.getZupanije().add(zup);
                PretragaZupanijaController.getObservableListaZupanija().add(zup);
            }

            if(PretragaZupanijaController.getZupanije().contains(zup)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje nove županije");
                alert.setHeaderText ("Uspješno dodavanje županije");
                alert.setContentText("Uspješno ste dodali novu županiju u bazu podataka!");
                alert.showAndWait();

                naziv.clear();
                brojStanovnika.clear();
                brojZarazenih.clear();
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove županije");
            alert.setHeaderText ("Neuspješno dodavanje županije");
            alert.setContentText("Dogodila se greška!\nNiste uspjeli unjeti novu županiju.\n" +
                    "Molimo vas, da ponovno provjerite podatke!");
            alert.showAndWait();
        }

    }
}

