package varaus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import tietokanta.TietokantaManager;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class VarausTietokanta {

    //Hakee varausten tiedot
    protected static ObservableList<Varaus> haeVaraukset() {
        ObservableList<Varaus> varaukset = FXCollections.observableArrayList();
        String sql = "SELECT varausID, alkupvm, loppupvm, varausaika, " +
                "(SELECT nimi FROM Toimipiste WHERE Toimipiste.toimipisteID = Varaus.toimipisteID) AS toimipiste, " +
                "(SELECT sukunimi FROM Asiakas WHERE Asiakas.asiakasID = Varaus.asiakasID) AS sukunimi " +
                "FROM varaus";
        ResultSet tulos = TietokantaManager.teeKysely(sql);
        try {
            while (tulos.next() == true) {
                int varausID = tulos.getInt("varausID");
                String asiakkaanSukunimi = tulos.getString("sukunimi");
                String toimipiste = tulos.getString("toimipiste");
                Date alkupvm = tulos.getDate("alkupvm");
                Date loppupvm = tulos.getDate("loppupvm");
                Timestamp varausaika = tulos.getTimestamp("varausaika");

                varaukset.add(new Varaus(varausID, asiakkaanSukunimi, toimipiste, alkupvm, loppupvm, varausaika));
            }
        } catch (SQLException se) {
            System.err.println("Virhe!");
        }

        return varaukset;
    }


    //Lisää uuden varauksen tietokantaan
    protected static boolean lisaaVaraus(Date alkupvm, Date loppupvm, Timestamp varausaika,
                                         int toimipisteID, int asiakasID, List<String> valitutPalvelut) {

        String sql = "INSERT INTO varaus " +
                "(alkupvm, loppupvm, varausaika, toimipisteID, asiakasID) " +
                "VALUES (?, ?, ?, ?, ?)";
        int varausID = -1;
        boolean onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, alkupvm, loppupvm,
                varausaika, toimipisteID, asiakasID);

        if (onnistuiko) {
            varausID = viimeisinVarausID(); // Hae viimeisin varausID
            if (varausID != -1) {
                for (String palveluNimi : valitutPalvelut) {
                    // Hae palvelun ID sen nimen perusteella tietokannasta
                    int palveluID = haePalvelunID(palveluNimi);
                    if (palveluID != -1) {
                        boolean onnistuikoPalvelu = lisaaVarausPalvelu(varausID, palveluID);
                    }
                }
            }
        }
        return onnistuiko;
        }
    private static int haePalvelunID(String palveluNimi) {
        String sql = "SELECT palveluID FROM palvelu WHERE nimi = ?";
        ResultSet tulos = TietokantaManager.teeKysely(sql, palveluNimi);
        try {
            if (tulos.next()) {
                return tulos.getInt("palveluID");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return -1;
    }

    private static boolean lisaaVarausPalvelu(int varausID, int palveluID) {
        String varausPalveluSql = "INSERT INTO varauksenpalvelut (varausID, palveluID) VALUES (?, ?)";
        return TietokantaManager.teeMuutosoperaatio(varausPalveluSql, varausID, palveluID);
    }

    //Hakee uusimman varauksen varausID:n
    protected static int viimeisinVarausID() {
        int varausID = -1;
        String sql = "SELECT MAX(varausID) FROM varaus";
        ResultSet tulos = TietokantaManager.teeKysely(sql);

        try {
            if (tulos.next() == true) {
                varausID = tulos.getInt(1);
            }
        } catch (SQLException se) {
            System.err.println("Haku epäonnistui.");
        }
        return varausID;
    }

    //Päivittää varauksen tiedot tietokantaan
    protected static boolean muokkaaVarauksenTietoja(int varausID, Date alkupvm, Date loppupvm, int toimipisteID, int asiakasID, List<String> valitutPalvelut) {
        String sql = "UPDATE varaus " +
                "SET alkupvm = ?, loppupvm = ?, varausaika = ?, toimipisteID = ?, asiakasID = ? " +
                "WHERE varausID = ?";

        boolean onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, alkupvm, loppupvm,
                new Timestamp(System.currentTimeMillis()), toimipisteID, asiakasID, varausID);
        if (onnistuiko) {
            boolean palvelutPoistettu = poistaVarauksenPalvelut(varausID);
            if (palvelutPoistettu) {
                if (varausID != -1) {
                    for (String palveluNimi : valitutPalvelut) {
                        // Hae palvelun ID sen nimen perusteella tietokannasta
                        int palveluID = haePalvelunID(palveluNimi);
                        if (palveluID != -1) {
                            lisaaVarausPalvelu(varausID, palveluID);
                        }
                    }
                }
            }
        }

        return onnistuiko;
    }

    private static boolean poistaVarauksenPalvelut(int varausID) {
        String sql = "DELETE FROM VarauksenPalvelut " +
                "WHERE varausID = ?";

        boolean onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, varausID);

        return onnistuiko;
    }

    //Poistaa varauksen tietokannasta
    protected static boolean poistaVaraus(int varausID) {
        boolean onnistuiko = false;
        String sql = "DELETE FROM varaus " +
                "WHERE varausID = ?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Haluatko varmasti poistaa varauksen?");
        alert.setHeaderText("Poista varaus");
        alert.setTitle("Varauksen poisto");

        ButtonType btKylla = new ButtonType("Kyllä");
        ButtonType btEn = new ButtonType("En");
        alert.getButtonTypes().setAll(btKylla, btEn);

        Optional<ButtonType> vastaus = alert.showAndWait();
        if (vastaus.isPresent() && vastaus.get() == btKylla) {
            onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, varausID);
        }

        return onnistuiko;
    }
}
