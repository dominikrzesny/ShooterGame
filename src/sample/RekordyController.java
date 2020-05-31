package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.io.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.util.ArrayList;
import java.lang.*;
import java.util.*;

/** klasa oblugujaca plik rekordy.fmxl.
 * Nadaje odpowiedni wyglad okna oraz wypisuje na ekran liste najlepszych wynikow*/
public class RekordyController {

    @FXML
    public Pane panelRekordow;
    @FXML
    public ListView<String> listaWynikow;


    /**
     * {@link #initialize()} Funkcja wywolujaca sie przy wyswietlaniu okna,
     * ustawia tlo okna w zaleznosci od parametryzacji, wyswietla liste najlepszych wynikow.
     * Jezeli serwer jest online, pobierane sa one z serwera
     */
    public void initialize() {


        if (Parametryzacja.tlo.equals("jednolite")) {
            panelRekordow.setStyle("-fx-background-color: rgb(" + Parametryzacja.kolorTla[0] + "," + Parametryzacja.kolorTla[1] + "," + Parametryzacja.kolorTla[2] + ")");
        }
        if (Parametryzacja.tlo.equals("plikGraficzny")) {
            try {
                Image image2 = new Image(new FileInputStream(Parametryzacja.plikTla));
                BackgroundSize backgroundSize = new BackgroundSize(panelRekordow.getWidth(), panelRekordow.getHeight(), true, true, true, false);
                BackgroundImage backgroundImage = new BackgroundImage(image2, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                Background background = new Background(backgroundImage);
                panelRekordow.setBackground(background);
            } catch (FileNotFoundException ex) {
                System.out.println("Nie znaleziono pliku tla");
            }
        }

        /////////////////////////////////////////////////////////////////////////////////////////////

        if (Parametryzacja.statusSerwera == 0){

            try {
                ObservableList<String> lines = FXCollections.observableArrayList();
                File file = new File("rekordy.txt");
                Scanner sc = new Scanner(file);
                List<Rekord> rekordy = new LinkedList<>();
                while (sc.hasNext()) {
                    rekordy.add(new Rekord(sc.next(), sc.nextInt(), sc.nextInt()));
                }
                Collections.sort(rekordy);

                if (Parametryzacja.liczbaNajlepszychWynikow < rekordy.size()) {
                    for (int i = 0; i < Parametryzacja.liczbaNajlepszychWynikow; i++) {
                        lines.add(rekordy.get(i).name + " " + rekordy.get(i).rekord + " pkt. " + rekordy.get(i).level + " stopien trudnosci");
                    }
                } else {
                    for (int i = 0; i < rekordy.size(); i++) {
                        lines.add(rekordy.get(i).name + " " + rekordy.get(i).rekord + " pkt. " + rekordy.get(i).level + " stopien trudnosci");
                    }
                }

                listaWynikow.setItems(lines);

            } catch (FileNotFoundException ex) {
                System.out.println("Nie znaleziono pliku!");
            }
        }

        if (Parametryzacja.statusSerwera == 1){

            try {
                ObservableList<String> lines = FXCollections.observableArrayList();
                List<Rekord> rekordy = new LinkedList<>();

                Socket soket = new Socket("localhost", 4333);

                OutputStream os = soket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                pw.println("GET_RECORDS");

                InputStream is = soket.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));



                    String recordLine = br.readLine();
                    System.out.println(recordLine);
                    String x[];
                    x = recordLine.split("@");
                for(int i=0;i<x.length;i++){
                    String y[];
                    y = x[i].split(" ");
                    rekordy.add(new Rekord(y[0], Integer.parseInt(y[1]), Integer.parseInt(y[2])));
                }


                Collections.sort(rekordy);

                if (Parametryzacja.liczbaNajlepszychWynikow < rekordy.size()) {
                    for (int i = 0; i < Parametryzacja.liczbaNajlepszychWynikow; i++) {
                        lines.add(rekordy.get(i).name + " " + rekordy.get(i).rekord + " pkt. " + rekordy.get(i).level + " stopien trudnosci");
                    }
                } else {
                    for (int i = 0; i < rekordy.size(); i++) {
                        lines.add(rekordy.get(i).name + " " + rekordy.get(i).rekord + " pkt. " + rekordy.get(i).level + " stopien trudnosci");
                    }
                }

                listaWynikow.setItems(lines);

            }
            catch (FileNotFoundException ex) {
                System.out.println("Nie znaleziono pliku!");
            }
            catch (IOException io){
                System.out.println("Nie polaczono z serwerem");
            }
        }

    }

}


