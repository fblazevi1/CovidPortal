package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.SpremiSimptom;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DodavanjeNovogSimptomaController implements Initializable {

    @FXML
    private TextField naziv;

    @FXML
    private String vrijednost;

    @FXML
    private MenuButton odabirVrijednosti;

    @FXML
    private RadioMenuItem intenzivno;

    @FXML
    private RadioMenuItem produktivni;

    @FXML
    private RadioMenuItem visoka;

    @FXML
    private RadioMenuItem jaka;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    public void dodajNoviSimptom(){
        try {
            String nazivSimptoma = naziv.getText();

            if(intenzivno.isSelected()) vrijednost = intenzivno.getText().toUpperCase();
            else if (produktivni.isSelected()) vrijednost = produktivni.getText().toUpperCase();
            else if (visoka.isSelected()) vrijednost = visoka.getText().toUpperCase();
            else vrijednost = jaka.getText().toUpperCase();

            String vrijednostSimptoma = vrijednost;

            PretragaSimptomaController.ucitajSimptome();

            Simptom sim = new Simptom(nazivSimptoma, vrijednostSimptoma);

            SpremiSimptom nitSimptom = new SpremiSimptom(sim);
            ExecutorService executorServiceSimptom = Executors.newCachedThreadPool();
            executorServiceSimptom.execute(nitSimptom);
            executorServiceSimptom.awaitTermination(1, TimeUnit.SECONDS);

            if(!(nazivSimptoma.equals(""))) {
                PretragaSimptomaController.getSimptomi().add(sim);
                PretragaSimptomaController.getObservableListaSimptoma().add(sim);
            }

            if(PretragaSimptomaController.getSimptomi().contains(sim)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje novog simptoma");
                alert.setHeaderText ("Uspješno dodavanje simptoma");
                alert.setContentText("Uspješno ste dodali novi simptom u bazu podataka!");
                alert.showAndWait();

                naziv.clear();
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje novog simptoma");
            alert.setHeaderText ("Neuspješno dodavanje simptoma");
            alert.setContentText("Dogodila se greška!\nNiste uspjeli unjeti novi simptom.\n" +
                    "Molimo vas, da ponovno provjerite podatke!");
            alert.showAndWait();
        }
    }
}
