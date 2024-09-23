package toimipiste;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import main.Main;

import java.net.URL;
import java.util.*;

public class ToimipisteController implements Initializable {

    @FXML
    private TableColumn<Toimipiste, String> tcToimipiste;

    @FXML
    private TableColumn<Toimipiste, String> tcOsoite;

    @FXML
    private TableColumn<Toimipiste, Integer> tcId;

    @FXML
    private TableColumn<Toimipiste, Integer> tcKapasiteetti;

    @FXML
    private TableColumn<Toimipiste, Integer> tcPostinumero;

    @FXML
    private TableColumn<Toimipiste, String> tcPostitoimipaikka;

    @FXML
    private TableColumn<Toimipiste, Integer> tcVuorokausihinta;

    @FXML
    private TableView<Toimipiste> tvTaulu;

    @FXML
    private TextField tfToimipiste;

    @FXML
    private TextField tfOsoite;

    @FXML
    private TextField tfHaku;

    @FXML
    private TextField tfKapasiteetti;

    @FXML
    private TextField tfPostinumero;

    @FXML
    private TextField tfPostitoimipaikka;

    @FXML
    private TextField tfVuorokausihinta;

    @FXML
    private Text txVirhe;

    @FXML
    private Button btTallenna;

    @FXML
    private Button btPoista;

    @FXML
    private Button btMuokkaa;

    @FXML
    private Button btPeruuta;

    @FXML
    private ListView<String> lvPalvelut;
    private ObservableList<Toimipiste> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcToimipiste.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        tcOsoite.setCellValueFactory(new PropertyValueFactory<>("lahiosoite"));
        tcId.setCellValueFactory(new PropertyValueFactory<>("toimipisteID"));
        tcPostinumero.setCellValueFactory(new PropertyValueFactory<>("postinumero"));
        tcPostitoimipaikka.setCellValueFactory(new PropertyValueFactory<>("postitoimipaikka"));
        tcKapasiteetti.setCellValueFactory(new PropertyValueFactory<>("kapasiteetti"));
        tcVuorokausihinta.setCellValueFactory(new PropertyValueFactory<>("vrkhinta"));

        tvTaulu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<String> palvelut = ToimipisteTietokanta.haeToimipisteenPalvelut(newValue.getToimipisteID());
                lvPalvelut.setItems(palvelut);

                tfToimipiste.setText(newValue.getNimi());
                tfOsoite.setText(newValue.getLahiosoite());
                tfPostinumero.setText(String.valueOf(newValue.getPostinumero()));
                tfPostitoimipaikka.setText(newValue.getPostitoimipaikka());
                tfKapasiteetti.setText(String.valueOf(newValue.getKapasiteetti()));
                tfVuorokausihinta.setText(String.valueOf(newValue.getVrkhinta()));
                this.asetaVirheteksti("");
            }
        });
        this.asetaVirheteksti("");

        data = ToimipisteTietokanta.haeKaikkiToimipisteet();
        tvTaulu.setItems(data);

        tfHaku.textProperty().addListener((observable, oldValue, newValue) -> {
            String lowercaseQuery = newValue.toLowerCase();
            tvTaulu.setItems(
                    data.filtered(p -> {
                        String nimi = p.getNimi().toLowerCase();
                        int id = p.getToimipisteID();
                        return nimi.contains(lowercaseQuery) || String.valueOf(id).contains(lowercaseQuery);
                    })
            );
        });

        btTallenna.setOnAction(event -> {
            this.asetaVirheteksti("Täytä kaikki kentät!");
            // Lisää uusi toimipiste tietokantaan
            String nimi = tfToimipiste.getText();
            String osoite = tfOsoite.getText();
            String postinumeroStr = tfPostinumero.getText();
            String postitoimipaikka = tfPostitoimipaikka.getText();
            String kapasiteettiStr = tfKapasiteetti.getText();
            String vuorokausihintaStr = tfVuorokausihinta.getText();

            if (!nimi.isEmpty() && !osoite.isEmpty() && !postinumeroStr.isEmpty() && !postitoimipaikka.isEmpty() && !kapasiteettiStr.isEmpty() && !vuorokausihintaStr.isEmpty()) {
                int postinumero = Integer.parseInt(postinumeroStr);
                int kapasiteetti = Integer.parseInt(kapasiteettiStr);
                double vuorokausihinta = Double.parseDouble(vuorokausihintaStr);

                boolean onnistuiko = ToimipisteTietokanta.luoUusiToimipiste(nimi, osoite, postinumero, postitoimipaikka, kapasiteetti, vuorokausihinta);
                if (onnistuiko) {
                    data.add(new Toimipiste(nimi, osoite, ToimipisteTietokanta.viimeisinToimipisteID(), postinumero, postitoimipaikka, kapasiteetti, vuorokausihinta));
                    tfToimipiste.clear();
                    tfOsoite.clear();
                    tfPostinumero.clear();
                    tfPostitoimipaikka.clear();
                    tfKapasiteetti.clear();
                    tfVuorokausihinta.clear();
                    lvPalvelut.setItems(FXCollections.observableArrayList());
                    this.asetaVirheteksti("");
                } else {
                    this.asetaVirheteksti("Täytä kaikki kentät!");
                }
            }
        });


        btPoista.setOnAction(event -> {
            Toimipiste valittuPiste = tvTaulu.getSelectionModel().getSelectedItem();
            if (valittuPiste != null) {
                String nimi = valittuPiste.getNimi();
                if (poistoVarmistus.poistoVarmistus(nimi)) {
                    boolean onnistuiko = ToimipisteTietokanta.poistaToimipiste(valittuPiste.getToimipisteID());
                    if (onnistuiko) {
                        data.remove(valittuPiste);
                        tfToimipiste.clear();
                        tfOsoite.clear();
                        tfPostinumero.clear();
                        tfPostitoimipaikka.clear();
                        tfKapasiteetti.clear();
                        tfVuorokausihinta.clear();
                        this.asetaVirheteksti("");
                    } else {
                        this.asetaVirheteksti("Täytä kaikki kentät!");
                    }
                }
            }
        });

        btMuokkaa.setOnAction(event -> {
            Toimipiste valittuPiste = tvTaulu.getSelectionModel().getSelectedItem();
            if (valittuPiste != null) {
                // Hae ja näytä valitun toimipisteen palvelut
                ObservableList<String> palvelut = ToimipisteTietokanta.haeToimipisteenPalvelut(valittuPiste.getToimipisteID());
                lvPalvelut.setItems(palvelut);

                String nimi = tfToimipiste.getText();
                String osoite = tfOsoite.getText();
                String postinumeroStr = tfPostinumero.getText();
                String postitoimipaikka = tfPostitoimipaikka.getText();
                String kapasiteettiStr = tfKapasiteetti.getText();
                String vuorokausihintaStr = tfVuorokausihinta.getText();

                if (!nimi.isEmpty() && !osoite.isEmpty() && !postinumeroStr.isEmpty() && !postitoimipaikka.isEmpty() && !kapasiteettiStr.isEmpty() && !vuorokausihintaStr.isEmpty()) {
                    int postinumero = Integer.parseInt(postinumeroStr);
                    int kapasiteetti = Integer.parseInt(kapasiteettiStr);
                    double vuorokausihinta = Double.parseDouble(vuorokausihintaStr);

                    boolean onnistuiko = ToimipisteTietokanta.muokkaaToimipisteenTietoja(valittuPiste.getToimipisteID(), nimi, osoite, postinumero, postitoimipaikka, kapasiteetti, vuorokausihinta);
                    if (onnistuiko) {
                        valittuPiste.setNimi(nimi);
                        valittuPiste.setLahiosoite(osoite);
                        valittuPiste.setPostinumero(postinumero);
                        valittuPiste.setPostitoimipaikka(postitoimipaikka);
                        valittuPiste.setKapasiteetti(kapasiteetti);
                        valittuPiste.setVrkhinta(vuorokausihinta);
                        tvTaulu.refresh();
                        this.asetaVirheteksti("");
                    } else {
                        this.asetaVirheteksti("Toimipisteen tallentaminen epäonnistui!\nTarkista tiedot.");
                    }
                }
            }
        });

        btPeruuta.setOnAction(event -> {
            tfToimipiste.clear();
            tfOsoite.clear();
            tfPostinumero.clear();
            tfPostitoimipaikka.clear();
            tfKapasiteetti.clear();
            tfVuorokausihinta.clear();
            tvTaulu.getSelectionModel().clearSelection();
            lvPalvelut.setItems(FXCollections.observableArrayList());
            this.asetaVirheteksti("");
        });
    }
    private void asetaVirheteksti(String virhe) {
        this.txVirhe.setText(virhe);
    }

    @FXML
    void btTakaisin(ActionEvent event) {
        Main.changeScene("main.fxml");
    }
}