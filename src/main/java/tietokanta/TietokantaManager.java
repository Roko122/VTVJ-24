package tietokanta;

import java.sql.*;

public class TietokantaManager {
    private static final String OSOITE = "jdbc:mariadb://localhost:3306/toimistovarausjarjestelma";
    private static final String KAYTTAJANIMI = Kirjautumistiedot.getKayttajanimi();
    private static final String SALASANA = Kirjautumistiedot.getSalasana();
    private static Connection yhteys;

    //Staattinen metodi tietokantayhteyden muodostamiseen
    public static void yhdista() {
        System.out.println("Muodostetaan yhteyttä tietokantaan...");
        try {
            yhteys = DriverManager.getConnection(OSOITE, KAYTTAJANIMI, SALASANA);
            System.out.println("Yhteys muodostettu.");
        } catch (SQLException se) {
            System.err.println("Yhteyden muodostaminen epäonnistui.");
        }
    }

    //Staattinen metodi tietokantayhteyden katkaisemiseen
    public static void sulje() {
        System.out.println("Suljetaan tietokantayhteys...");
        try {
            if (yhteys != null && !yhteys.isClosed()) {
                yhteys.close();
                System.out.println("Yhteys suljettu.");
            }
        } catch (SQLException se) {
            System.err.println("Yhteyden sulkeminen epäonnistuinen.");
        }
    }

    //Staattinen metodi, joka tekee annetun kyselyn annetuilla parametreilla
    public static ResultSet teeKysely(String sql, Object... parametrit) {
        ResultSet kyselynTulos = null;

        yhdista();
        try {
            PreparedStatement lause = yhteys.prepareStatement(sql);
            asetaParametrit(lause, parametrit);
            kyselynTulos = lause.executeQuery();
        } catch (SQLException se) {
            System.err.println("Kyselyn suoritus epäonnistui.");
        }
        sulje();

        return kyselynTulos;
    }

    //Staattinen metodi, jolla voi tehdä muutosoperaatioita (INSERT, UPDATE, DELETE) annetuilla parametreilla
    public static boolean teeMuutosoperaatio(String sql, Object... parametrit) {
        Boolean onnistuiko = false;

        yhdista();
        try {
            PreparedStatement lause = yhteys.prepareStatement(sql);
            asetaParametrit(lause, parametrit);
            lause.executeUpdate();
            lause.close();
            System.out.println("Muutosoperaatio onnistui.");
            onnistuiko = true;

        } catch (SQLException se) {
            System.err.println("Muutosoperaatio epäonnistui.");
        }
        sulje();

        return onnistuiko;
    }


    //Asettaa annetut parametrit lauseeseen
    public static void asetaParametrit(PreparedStatement lause, Object... parametrit) throws SQLException {
        for (int i = 0; i < parametrit.length; i++) {
            lause.setObject(i + 1, parametrit[i]);
        }
    }

    //Palauttaa yhteyden
    public static Connection getYhteys() {
        return yhteys;
    }
}
