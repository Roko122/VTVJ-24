package asiakas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import tietokanta.TietokantaManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AsiakasTietokanta {

    //Hakee kaikkien asiakkaiden tiedot
    public static ObservableList<Asiakas> haeKaikkiAsiakkaat() {
        ObservableList<Asiakas> asiakkaat = FXCollections.observableArrayList();
        String sql = "SELECT * " +
                "FROM asiakas";
        ResultSet tulos = TietokantaManager.teeKysely(sql);
        try {
            while (tulos.next() == true) {
                int asiakasID = tulos.getInt("asiakasID");
                String etunimi = tulos.getString("etunimi");
                String sukunimi = tulos.getString("sukunimi");
                String sahkopostiosoite = tulos.getString("sahkopostiosoite");
                String puhelinnumero = tulos.getString("puhelinnumero");
                String lahiosoite = tulos.getString("lahiosoite");
                String postinumero = tulos.getString("postinumero");
                String postitoimipaikka = tulos.getString("postitoimipaikka");

                asiakkaat.add(new Asiakas(asiakasID, etunimi, sukunimi, sahkopostiosoite, puhelinnumero, lahiosoite,
                        postinumero, postitoimipaikka));
            }
        } catch (SQLException se) {
            System.err.println("Virhe!");
        }

        return asiakkaat;
    }


    //Luo uuden asiakkaan ja lisää sen tietokantaan
    protected static boolean luoUusiAsiakas(String etunimi, String sukunimi, String sahkopostiosoite, String puhelinnumero,
                                   String lahiosoite, String postinumero, String postitoimipaikka) {

        String sql = "INSERT INTO asiakas " +
                "(etunimi, sukunimi, sahkopostiosoite, lahiosoite, postitoimipaikka, postinumero, puhelinnumero) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, etunimi, sukunimi, sahkopostiosoite, lahiosoite,
                postitoimipaikka, postinumero, puhelinnumero);

        return onnistuiko;
    }


    //Hakee uusimman asiakkaan asiakasID:n
    protected static int viimeisinAsiakasID() {
        int asiakasID = -1;
        String sql = "SELECT MAX(asiakasID) FROM asiakas";
        ResultSet tulos = TietokantaManager.teeKysely(sql);

        try {
            if (tulos.next() == true) {
                asiakasID = tulos.getInt(1);
            }
        } catch (SQLException se) {
            System.err.println("Haku epäonnistui.");
        }
        return asiakasID;
    }


    //Päivittää asiakastietueen tiedot tietokantaan
    protected static boolean muokkaaAsiakkaanTietoja(int asiakasID, String etunimi, String sukunimi, String sahkopostiosoite,
                                            String puhelinnumero, String lahiosoite, String postinumero, String postitoimipaikka) {
        String sql = "UPDATE asiakas " +
                "SET etunimi = ?, sukunimi = ?, sahkopostiosoite = ?, lahiosoite = ?, postitoimipaikka = ?, postinumero = ?, puhelinnumero = ? " +
                "WHERE asiakasID = ?";

        boolean onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, etunimi, sukunimi, sahkopostiosoite, lahiosoite,
                postitoimipaikka, postinumero, puhelinnumero, asiakasID);

        return onnistuiko;
    }


    //Poistaa asiakastietueen tietokannasta
    protected static boolean poistaAsiakas(int asiakasID) {
        boolean onnistuiko = false;
        String sql = "DELETE FROM asiakas " +
                "WHERE asiakasID = ?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Haluatko varmasti poistaa asiakkaan?");
        alert.setHeaderText("Poista asiakas");
        alert.setTitle("Asiakkaan poisto");

        ButtonType btKylla = new ButtonType("Kyllä");
        ButtonType btEn = new ButtonType("En");
        alert.getButtonTypes().setAll(btKylla, btEn);

        Optional<ButtonType> vastaus = alert.showAndWait();
        if (vastaus.isPresent() && vastaus.get() == btKylla) {
            onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, asiakasID);
        }

        return onnistuiko;
    }

}
