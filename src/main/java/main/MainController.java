package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private Button btAsiakkaat;

    @FXML
    private Button btPalvelut;

    @FXML
    private Button btToimipisteet;

    @FXML
    private Button btVaraukset;

    @FXML
    private Label lbVersio;

    @FXML
    private Label lbVuokratoimistot;

    @FXML
    private VBox paneeliPainikkeet;

    @FXML
    private Separator viiva;

    @FXML
    void naytaAsiakkaatSivu(ActionEvent event) {
        Main.changeScene("asiakas.fxml");
    }

    @FXML
    void naytaPalvelutSivu(ActionEvent event) {
        Main.changeScene("palvelu.fxml");
    }

    @FXML
    void naytaToimipisteetSivu(ActionEvent event) {
        Main.changeScene("toimipiste.fxml");
    }

    @FXML
    void naytaVarauksetSivu(ActionEvent event) {
        Main.changeScene("varaus.fxml");
    }

}
