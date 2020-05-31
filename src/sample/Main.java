package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;

/** klasa glowna Main, z ktorej zaczyna sie program.
 * wczytywanie parametrow z plikow parameetrycznych.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        primaryStage.setTitle(Parametryzacja.nazwaGry);

        primaryStage.setScene(new Scene(root, 281, 400));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {

        //Parametryzacja.wczytajParametry();
        //Parametryzacja.wczytajParametryZSerwera();
        launch(args);

    }
}