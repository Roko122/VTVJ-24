package toimipiste;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import tietokanta.TietokantaManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ToimipisteTietokanta {
    // Metodi hakee valitun toimipisteen palvelut tietokannasta
    public static ObservableList<String> haeToimipisteenPalvelut(int toimipisteID) {
        ObservableList<String> palvelut = FXCollections.observableArrayList();
        String sql = "SELECT nimi FROM Palvelu WHERE toimipisteID = ?";
        ResultSet tulos = TietokantaManager.teeKysely(sql, toimipisteID);
        try {
            while (tulos.next()) {
                String palvelunNimi = tulos.getString("nimi");
                palvelut.add(palvelunNimi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return palvelut;
    }


    // Hakee kaikkien toimipisteiden tiedot
    protected static ObservableList<Toimipiste> haeKaikkiToimipisteet() {
        ObservableList<Toimipiste> toimipisteet = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Toimipiste";
        ResultSet tulos = TietokantaManager.teeKysely(sql);
        try {
            while (tulos.next()) {
                String nimi = tulos.getString("nimi");
                String osoite = tulos.getString("lahiosoite");
                int id = tulos.getInt("toimipisteID");
                int postinumero = Integer.parseInt(tulos.getString("postinumero"));
                String postitoimipaikka = tulos.getString("postitoimipaikka");
                int kapasiteetti = tulos.getInt("kapasiteetti");
                double vuorokausihinta = tulos.getDouble("vrkhinta");

                toimipisteet.add(new Toimipiste(nimi, osoite, id, postinumero, postitoimipaikka,
                        kapasiteetti, vuorokausihinta));
            }
        } catch (SQLException se) {
            System.err.println("Virhe!");
        }

        return toimipisteet;
    }

    // Luo uuden toimipisteen ja lisää sen tietokantaan
    protected static boolean luoUusiToimipiste(String nimi, String osoite, int postinumero, String postitoimipaikka,
                                               int kapasiteetti, double vuorokausihinta) {

        String sql = "INSERT INTO Toimipiste " +
                "(nimi, lahiosoite, postinumero, postitoimipaikka, kapasiteetti, vrkhinta) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        boolean onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, nimi, osoite, postinumero, postitoimipaikka,
                kapasiteetti, vuorokausihinta);

        return onnistuiko;
    }

    // Hakee uusimman toimipisteen id:n
    protected static int viimeisinToimipisteID() {
        int id = -1;
        String sql = "SELECT MAX(toimipisteID) FROM Toimipiste";
        ResultSet tulos = TietokantaManager.teeKysely(sql);

        try {
            if (tulos.next()) {
                id = tulos.getInt(1);
            }
        } catch (SQLException se) {
            System.err.println("Haku epäonnistui.");
        }
        return id;
    }
    protected static boolean poistaToimipiste(int id) {
        String sql = "DELETE FROM Toimipiste WHERE toimipisteID = ?";
        boolean onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, id);
        return onnistuiko;
    }

    // Päivittää toimipisteen tiedot tietokantaan
    protected static boolean muokkaaToimipisteenTietoja(int id, String nimi, String osoite, int postinumero,
                                                        String postitoimipaikka, int kapasiteetti, double vuorokausihinta) {
        String sql = "UPDATE Toimipiste " +
                "SET nimi = ?, lahiosoite = ?, postinumero = ?, postitoimipaikka = ?, kapasiteetti = ?, vrkhinta = ? " +
                "WHERE toimipisteID = ?";

        boolean onnistuiko = TietokantaManager.teeMuutosoperaatio(sql, nimi, osoite, postinumero, postitoimipaikka,
                kapasiteetti, vuorokausihinta, id);

        return onnistuiko;
    }
}