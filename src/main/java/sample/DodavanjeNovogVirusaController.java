package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.Virus;
import main.java.hr.java.covidportal.niti.SpremiBolest;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DodavanjeNovogVirusaController implements Initializable {

    @FXML
    private TextField naziv;

    @FXML
    private MenuButton izborSimptomaVirusa;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PretragaSimptomaController.ucitajSimptome();

        for (Simptom s : PretragaSimptomaController.getSimptomi()) {
            CheckMenuItem simptom = new CheckMenuItem(s.getNaziv() + " - " + s.getVrijednost());

            izborSimptomaVirusa.getItems().add(simptom);
        }
    }

    public void dodajNoviVirus() {
        try{
            String nazivVirusa = naziv.getText();
            List<Simptom> simptomiVirusa = new ArrayList<>();

            PretragaBolestiController.ucitajBolesti();
            PretragaVirusaController.ucitajViruse();

            for (MenuItem i : izborSimptomaVirusa.getItems()){
                CheckMenuItem check = (CheckMenuItem) i;
                if (check.isSelected()) {
                    Simptom simptom = PretragaSimptomaController.getSimptomi().stream()
                            .filter( s -> check.getText().toLowerCase().contains(s.getNaziv().toLowerCase()))
                            .findAny().get();

                    simptomiVirusa.add(simptom);
                }
            }

            Virus vir = new Virus(nazivVirusa, simptomiVirusa, Boolean.TRUE);

            SpremiBolest nit = new SpremiBolest(vir);
            ExecutorService executorServiceVirus = Executors.newCachedThreadPool();
            executorServiceVirus.execute(nit);
            executorServiceVirus.awaitTermination(1, TimeUnit.SECONDS);

            if (!(nazivVirusa.equals(""))) {
                PretragaBolestiController.getBolesti().add(vir);
                PretragaVirusaController.getVirusi().add(vir);
                PretragaBolestiController.getObservableListaBolesti().add(vir);
                PretragaVirusaController.getObservableListaVirusa().add(vir);
            }

            if(PretragaBolestiController.getBolesti().contains(vir)
                    && PretragaVirusaController.getVirusi().contains(vir)){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje novog virusa");
                alert.setHeaderText ("Uspješno dodavanje virusa");
                alert.setContentText("Uspješno ste dodali novi virus u bazu podataka!");
                alert.showAndWait();

                naziv.clear();
            }

        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje novog virusa");
            alert.setHeaderText ("Neuspješno dodavanje virusa");
            alert.setContentText("Dogodila se greška!\nNiste uspjeli unjeti novi virus.\n" +
                    "Molimo vas, da ponovno provjerite podatke!");
            alert.showAndWait();
        }
    }
}