package asiakas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class AsiakasController implements Initializable {

    @FXML
    private Button btLuoUusiAsiakas;

    @FXML
    private Button btPoistaAsiakas;

    @FXML
    private Button btTakaisin;

    @FXML
    private Button btTallennaMuutokset;

    @FXML
    private Button btTyhjennaKentat;

    @FXML
    private HBox hboxAlaPainikkeet;

    @FXML
    private HBox hboxYlaPainikkeet;

    @FXML
    private Label lbAsiakkaat;

    @FXML
    private Label lbHaeAsiakkaita;

    @FXML
    private Label lbUusiAsiakas;

    @FXML
    private TableColumn<Asiakas, Integer> tcAsiakasID;

    @FXML
    private TableColumn<Asiakas, String> tcEtunimi;

    @FXML
    private TableColumn<Asiakas, String> tcLahiosoite;

    @FXML
    private TableColumn<Asiakas, String> tcPostinumero;

    @FXML
    private TableColumn<Asiakas, String> tcPostitoimipaikka;

    @FXML
    private TableColumn<Asiakas, String> tcPuhelinnumero;

    @FXML
    private TableColumn<Asiakas, String> tcSahkopostiosoite;

    @FXML
    private TableColumn<Asiakas, String> tcSukunimi;

    @FXML
    private TextField tfEtunimi;

    @FXML
    private TextField tfHaku;

    @FXML
    private TextField tfLahiosoite;

    @FXML
    private TextField tfPostinumero;

    @FXML
    private TextField tfPostitoimipaikka;

    @FXML
    private TextField tfPuhelinnumero;

    @FXML
    private TextField tfSahkopostiosoite;

    @FXML
    private TextField tfSukunimi;

    @FXML
    private TableView<Asiakas> tvAsiakasTaulukko;

    @FXML
    private Text txVirhe;

    @FXML
    private VBox vboxPainikkeetTekstikentat;

    private ObservableList<Asiakas> asiakkaatLista;

    private Asiakas valittuAsiakas;

    private int valitunAsiakkaanAsiakasID;

    private int valitunAsiakkaanIndeksi;


    @FXML
    void btTakaisin(ActionEvent event) {
        Main.changeScene("main.fxml");
    }

    @FXML
    void btLuoUusiAsiakas(ActionEvent event) {
        String etunimi = tfEtunimi.getText();
        String sukunimi = tfSukunimi.getText();
        String sahkopostiosoite = tfSahkopostiosoite.getText();
        String puhelinnumero = tfPuhelinnumero.getText();
        String lahiosoite = tfLahiosoite.getText();
        String postinumero = tfPostinumero.getText();
        String postitoimipaikka = tfPostitoimipaikka.getText();

        if (!etunimi.isEmpty() && !sukunimi.isEmpty() && !sahkopostiosoite.isEmpty() && !puhelinnumero.isEmpty() &&
                !lahiosoite.isEmpty() && !postinumero.isEmpty() && !postitoimipaikka.isEmpty()) {

            boolean onnistuiko = AsiakasTietokanta.luoUusiAsiakas(etunimi, sukunimi, sahkopostiosoite, puhelinnumero, lahiosoite,
                    postinumero, postitoimipaikka);

            if (onnistuiko) {
                int asiakasID = AsiakasTietokanta.viimeisinAsiakasID();
                if (asiakasID != -1) {
                    Asiakas asiakas = new Asiakas(asiakasID, etunimi, sukunimi, sahkopostiosoite, puhelinnumero, lahiosoite,
                            postinumero, postitoimipaikka);
                    this.asiakkaatLista.add(asiakas);
                    this.tfHaku.clear();
                    this.asetaVirheteksti("");
                }
            } else {
                this.asetaVirheteksti("Asiakkaan lisääminen epäonnistui!\nTarkista tiedot.");
            }

        } else {
            this.asetaVirheteksti("Täytä kaikki kentät!");
        }
    }

    @FXML
    void btTallennaMuutokset(ActionEvent event) {
        if (this.valittuAsiakas != null) {
            String etunimi = this.tfEtunimi.getText();
            String sukunimi = this.tfSukunimi.getText();
            String sahkopostiosoite = this.tfSahkopostiosoite.getText();
            String puhelinnumero = this.tfPuhelinnumero.getText();
            String lahiosoite = this.tfLahiosoite.getText();
            String postinumero = this.tfPostinumero.getText();
            String postitoimipaikka = this.tfPostitoimipaikka.getText();

            if (!etunimi.isEmpty() && !sukunimi.isEmpty() && !sahkopostiosoite.isEmpty() && !puhelinnumero.isEmpty() &&
                    !lahiosoite.isEmpty() && !postinumero.isEmpty() && !postitoimipaikka.isEmpty()) {

                boolean onnistuiko = AsiakasTietokanta.muokkaaAsiakkaanTietoja(this.valitunAsiakkaanAsiakasID, etunimi,
                        sukunimi, sahkopostiosoite, puhelinnumero, lahiosoite, postinumero, postitoimipaikka);

                if (onnistuiko) {
                    this.valittuAsiakas.setEtunimi(etunimi);
                    this.valittuAsiakas.setSukunimi(sukunimi);
                    this.valittuAsiakas.setSahkopostiosoite(sahkopostiosoite);
                    this.valittuAsiakas.setPuhelinnumero(puhelinnumero);
                    this.valittuAsiakas.setLahiosoite(lahiosoite);
                    this.valittuAsiakas.setPostinumero(postinumero);
                    this.valittuAsiakas.setPostitoimipaikka(postitoimipaikka);

                    this.tvAsiakasTaulukko.refresh();
                    this.tfHaku.clear();
                    this.asetaVirheteksti("");
                } else {
                    this.asetaVirheteksti("Asiakkaan tietojen päivitys epäonnistui.\nTarkista tiedot.");
                }
            } else {
                asetaVirheteksti("Tyhjiä kenttiä ei sallita!");
            }
        }
    }

    @FXML
    void btPoistaAsiakas(ActionEvent event) {
        if (this.valittuAsiakas != null) {
            boolean onnistuiko = AsiakasTietokanta.poistaAsiakas(this.valitunAsiakkaanAsiakasID);
            if (onnistuiko) {
                this.tvAsiakasTaulukko.getItems().remove(this.valitunAsiakkaanIndeksi);
                this.tyhjennaKentat();
                this.asetaVirheteksti("");
            }
        } else {
            this.asetaVirheteksti("Asiakasta ei ole valittuna!");
        }
    }

    @FXML
    void btTyhjennaKentat(ActionEvent event) {
        this.tyhjennaKentat();
        this.asetaVirheteksti("");
    }

    //Lisää taulukkoon asiakkaiden tiedot tietokannasta
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tcAsiakasID.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
        this.tcEtunimi.setCellValueFactory(new PropertyValueFactory<>("etunimi"));
        this.tcSukunimi.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));
        this.tcSahkopostiosoite.setCellValueFactory(new PropertyValueFactory<>("sahkopostiosoite"));
        this.tcPuhelinnumero.setCellValueFactory(new PropertyValueFactory<>("puhelinnumero"));
        this.tcLahiosoite.setCellValueFactory(new PropertyValueFactory<>("lahiosoite"));
        this.tcPostinumero.setCellValueFactory(new PropertyValueFactory<>("postinumero"));
        this.tcPostitoimipaikka.setCellValueFactory(new PropertyValueFactory<>("postitoimipaikka"));

        this.asiakkaatLista = AsiakasTietokanta.haeKaikkiAsiakkaat();
        this.tvAsiakasTaulukko.setItems(this.asiakkaatLista);

        //Päivittää valitun asiakkaan tiedot tekstikenttiin
        this.tvAsiakasTaulukko.getSelectionModel().selectedItemProperty().addListener(e -> {
            this.valittuAsiakas = this.tvAsiakasTaulukko.getSelectionModel().getSelectedItem();
            if (this.valittuAsiakas != null) {
                this.tfEtunimi.setText(this.valittuAsiakas.getEtunimi());
                this.tfSukunimi.setText(this.valittuAsiakas.getSukunimi());
                this.tfSahkopostiosoite.setText(this.valittuAsiakas.getSahkopostiosoite());
                this.tfPuhelinnumero.setText(this.valittuAsiakas.getPuhelinnumero());
                this.tfLahiosoite.setText(this.valittuAsiakas.getLahiosoite());
                this.tfPostinumero.setText(this.valittuAsiakas.getPostinumero());
                this.tfPostitoimipaikka.setText(this.valittuAsiakas.getPostitoimipaikka());

                this.valitunAsiakkaanAsiakasID = this.valittuAsiakas.getAsiakasID();
                this.valitunAsiakkaanIndeksi = this.tvAsiakasTaulukko.getSelectionModel().getSelectedIndex();

                this.asetaVirheteksti("");
            }
        });

        this.tfHaku.textProperty().addListener(e -> {
            this.suodataAsiakkaita();
            this.asetaVirheteksti("");
        });
    }

    //Tyhjentää kaikki tekstikentät
    private void tyhjennaKentat() {
        this.tfHaku.clear();
        this.tfEtunimi.clear();
        this.tfSukunimi.clear();
        this.tfSahkopostiosoite.clear();
        this.tfLahiosoite.clear();
        this.tfPostitoimipaikka.clear();
        this.tfPostinumero.clear();
        this.tfPuhelinnumero.clear();
    }

    private void asetaVirheteksti(String virhe) {
        this.txVirhe.setText(virhe);
    }

    //Hakutoiminto
    private void suodataAsiakkaita() {
        String hakusana = this.tfHaku.getText().toLowerCase();

        if (hakusana.isEmpty()) {
            this.tvAsiakasTaulukko.setItems(this.asiakkaatLista);
        } else {
            ObservableList<Asiakas> suodatettuLista = FXCollections.observableArrayList();
            for (Asiakas asiakas : this.asiakkaatLista) {
                String asiakasID = asiakas.getAsiakasID() + "";
                String sukunimi = asiakas.getSukunimi().toLowerCase();

                if (asiakasID.contains(hakusana) || sukunimi.contains(hakusana)) {
                    suodatettuLista.add(asiakas);
                }
            }

            this.tvAsiakasTaulukko.setItems(suodatettuLista);
        }
    }

}

