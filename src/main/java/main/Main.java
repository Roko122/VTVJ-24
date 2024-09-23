package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        changeScene("main.fxml");
        stage.setTitle("Varausjärjestelmä - Vuokratoimistot OY");
        stage.resizableProperty().set(false);
        stage.show();
    }

    //Staattinen metodi, jota käytetään näkymän vaihtamiseen
    public static void changeScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxml));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
