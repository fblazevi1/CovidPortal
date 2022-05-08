package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.SpremiBolest;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DodavanjeNoveBolestiController implements Initializable {

    @FXML
    private TextField naziv;

    @FXML
    private MenuButton izborSimptomaBolesti;

    @FXML
    private RadioButton virusDa;

    @FXML
    private RadioButton virusNe;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PretragaSimptomaController.ucitajSimptome();

        for (Simptom s : PretragaSimptomaController.getSimptomi()) {
            CheckMenuItem simptom = new CheckMenuItem(s.getNaziv() + " - " + s.getVrijednost());

            izborSimptomaBolesti.getItems().add(simptom);
        }
    }

    public void dodajNovuBolest() {

        try{
            String nazivBolesti = naziv.getText();
            List<Simptom> simptomiBolesti = new ArrayList<>();
            Boolean virus;

            PretragaBolestiController.ucitajBolesti();

            if(virusDa.isSelected()) virus = Boolean.TRUE;
            else virus = Boolean.FALSE;

            for (MenuItem i : izborSimptomaBolesti.getItems()){
                CheckMenuItem check = (CheckMenuItem) i;
                if (check.isSelected()) {
                    Simptom simptom = PretragaSimptomaController.getSimptomi().stream()
                            .filter( s -> check.getText().toLowerCase().contains(s.getNaziv().toLowerCase()))
                            .findAny().get();

                    simptomiBolesti.add(simptom);
                }
            }

            Bolest bol = new Bolest(nazivBolesti, simptomiBolesti, virus);

            SpremiBolest nit = new SpremiBolest(bol);
            ExecutorService executorServiceBolest = Executors.newCachedThreadPool();
            executorServiceBolest.execute(nit);
            executorServiceBolest.awaitTermination(1, TimeUnit.SECONDS);

            if (!(nazivBolesti.equals(""))) {
                PretragaBolestiController.getBolesti().add(bol);
                PretragaBolestiController.getObservableListaBolesti().add(bol);
            }

            if(PretragaBolestiController.getBolesti().contains(bol)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje nove bolesti");
                alert.setHeaderText ("Uspješno dodavanje bolesti");
                alert.setContentText("Uspješno ste dodali novu bolest u bazu podataka!");
                alert.showAndWait();

                naziv.clear();
            }

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove bolesti");
            alert.setHeaderText ("Neuspješno dodavanje bolesti");
            alert.setContentText("Dogodila se greška!\nNiste uspjeli unjeti novu bolest.\n" +
                    "Molimo vas, da ponovno provjerite podatke!");
            alert.showAndWait();
        }
    }
}