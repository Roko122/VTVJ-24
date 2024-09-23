package palvelu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main;
import tietokanta.TietokantaManager;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;


public class PalveluController implements Initializable {
    @FXML
    private HBox pHakuHbox;

    @FXML
    private TextField pHakuKentta;

    @FXML
    private TextField pHintaKentta;

    @FXML
    private Button pLisaaBtn;

    @FXML
    private Label pMuokkausLabel;

    @FXML
    private VBox pMuutosVbox;

    @FXML
    private BorderPane pNayttoPane;

    @FXML
    private VBox pNayttoVbox;

    @FXML
    private TextField pNimiKentta;

    @FXML
    private Label pPALVELUTLabel;

    @FXML
    private RadioButton pTyyppiLaiteRadioButton;

    @FXML
    private RadioButton pTyyppiLisapRadioButton;

    @FXML
    private ToggleGroup pTyyppiToggleGroup;

    @FXML
    private Button pPoistaBtn;

    @FXML
    private Button pTakaisinBtn;

    @FXML
    private Button pTallennaBtn;

    @FXML
    private ComboBox<String> pToimipisteComboBox;

    @FXML
    private Text tekstiVirhe;

    @FXML
    private TableView<Palvelu> palveluTableView;

    @FXML
    private TableColumn<Palvelu, Integer> palveluHinta;

    @FXML
    private TableColumn<Palvelu, Integer> palveluID;

    @FXML
    private TableColumn<Palvelu, String> palveluNimi;

    @FXML
    private TableColumn<Palvelu, String> palveluTyyppi;
    private ObservableList<Palvelu> palvelutLista;
    private Palvelu valittuPalvelu;
    private int valittuPalveluID;
    private int valittuPalveluIndeksi;

    @FXML
    void PalvelustaEtusivulle(ActionEvent e) {Main.changeScene("main.fxml");}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.palveluNimi.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        this.palveluTyyppi.setCellValueFactory(new PropertyValueFactory<>("tyyppi"));
        this.palveluHinta.setCellValueFactory(new PropertyValueFactory<>("hinta"));
        this.palveluID.setCellValueFactory(new PropertyValueFactory<>("palveluID"));

        this.palvelutLista = FXCollections.observableArrayList();

        haeJaNaytaToimipisteet();
        pToimipisteComboBox.setOnAction(event -> valitseToimipiste());


        this.palveluTableView.getSelectionModel().selectedItemProperty().addListener(e -> {
            this.valittuPalvelu = this.palveluTableView.getSelectionModel().getSelectedItem();
            if (this.valittuPalvelu != null) {
                this.pNimiKentta.setText(this.valittuPalvelu.getNimi());
                this.pHintaKentta.setText(String.valueOf(this.valittuPalvelu.getHinta()));
                String tyyppi = this.valittuPalvelu.getTyyppi();
                if (tyyppi.equals("Laite")) {
                    pTyyppiLaiteRadioButton.setSelected(true);
                } else if (tyyppi.equals("Lisäpalvelu")) {
                    pTyyppiLisapRadioButton.setSelected(true);
                }
                this.valittuPalveluID = this.valittuPalvelu.getPalveluID();
                this.valittuPalveluIndeksi = this.palveluTableView.getSelectionModel().getSelectedIndex();

                this.pVirheTeksti("");
            }
        });

        this.pHakuKentta.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                this.palveluTableView.setItems(this.palvelutLista);
            } else {
                this.palvelunHaku();
            }
            this.pVirheTeksti("");
        });

    }
    private void pTyhjennaKentat() {
        this.pHakuKentta.clear();
        this.pNimiKentta.clear();
        this.pHintaKentta.clear();
        this.pTyyppiToggleGroup.selectToggle(null);
    }

    // Palvelun hakeminen.
    private void palvelunHaku() {
        String pHakusana = this.pHakuKentta.getText().toLowerCase();

            ObservableList<Palvelu> pSuodatettuLista = FXCollections.observableArrayList();
            for (Palvelu palvelu : this.palvelutLista) {
                int palveluID = palvelu.getPalveluID();
                String nimi = palvelu.getNimi().toLowerCase();

                // tarkistetaan vastaako hakusana palvelun nimeä tai ID:tä
                if (nimi.contains(pHakusana) || String.valueOf(palveluID).contains(pHakusana)) {
                    pSuodatettuLista.add(palvelu);
                }
            }
            this.palveluTableView.setItems(pSuodatettuLista);
    }

    private void pVirheTeksti(String virhe) {
        this.tekstiVirhe.setText(virhe);
    }

    @FXML
    void haeJaNaytaToimipisteet() {
        try {
            // Haetaan toimipisteet tietokannasta ja lisätään ne comboboxiin
            ResultSet resultSet = TietokantaManager.teeKysely("SELECT nimi FROM toimipiste");
            while (resultSet.next()) {
                String toimipisteNimi = resultSet.getString("nimi");
                pToimipisteComboBox.getItems().add(toimipisteNimi);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void valitseToimipiste() {
        String valittuToimipiste = pToimipisteComboBox.getSelectionModel().getSelectedItem();
        if (valittuToimipiste != null) {
            haeJaNaytaPalvelut(palveluTableView, valittuToimipiste);
        }
    }

    @FXML
    public void haeJaNaytaPalvelut(TableView<Palvelu> palveluTableView, String valittuToimipiste) {
        if (valittuToimipiste != null) {
            try {
                ObservableList<Palvelu> pUusiLista = FXCollections.observableArrayList();

                // Haetaan palveluiden nimet tietokannasta ja lisätään ne TableViewiin
                String query = "SELECT * FROM palvelu WHERE toimipisteID = (SELECT toimipisteID FROM toimipiste WHERE nimi = ? LIMIT 1)";
                ResultSet resultSet = TietokantaManager.teeKysely(query, valittuToimipiste);
                while (resultSet.next()) {
                    int palveluID = resultSet.getInt("palveluID");
                    String palveluNimi = resultSet.getString("nimi");
                    int hinta = resultSet.getInt("hinta");
                    String tyyppi = resultSet.getString("tyyppi");
                    Palvelu palvelu = new Palvelu(palveluID,palveluNimi,hinta,tyyppi);
                    pUusiLista.add(palvelu);
                }
                palveluTableView.getItems().clear();
                palveluTableView.getItems().addAll(pUusiLista);
                palvelutLista.clear();
                palvelutLista.addAll(pUusiLista);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void pLisaaBtn(ActionEvent event) {
        String valittuToimipiste = pToimipisteComboBox.getSelectionModel().getSelectedItem();
        if (valittuToimipiste != null) {
            String nimi = pNimiKentta.getText();
            String tyyppi = "";
            RadioButton valittuRadioButton = (RadioButton) pTyyppiToggleGroup.getSelectedToggle();
            if (valittuRadioButton != null) {
                tyyppi = valittuRadioButton.getText();
            }

            int hinta = 0; // alustetaan hinta muuttuja, jotta se ei ole null

            try {
                hinta = Integer.parseInt(pHintaKentta.getText());
            } catch (NumberFormatException ex) {
                this.pVirheTeksti("Hinta täytyy olla kokonaisluku!");
            }

            int toimipisteID = 0;
            try {
                String query = "SELECT toimipisteID FROM toimipiste WHERE nimi = ?";
                ResultSet resultSet = TietokantaManager.teeKysely(query, valittuToimipiste);
                if (resultSet.next()) {
                    toimipisteID = resultSet.getInt("toimipisteID");
                }
            } catch (SQLException e) {
                System.err.println("Virhe!");
            }

            if (!nimi.isEmpty() && hinta != 0 && !tyyppi.isEmpty()) {
                boolean onnistuiko = this.lisaaUusiPalveluToimipisteeseen(nimi, hinta, tyyppi, toimipisteID, valittuToimipiste);
                if (onnistuiko) {
                    try {
                        ObservableList<Palvelu> pUusiLista2 = FXCollections.observableArrayList();

                        String query = "SELECT * FROM palvelu WHERE toimipisteID = (SELECT toimipisteID FROM toimipiste WHERE nimi = ? LIMIT 1)";
                        ResultSet resultSet = TietokantaManager.teeKysely(query, valittuToimipiste);
                        while (resultSet.next()) {
                            int palveluID = resultSet.getInt("palveluID");
                            String palveluNimi = resultSet.getString("nimi");
                            int hinta2 = resultSet.getInt("hinta");
                            String tyyppi2 = resultSet.getString("tyyppi");
                            Palvelu palvelu2 = new Palvelu(palveluID,palveluNimi,hinta2,tyyppi2);
                            pUusiLista2.add(palvelu2);
                        }
                        palveluTableView.getItems().clear();
                        palveluTableView.getItems().addAll(pUusiLista2);
                        palvelutLista = pUusiLista2;// muutos
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                this.pVirheTeksti("Täytä kaikki kentät ja varmista tiedot");
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Virhe");
            alert.setHeaderText(null);
            alert.setContentText("Valitse toimipiste ennen uuden palvelun luomista.");
            alert.showAndWait();
        }
        this.palveluTableView.refresh();
    }

    private boolean lisaaUusiPalveluToimipisteeseen(String nimi, int hinta, String tyyppi, int toimipisteID, String valittuToimipiste) {

        String sql = "INSERT INTO palvelu (nimi, hinta, tyyppi, toimipisteID) VALUES (?, ?, ?, ?)";
            boolean onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, nimi, hinta, tyyppi, toimipisteID);

            return onnistuiko;
        }

    @FXML
    void pTallennaBtn(ActionEvent event) {
        if (this.valittuPalvelu != null) {
            String nimi = this.pNimiKentta.getText();
            int hinta = Integer.parseInt(this.pHintaKentta.getText());
            String tyyppi = "";
            RadioButton valittuRadioButton = (RadioButton) pTyyppiToggleGroup.getSelectedToggle();
            if (valittuRadioButton != null) {
                tyyppi = valittuRadioButton.getText();
            }
            if (!nimi.isEmpty() && hinta != 0 && !tyyppi.isEmpty()) {
                boolean pOnnistuiko = this.muokkaaPalvelua(this.valittuPalveluID, nimi, hinta, tyyppi);
                if (pOnnistuiko) {
                    this.valittuPalvelu.setNimi(nimi);
                    this.valittuPalvelu.setHinta(hinta);
                    this.valittuPalvelu.setTyyppi(tyyppi);

                    this.palveluTableView.refresh();
                    this.pVirheTeksti("");
                } else {
                    this.pVirheTeksti("Palvelun lisääminen epäonnistui.\nTarkista tiedot.");
                }
            } else {
                this.pVirheTeksti("Tyhjä kenttä. Täytyä kaikki kentät.");
            }
        }
    }
    private boolean muokkaaPalvelua(int palveluID, String nimi, int hinta, String tyyppi){

        String sql = "UPDATE palvelu " + "SET nimi = ?, hinta = ?, tyyppi = ?" + "WHERE palveluID = ?";

        boolean pOnnistuiko = TietokantaManager.teeMuutosoperaatio(sql, nimi, hinta, tyyppi, palveluID);

        return pOnnistuiko;
    }

    @FXML
    void pPoistaBtn(ActionEvent event) {
        if (this.valittuPalvelu != null) {
            boolean pOnnistuiko = this.poistaPalvelu(this.valittuPalveluID);
            if (pOnnistuiko) {
                this.palveluTableView.getItems().remove(this.valittuPalveluIndeksi);
                this.pTyhjennaKentat();
                this.pVirheTeksti("");
            }
        } else {
            this.pVirheTeksti("Palvelua ei ole valittu.");
        }
    }

    private boolean poistaPalvelu(int palveluID) {
        boolean pOnnistuiko = false;
        String pSql = "DELETE FROM palvelu WHERE palveluID = ?";
        Alert pAlert = new Alert(Alert.AlertType.CONFIRMATION);
        pAlert.setContentText("Haluatko varmasti poistaa palvelun?");
        pAlert.setHeaderText("Poista palvelu");
        pAlert.setTitle("Palvelun poisto");

        ButtonType pKyllaButton = new ButtonType("Kyllä");
        ButtonType pEiButton = new ButtonType("Ei");
        pAlert.getButtonTypes().setAll(pKyllaButton, pEiButton);

        Optional<ButtonType> pVastaus = pAlert.showAndWait();
        if (pVastaus.isPresent() && pVastaus.get() == pKyllaButton) {
            pOnnistuiko = TietokantaManager.teeMuutosoperaatio(pSql, palveluID);
        }
        return pOnnistuiko;
    }
}
