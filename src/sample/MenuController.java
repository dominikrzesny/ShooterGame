package sample;

import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;

import javafx.scene.layout.Pane;


/** klasa oblugujaca plik menu.fmxl, obsulujaca zdarzenia po kliknieciu na przyciski w menu gry.
 * Nadaje tez menu odpowiedni wyglad*/
public class MenuController {

    @FXML
    public Pane mainPane;
    @FXML
    private Button przyciskStart;
    @FXML
    private Button przyciskRekordy;
    @FXML
    private Button przyciskWyjdzZGry;
    @FXML
    public ComboBox<String> comboBoxStopniTrudnosci;
    @FXML
    public int mozliwoscStartu;
    @FXML
    public TextField poleNaNick;
    @FXML
    public ImageView obrazekMenu;
    @FXML
    public Button przyciskInfo;

    /**
     * {@link #initialize()} Funkcja wywolujaca sie przy wlaczaniu gry,
     * ustawia tlo menu w zaleznosci od parametryzacji, dodaje tytul menu
     * oraz ustawia mozliwosci wyboru poziomow trudnsoci
     */
    public void initialize()
    {
        polaczZSerwerem();
        if(Parametryzacja.statusSerwera==0)
            Parametryzacja.wczytajParametry();

        if(Parametryzacja.statusSerwera==1)
        Parametryzacja.wczytajParametryZSerwera();
        Parametryzacja.wczytajParametryLeveliZSerwera();


        if(Parametryzacja.tlo.equals("jednolite"))
        mainPane.setStyle("-fx-background-color: rgb("+Parametryzacja.kolorTla[0]+","+Parametryzacja.kolorTla[1]+","+Parametryzacja.kolorTla[2]+")");
        else{
            mainPane.setStyle("-fx-background-color: rgb("+100+","+189+","+214+")");
        }

        try {
            Image obrazek = new Image(new FileInputStream("menu.jpg"));
            obrazekMenu.setImage(obrazek);
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Nie znaleziono pliku");
        }


        ObservableList<String> lista = FXCollections.observableArrayList();

        for(int i=1;i<=Parametryzacja.liczbaStopniTrudnosci;i++)
            {
                lista.add(""+i);
            }
            comboBoxStopniTrudnosci.setItems(lista);


    }

    /**
     * {@link #wyswietlRekordy()} Funkcja otwierajaca okno z
     * najlepszymi wynikami w grze
     */
    public void wyswietlRekordy(){

        Parent rekordy;
        try {
            rekordy = FXMLLoader.load(getClass().getResource("rekordy.fxml"));
            Stage scenaRekordow= new Stage();
            scenaRekordow.setTitle("Najlepsze wyniki");
            scenaRekordow.setScene(new Scene(rekordy, 500, 400));
            scenaRekordow.show();
            // Hide this current window (if this is what you want)
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException i) {
            i.printStackTrace();
        }

    }

    /**
     * {@link #wyjdzZgry()} ()} Funkcja zamykajaca gre
     */
    public void wyjdzZgry()
    {
        System.exit(0);
    }

    /**
     * {@link #wyborStopniaTrudnosci()} Funkcja sprawdzajaca czy gracz wybral poziom trudnosci,
     * ustawia parametr ktory pozniej bedzie warunkiem startu gry.
     */
    public void wyborStopniaTrudnosci()
    {
        mozliwoscStartu = 1;
    }

    /**
     * {@link #wystartujGre()} Funkcja otwierajaca okno z plansza gry.
     * Aby zostalo otwarte musza byc spelnione dwa warunki(wybrany poziom trudnosci
     * oraz wpisany nick), jesli nie to zostanie wyswietlony komunikat. Funkcja ta zapisuje do zmiennych klasy Parametryzacja nick
     * i wybrany poziom trudnosci.
     */
    public void wystartujGre()
    {

        if (mozliwoscStartu==1 && !poleNaNick.getText().isEmpty())
        {
            System.out.println("Wystartowano GRE!");
            Parametryzacja.nick = poleNaNick.getText();
            Parametryzacja.poziomTrudosci = Integer.parseInt(comboBoxStopniTrudnosci.getValue());


            Parent gra;
            try {
                gra = FXMLLoader.load(getClass().getResource("gra.fxml"));
                Stage planszaGry = new Stage();
                planszaGry.setTitle(Parametryzacja.nazwaGry);
                planszaGry.setScene(new Scene(gra));
                planszaGry.setHeight(Parametryzacja.poczatkowaWysokoscPlanszy);
                planszaGry.setWidth(Parametryzacja.poczatkowaSzerokoscPlanszy);
                planszaGry.show();

            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {
            Alert dg = new Alert(Alert.AlertType.WARNING);
            dg.setTitle("Warning");
            dg.setContentText("Wpisz nick i wybierz poziom trudnosci aby wystartowac!");
            dg.setHeaderText(null);
           // dg.setGraphic(new ImageView(this.getClass().getResource("1.jpg").toString()));
            dg.show();
           // System.out.println("Wpisz nick i wybierz poziom trudnosci aby wystartowac!");
        }
    }

    /**
     * {@link #wyswietlInfo()} Funckja otwierajaca komunikat ze wskazowkami do gry, oraz jej zasadami.
     */
    public void wyswietlInfo()
    {
        Alert dg = new Alert(Alert.AlertType.INFORMATION);
        dg.setTitle("Info");
        dg.setContentText("Wycwicz swoj refleks");
        dg.setHeaderText("Gra Hunter Killer");
        dg.show();
    }

    /*public void pobraniePlikuZSerwera(){

        try {


            File plik = new File("src/Server/rekordy2.txt");
            byte[] b = new byte[(int)plik.length()];
            Socket soket = new Socket("localhost", 4333);
            OutputStream os = soket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("GET_RECORDS");
            InputStream is = soket.getInputStream();
            FileOutputStream fr = new FileOutputStream("rekordy.txt");
            is.read(b,0,b.length);
            fr.write(b,0,b.length);
        }
        catch(FileNotFoundException f){
            System.out.println("Nie udalo sie przeniesc pliku");
        }
        catch (UnknownHostException h){
            System.out.println("Nie udalo sie nawiazac polaczenia z hostem");
        }
        catch(IOException e){
            System.out.println("Nie udalo sie nawiazac polaczenia");
        }

    }*/

    /**
     * {@link #polaczZSerwerem()}
     * Funkcja wysylajaca zadanie polaczenia z serwerem oraz nasluchuje jego odpowiedzi
     */
    public void polaczZSerwerem() {
        try {
            Socket soket = new Socket("localhost", 4333);
            OutputStream os = soket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            InputStream is = soket.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            pw.println("LOG IN");
            String odpowiedzSerwera = br.readLine();
            System.out.println(odpowiedzSerwera);
            if(odpowiedzSerwera.equals("YOU ARE CONNECTED")){
                Parametryzacja.statusSerwera=1;
                System.out.println("Grasz online");
            }
            else{
                System.out.println("Grasz offline");
            }

        } catch (UnknownHostException h) {
            System.out.println("Nie udalo sie nawiazac polaczenia z hostem");
        } catch (IOException e) {
            System.out.println("Grasz offline");
        }
    }
}