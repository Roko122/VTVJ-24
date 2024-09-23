package varaus;

import asiakas.Asiakas;
import asiakas.AsiakasTietokanta;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import main.Main;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.controlsfx.control.CheckComboBox;
import tietokanta.TietokantaManager;
import toimipiste.Toimipiste;

public class VarausController implements Initializable {

    @FXML
    private ListView<String> PalvelutLista;

    @FXML
    private Text VapaavaivarattuText;

    @FXML
    private Button btPeruuta;

    @FXML
    private Button btPoista;

    @FXML
    private Button btTakaisin;

    @FXML
    private Button btTallennaMuutokset;

    @FXML
    private Button btTeeVaraus;

    @FXML
    private Circle cVapaana;

    @FXML
    private ComboBox<String> cbToimipisteValinta;

    @FXML
    private CheckComboBox<String> ccbPalvelutValinta;

    @FXML
    private DatePicker dpAloituspvm;

    @FXML
    private DatePicker dpLopetuspvm;

    @FXML
    private TableColumn<Asiakas, Integer> tcAsiakasID;

    @FXML
    private TableColumn<Asiakas, String> tcSukunimi;

    @FXML
    private TableColumn<Varaus, Date> tcAlkupvm;

    @FXML
    private TableColumn<Varaus, String> tcAsiakas;

    @FXML
    private TableColumn<Varaus, Date> tcLoppupvm;

    @FXML
    private TableColumn<Varaus, String> tcToimipiste;

    @FXML
    private TableColumn<Varaus, Integer> tcVarausID;

    @FXML
    private TableColumn<Varaus, Timestamp> tcVarausaika;

    @FXML
    private TextField tfAsiakasID;

    @FXML
    private TextField tfAsiakasLahiosoite;

    @FXML
    private TextField tfAsiakasPostinumero;

    @FXML
    private TextField tfAsiakasPostitoimipaikka;

    @FXML
    private TextField tfEtunimi;

    @FXML
    private TextField tfHaeAsiakas;

    @FXML
    private TextField tfHintavrk;

    @FXML
    private TextField tfKapasiteetti;

    @FXML
    private TextField tfPuhelinnumero;

    @FXML
    private TextField tfSahkopostiosoite;

    @FXML
    private TextField tfSukunimi;

    @FXML
    private TextField tfToimipisteID;

    @FXML
    private TextField tfToimipisteLahiosoite;

    @FXML
    private TextField tfToimipisteNimi;

    @FXML
    private TextField tfToimipistePostinumero;

    @FXML
    private TextField tfToimipistePostitoimipaikka;

    @FXML
    private TableView<Asiakas> tvAsiakkaat;

    @FXML
    private TableView<Varaus> tvVaraukset;

    @FXML
    private TextField varausKestoKentta;

    private Asiakas valittuAsiakas;

    private Toimipiste valittuToimipiste;

    private Varaus valittuVaraus;

    private ObservableList<Varaus> varauksetLista;
    private ObservableList<Asiakas> asiakkaatLista;

    private List<Toimipiste> kaikkiToimipisteet = new ArrayList<>();




    @FXML
    void btTakaisin(ActionEvent event) {
        Main.changeScene("main.fxml");
    }

    public void kaytaValitutPalvelut(ObservableList<String> valitutPalvelut) {
        valitutPalvelut = ccbPalvelutValinta.getCheckModel().getCheckedItems();
    }

    @FXML
    void btTeeVaraus(ActionEvent event) {
        if (this.valittuToimipiste != null && this.valittuAsiakas != null) {
                Date alkupvm = Date.valueOf(dpAloituspvm.getValue());
                Date loppupvm = Date.valueOf(dpLopetuspvm.getValue());
                Asiakas asiakas = tvAsiakkaat.getSelectionModel().getSelectedItem();

                LocalDateTime varausLocalDateTime = LocalDateTime.now();
                Timestamp varausaika = Timestamp.valueOf(varausLocalDateTime);

                String asiakkaanSukunimi = (asiakas != null) ? asiakas.getSukunimi() : null;
                String toimipisteNimi = cbToimipisteValinta.getSelectionModel().getSelectedItem();

            if (alkupvm != null && loppupvm != null &&
                    asiakkaanSukunimi != null && toimipisteNimi != null) {
                    List<String> valitutPalvelut = new ArrayList<>(ccbPalvelutValinta.getCheckModel().getCheckedItems());

                    int toimipisteID = haeToimipisteID(toimipisteNimi);
                    int asiakasID = haeAsiakasID(asiakkaanSukunimi);
                    if (toimipisteID != -1 && asiakasID != -1) {
                        boolean onnistuikoVaraus = VarausTietokanta.lisaaVaraus(alkupvm, loppupvm, varausaika, toimipisteID, asiakasID, valitutPalvelut);
                        if (onnistuikoVaraus) {
                            Varaus uusiVaraus = new Varaus(VarausTietokanta.viimeisinVarausID(), asiakkaanSukunimi, toimipisteNimi, alkupvm, loppupvm, new Timestamp(System.currentTimeMillis()));
                            this.varauksetLista.add(uusiVaraus);
                            tvVaraukset.refresh();
                            tfHaeAsiakas.clear();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Virhe");
                        alert.setHeaderText(null);
                        alert.setContentText("Virhe toimipisteen tai asiakkaan kanssa.");
                        alert.showAndWait();
                    }
                }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Varauksen luominen");
            alert.setHeaderText(null);
            alert.setContentText("Valitse toimipiste ja asiakas ennen varauksen tekemistä.");
            alert.showAndWait();
        }
    }

    private int haeToimipisteID(String toimipisteNimi) {
        try {
            String query = "SELECT toimipisteID FROM toimipiste WHERE nimi = ?";
            ResultSet tulos = TietokantaManager.teeKysely(query, toimipisteNimi);
            if (tulos.next()) {
                return tulos.getInt("toimipisteID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Palautetaan -1, jos toimipistettä ei löydy tai tapahtuu virhe
    }
    private int haeAsiakasID(String asiakkaanSukunimi) {
        try {
            String query = "SELECT asiakasID FROM asiakas WHERE sukunimi = ?";
            ResultSet tulos = TietokantaManager.teeKysely(query, asiakkaanSukunimi);
            if (tulos.next()) {
                return tulos.getInt("asiakasID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
        // Asiakkaan hakulogiikka


    @FXML
    void btTallennaMuutokset(ActionEvent event) {
        Varaus valittuVaraus = tvVaraukset.getSelectionModel().getSelectedItem();
        if (valittuVaraus != null) {
            int varausID = valittuVaraus.getVarausID();
            Date alkupvm = Date.valueOf(dpAloituspvm.getValue());
            Date loppupvm = Date.valueOf(dpLopetuspvm.getValue());
            String asiakkaanSukunimi = tfSukunimi.getText();
            String toimipisteNimi = cbToimipisteValinta.getSelectionModel().getSelectedItem();

            if (alkupvm != null && loppupvm != null &&
                    asiakkaanSukunimi != null && toimipisteNimi != null) {
                List<String> valitutPalvelut = new ArrayList<>(ccbPalvelutValinta.getCheckModel().getCheckedItems());
                int toimipisteID = haeToimipisteID(toimipisteNimi);
                int asiakasID = haeAsiakasID(asiakkaanSukunimi);
                if (toimipisteID != -1 && asiakasID != -1) {
                    boolean onnistuikoMuokkaus = VarausTietokanta.muokkaaVarauksenTietoja(varausID, alkupvm, loppupvm, toimipisteID, asiakasID, valitutPalvelut);
                    if (onnistuikoMuokkaus) {
                        // Päivitä varauslista ja tyhjennä kentät
                        valittuVaraus.setAlkupvm(alkupvm);
                        valittuVaraus.setLoppupvm(loppupvm);
                        valittuVaraus.setToimipiste(toimipisteNimi);
                        valittuVaraus.setAsiakkaanSukunimi(asiakkaanSukunimi);
                        valittuVaraus.setVarausaika(new Timestamp(System.currentTimeMillis()));
                        tvVaraukset.refresh();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Virhe");
                        alert.setHeaderText(null);
                        alert.setContentText("Varauksen muokkaus epäonnistui.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Virhe");
                    alert.setHeaderText(null);
                    alert.setContentText("Virhe toimipisteen tai asiakkaan kanssa.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Täytä kaikki kentät ennen tallentamista.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Varauksen muokkaus");
            alert.setHeaderText(null);
            alert.setContentText("Valitse varaus ennen muokkaamista.");
            alert.showAndWait();
        }


    }

    @FXML
    void btPeruuta(ActionEvent event) {
        this.tyhjennaKentat();
    }

    @FXML
    void btPoista(ActionEvent event) {
        Varaus valittuVaraus = tvVaraukset.getSelectionModel().getSelectedItem();
        if (valittuVaraus != null) {
            int varausID = valittuVaraus.getVarausID();
            boolean onnistuiko = VarausTietokanta.poistaVaraus(varausID);
            if (onnistuiko) {
                this.varauksetLista.remove(valittuVaraus);
                this.tyhjennaKentat();
                this.tvVaraukset.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Varauksen poistaminen epäonnistui.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Varauksen poisto");
            alert.setHeaderText(null);
            alert.setContentText("Valitse varaus ennen poistamista.");
            alert.showAndWait();
        }
    }

    @FXML
    void dateChanged(ActionEvent event) {
        calculateDuration();
    }

    private void calculateDuration() {
        LocalDate alkuPvm = dpAloituspvm.getValue();
        LocalDate loppuPvm = dpLopetuspvm.getValue();

        if (alkuPvm != null && loppuPvm != null) {
            long duration = java.time.temporal.ChronoUnit.DAYS.between(alkuPvm, loppuPvm);
            varausKestoKentta.setText(String.valueOf(duration));
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.asetaAsiakkaatTableViewiin();
        this.asetaVarauksetTableViewiin();


        this.asiakkaatLista = AsiakasTietokanta.haeKaikkiAsiakkaat();
        this.tfHaeAsiakas.textProperty().addListener(e -> {
            this.suodataAsiakasHakua();
        });

        haeToimipisteet();

        // muuta hakemaan olio listalta
        this.cbToimipisteValinta.setOnAction(event -> {
            valitseToimipiste();
            if (dpAloituspvm.getValue() != null && dpLopetuspvm.getValue() != null && valittuToimipiste != null) {
                if (this.onkoVapaana()) {
                    this.cVapaana.setFill(Color.GREEN);
                } else {
                    this.cVapaana.setFill(Color.RED);
                }
            } else {
                this.cVapaana.setFill(Color.GRAY);
            }
        });

        this.ccbPalvelutValinta.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    // Poista ensin vanhat kohteet
                    PalvelutLista.getItems().removeAll(change.getAddedSubList());
                    // Lisää sitten uudet valitut vaihtoehdot listView:iin
                    PalvelutLista.getItems().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    // Poista poistetut valitut vaihtoehdot listView:stä
                    PalvelutLista.getItems().removeAll(change.getRemoved());
                }
            }
        });

        //päivittää asiakkaan tiedot tekstikenttiin
        this.tvAsiakkaat.getSelectionModel().selectedItemProperty().addListener(e -> {
            this.valittuAsiakas = this.tvAsiakkaat.getSelectionModel().getSelectedItem();
            if (this.valittuAsiakas != null) {
                this.tfAsiakasID.setText(Integer.toString(this.valittuAsiakas.getAsiakasID()));
                this.tfEtunimi.setText(this.valittuAsiakas.getEtunimi());
                this.tfSukunimi.setText(this.valittuAsiakas.getSukunimi());
                this.tfSahkopostiosoite.setText(this.valittuAsiakas.getSahkopostiosoite());
                this.tfPuhelinnumero.setText(this.valittuAsiakas.getPuhelinnumero());
                this.tfAsiakasLahiosoite.setText(this.valittuAsiakas.getLahiosoite());
                this.tfAsiakasPostinumero.setText(this.valittuAsiakas.getPostinumero());
                this.tfAsiakasPostitoimipaikka.setText(this.valittuAsiakas.getPostitoimipaikka());
            }
        });

        this.tvVaraukset.getSelectionModel().selectedItemProperty().addListener(e -> {
            this.valittuVaraus = this.tvVaraukset.getSelectionModel().getSelectedItem();
            if (this.valittuVaraus != null) {
                int toimipisteID = haeToimipisteID(this.valittuVaraus.getToimipiste());
                int asiakasID = haeAsiakasID(this.valittuVaraus.getAsiakkaanSukunimi());
                this.valittuToimipiste = haeToimipisteenTiedot(toimipisteID);
                this.valittuAsiakas = haeAsiakkaanTiedot(asiakasID);

                this.tfAsiakasID.setText(Integer.toString(this.valittuAsiakas.getAsiakasID()));
                this.tfEtunimi.setText(this.valittuAsiakas.getEtunimi());
                this.tfSukunimi.setText(this.valittuAsiakas.getSukunimi());
                this.tfSahkopostiosoite.setText(this.valittuAsiakas.getSahkopostiosoite());
                this.tfPuhelinnumero.setText(this.valittuAsiakas.getPuhelinnumero());
                this.tfAsiakasLahiosoite.setText(this.valittuAsiakas.getLahiosoite());
                this.tfAsiakasPostinumero.setText(this.valittuAsiakas.getPostinumero());
                this.tfAsiakasPostitoimipaikka.setText(this.valittuAsiakas.getPostitoimipaikka());

                this.tfToimipisteID.setText(Integer.toString(this.valittuToimipiste.getToimipisteID()));
                this.tfToimipisteNimi.setText(this.valittuToimipiste.getNimi());
                this.tfToimipisteLahiosoite.setText(this.valittuToimipiste.getLahiosoite());
                this.tfToimipistePostinumero.setText(Integer.toString(this.valittuToimipiste.getPostinumero()));
                this.tfToimipistePostitoimipaikka.setText(this.valittuToimipiste.getPostitoimipaikka());
                this.tfKapasiteetti.setText(Integer.toString(this.valittuToimipiste.getKapasiteetti()));
                this.tfHintavrk.setText(String.valueOf(this.valittuToimipiste.getVrkhinta()));

                this.tvAsiakkaat.getSelectionModel().select(this.valittuAsiakas);
                this.cbToimipisteValinta.getSelectionModel().select(this.valittuVaraus.getToimipiste());
                this.dpAloituspvm.setValue(this.valittuVaraus.getAlkupvm().toLocalDate());
                this.dpLopetuspvm.setValue(this.valittuVaraus.getLoppupvm().toLocalDate());
                int varausID = valittuVaraus.getVarausID();
                haeVarauksenPalvelut(varausID);

            }
        });

        this.dpAloituspvm.valueProperty().addListener(e -> {
            if (dpAloituspvm.getValue() != null && dpLopetuspvm.getValue() != null && valittuToimipiste != null) {
                if (this.onkoVapaana()) {
                    this.cVapaana.setFill(Color.GREEN);
                } else {
                    this.cVapaana.setFill(Color.RED);
                }
            } else {
                this.cVapaana.setFill(Color.GRAY);
            }
        });

        this.dpLopetuspvm.valueProperty().addListener(e -> {
            if (dpAloituspvm.getValue() != null && dpLopetuspvm.getValue() != null && valittuToimipiste != null) {
                if (this.onkoVapaana()) {
                    this.cVapaana.setFill(Color.GREEN);
                } else {
                    this.cVapaana.setFill(Color.RED);
                }
            } else {
                this.cVapaana.setFill(Color.GRAY);
            }
        });
    }
    private void haeVarauksenPalvelut(int varausID) { // hakee palveluIDt valitulta varaukselta
        try {
            // lista palveluID:lle
            List<Integer> palveluIDt = new ArrayList<>();

            String query = "SELECT palveluID FROM varauksenpalvelut WHERE varausID = ?";
            ResultSet resultSet = TietokantaManager.teeKysely(query, varausID);

            while (resultSet.next()) {
                int palveluID = resultSet.getInt("palveluID");
                palveluIDt.add(palveluID);
            }
            List<String> valitutPalvelut = new ArrayList<>();
            for (Integer palveluID : palveluIDt) {
                String palvelunNimi = haePalvelunNimi(palveluID);
                valitutPalvelut.add(palvelunNimi);
            }
            ccbPalvelutValinta.getCheckModel().clearChecks();
            for (String palvelunNimi : valitutPalvelut) {
                ccbPalvelutValinta.getCheckModel().check(palvelunNimi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // hakee palvelun nimen palveluID:n perusteella
    private String haePalvelunNimi(int palveluID) {
        String palvelunNimi = null;
        try {
        String query = "SELECT nimi FROM palvelu WHERE palveluID = ?";
        ResultSet resultSet = TietokantaManager.teeKysely(query, palveluID);
        if (resultSet.next()) {
            palvelunNimi = resultSet.getString("nimi");
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return palvelunNimi;
    }
    private Asiakas haeAsiakkaanTiedot(int asiakasID) {
        Asiakas asiakas = null;
        String sql = "SELECT * " +
                "FROM asiakas " +
                "WHERE asiakasID = ?";
        ResultSet tulos = TietokantaManager.teeKysely(sql, asiakasID);
        try {
            while (tulos.next() == true) {
                int asiakasID2 = tulos.getInt("asiakasID");
                String etunimi = tulos.getString("etunimi");
                String sukunimi = tulos.getString("sukunimi");
                String sahkopostiosoite = tulos.getString("sahkopostiosoite");
                String puhelinnumero = tulos.getString("puhelinnumero");
                String lahiosoite = tulos.getString("lahiosoite");
                String postinumero = tulos.getString("postinumero");
                String postitoimipaikka = tulos.getString("postitoimipaikka");

                asiakas = new Asiakas(asiakasID2, etunimi, sukunimi, sahkopostiosoite, puhelinnumero, lahiosoite,
                        postinumero, postitoimipaikka);
            }
        } catch (SQLException se) {
            System.err.println("Virhe!");
        }
        return asiakas;
    }

    private Toimipiste haeToimipisteenTiedot(int toimipisteID) {
        Toimipiste toimipiste = null;
        String sql = "SELECT * FROM Toimipiste WHERE toimipisteID = ?";
        ResultSet tulos = TietokantaManager.teeKysely(sql, toimipisteID);
        try {
            while (tulos.next()) {
                String nimi = tulos.getString("nimi");
                String osoite = tulos.getString("lahiosoite");
                int id = tulos.getInt("toimipisteID");
                int postinumero = Integer.parseInt(tulos.getString("postinumero"));
                String postitoimipaikka = tulos.getString("postitoimipaikka");
                int kapasiteetti = tulos.getInt("kapasiteetti");
                double vuorokausihinta = tulos.getDouble("vrkhinta");

                toimipiste = new Toimipiste(nimi, osoite, id, postinumero, postitoimipaikka,
                        kapasiteetti, vuorokausihinta);
            }
        } catch (SQLException se) {
            System.err.println("Virhe!");
        }

        return toimipiste;
    }

    private void tyhjennaKentat() {
        this.cbToimipisteValinta.getSelectionModel().clearSelection();
        this.ccbPalvelutValinta.getCheckModel().clearChecks();
        this.tfHaeAsiakas.clear();
        this.varausKestoKentta.clear();
        this.tfAsiakasID.clear();
        this.tfEtunimi.clear();
        this.tfSukunimi.clear();
        this.tfSahkopostiosoite.clear();
        this.tfPuhelinnumero.clear();
        this.tfAsiakasLahiosoite.clear();
        this.tfAsiakasPostinumero.clear();
        this.tfAsiakasPostitoimipaikka.clear();
        this.tfToimipisteID.clear();
        this.tfToimipisteNimi.clear();
        this.tfToimipisteLahiosoite.clear();
        this.tfToimipistePostinumero.clear();
        this.tfToimipistePostitoimipaikka.clear();
        this.tfKapasiteetti.clear();
        this.tfHintavrk.clear();
        this.dpAloituspvm.setValue(null);
        this.dpLopetuspvm.setValue(null);
        this.tvAsiakkaat.getSelectionModel().clearSelection();
        this.tvVaraukset.getSelectionModel().clearSelection();
        this.valittuToimipiste = null;
    }

    private void asetaAsiakkaatTableViewiin() {
        this.tcAsiakasID.setCellValueFactory(new PropertyValueFactory<>("asiakasID"));
        this.tcSukunimi.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));
        this.tvAsiakkaat.setItems(AsiakasTietokanta.haeKaikkiAsiakkaat());
    }

    private void suodataAsiakasHakua() {
        String hakusana = this.tfHaeAsiakas.getText().toLowerCase();

        if (hakusana.isEmpty()) {
            this.tvAsiakkaat.setItems(this.asiakkaatLista);
        } else {
            ObservableList<Asiakas> suodatettuHakuLista = FXCollections.observableArrayList();
            for (Asiakas asiakas : this.asiakkaatLista) {
                String asiakasID = asiakas.getAsiakasID() + "";
                String sukunimi = asiakas.getSukunimi().toLowerCase();

                if (asiakasID.contains(hakusana) || sukunimi.contains(hakusana)) {
                    suodatettuHakuLista.add(asiakas);
                }
            }
            this.tvAsiakkaat.setItems(suodatettuHakuLista);
        }
    }
    private void haeToimipisteet() {
        cbToimipisteValinta.getItems().clear();
        try {
            ResultSet resultSet = TietokantaManager.teeKysely("SELECT * FROM toimipiste");
            while (resultSet.next()) {
                int toimipisteID = resultSet.getInt("toimipisteID");
                String nimi = resultSet.getString("nimi");
                String lahiosoite = resultSet.getString("lahiosoite");
                int postinumero = resultSet.getInt("postinumero");
                String postitoimipaikka = resultSet.getString("postitoimipaikka");
                int kapasiteetti = resultSet.getInt("kapasiteetti");
                int vrkhinta = resultSet.getInt("vrkhinta");

                // Luo uusi Toimipiste-olio
                Toimipiste toimipiste = new Toimipiste(nimi, lahiosoite, toimipisteID, postinumero, postitoimipaikka, kapasiteetti, vrkhinta);

                // Lisää toimipiste listaan
                kaikkiToimipisteet.add(toimipiste);
            }

            // Lisää toimipisteiden nimet ComboBoxiin
            for (Toimipiste toimipiste : kaikkiToimipisteet) {
                cbToimipisteValinta.getItems().add(toimipiste.getNimi());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void valitseToimipiste() {
        String valittutoimipisteNimi = cbToimipisteValinta.getSelectionModel().getSelectedItem();
        if (valittutoimipisteNimi != null) {
            for (Toimipiste toimipiste : kaikkiToimipisteet) {
                if (toimipiste.getNimi().equals(valittutoimipisteNimi)) {
                    this.valittuToimipiste = toimipiste;
                    break;
                }
            }
        }
        if (this.valittuToimipiste != null) {
            this.tfToimipisteID.setText(Integer.toString(this.valittuToimipiste.getToimipisteID()));
            this.tfToimipisteNimi.setText(this.valittuToimipiste.getNimi());
            this.tfToimipisteLahiosoite.setText(this.valittuToimipiste.getLahiosoite());
            this.tfToimipistePostinumero.setText(Integer.toString(this.valittuToimipiste.getPostinumero()));
            this.tfToimipistePostitoimipaikka.setText(this.valittuToimipiste.getPostitoimipaikka());
            this.tfKapasiteetti.setText(Integer.toString(this.valittuToimipiste.getKapasiteetti()));
            this.tfHintavrk.setText(String.valueOf(this.valittuToimipiste.getVrkhinta()));

            ccbPalvelutValinta.getCheckModel().clearChecks();
            haePalvelut(this.valittuToimipiste);
        }
    }

        // hakee ja asettaa palvelut CheckComboboxiin
    @FXML
    void haePalvelut(Toimipiste valittuToimipiste) {
        if (valittuToimipiste != null) {
            try {
                ccbPalvelutValinta.getItems().clear();

                String query = "SELECT * FROM palvelu WHERE toimipisteID = (SELECT toimipisteID FROM toimipiste WHERE nimi = ? LIMIT 1)";
                ResultSet resultSet = TietokantaManager.teeKysely(query, valittuToimipiste.getNimi());

                while (resultSet.next()) {
                String palveluNimi = resultSet.getString("nimi");
                    ccbPalvelutValinta.getItems().add(palveluNimi);
                }

            } catch (SQLException e) {
            e.printStackTrace();
            }
        }
    }

    private void asetaVarauksetTableViewiin() {
        this.tcVarausID.setCellValueFactory(new PropertyValueFactory<>("varausID"));
        this.tcAsiakas.setCellValueFactory(new PropertyValueFactory<>("asiakkaanSukunimi"));
        this.tcToimipiste.setCellValueFactory(new PropertyValueFactory<>("toimipiste"));
        this.tcAlkupvm.setCellValueFactory(new PropertyValueFactory<>("alkupvm"));
        this.tcLoppupvm.setCellValueFactory(new PropertyValueFactory<>("loppupvm"));
        this.tcVarausaika.setCellValueFactory(new PropertyValueFactory<>("varausaika"));

        this.varauksetLista = VarausTietokanta.haeVaraukset();
        this.tvVaraukset.setItems(this.varauksetLista);

        this.tvVaraukset.getSortOrder().add(this.tcAlkupvm);
        this.tcAlkupvm.setSortType(TableColumn.SortType.ASCENDING);
        this.tvVaraukset.sort();
    }

    private boolean onkoVapaana() {
        boolean onkoVapaana = true;
        ArrayList<Varaus> valitunToimipisteenVaraukset = new ArrayList<>();
        LocalDate alkupvm = this.dpAloituspvm.getValue();
        LocalDate loppupvm = this.dpLopetuspvm.getValue();

        for (Varaus varaus : this.varauksetLista) {
            if (this.valittuToimipiste.getNimi().equals(varaus.getToimipiste())) {
                valitunToimipisteenVaraukset.add(varaus);
                if (this.valittuVaraus != null && this.valittuVaraus.getVarausID() == varaus.getVarausID()) {
                    valitunToimipisteenVaraukset.remove(varaus);
                }
            }
        }
        if (alkupvm != null && loppupvm != null && !valitunToimipisteenVaraukset.isEmpty())
            for (Varaus varaus : valitunToimipisteenVaraukset) {
                LocalDate varausAlkupvm = varaus.getAlkupvm().toLocalDate();
                LocalDate varausLoppupvm = varaus.getLoppupvm().toLocalDate();

                if ((alkupvm.isEqual(varausAlkupvm) || alkupvm.isAfter(varausAlkupvm)) &&
                        (alkupvm.isEqual(varausLoppupvm) || alkupvm.isBefore(varausLoppupvm)) ||
                        (loppupvm.isEqual(varausAlkupvm) || loppupvm.isAfter(varausAlkupvm)) &&
                        (loppupvm.isEqual(varausLoppupvm) || loppupvm.isBefore(varausLoppupvm)) ||
                        (alkupvm.isBefore(varausAlkupvm) && loppupvm.isAfter(varausLoppupvm))) {
                    onkoVapaana = false;
                }
            }

        return onkoVapaana;
    }
}