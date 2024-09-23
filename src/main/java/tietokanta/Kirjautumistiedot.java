package tietokanta;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Kirjautumistiedot {
    private static final String KIRJAUTUMISTIEDOT = "tietokanta.properties";

    public static String getKayttajanimi() {
        Properties properties = new Properties();
        try (FileInputStream lukija = new FileInputStream(KIRJAUTUMISTIEDOT)) {
            properties.load(lukija);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("user");
    }

    public static String getSalasana() {
        Properties properties = new Properties();
        try (FileInputStream lukija = new FileInputStream(KIRJAUTUMISTIEDOT)) {
            properties.load(lukija);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("password");
    }
}
