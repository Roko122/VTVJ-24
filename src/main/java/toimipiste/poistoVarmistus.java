package toimipiste;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class poistoVarmistus {

    public static boolean poistoVarmistus(String toimipisteenNimi) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Toimipisteen poisto");
        alert.setHeaderText(null);
        alert.setContentText("Haluatko varmasti poistaa toimipisteen " + toimipisteenNimi + " tiedot?");

        ButtonType buttonTypeYes = new ButtonType("Kyllä");
        ButtonType buttonTypeNo = new ButtonType("Ei");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }
}
