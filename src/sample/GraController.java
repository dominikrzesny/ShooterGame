package sample;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.Properties;

/** klasa oblugujaca plik gra.fmxl. Glowna klasa gry. Nadaje odpowiedni wyglad i rozmiar planszy gry,
 *  tutaj rozgrywa sie gra, odliczanie czasu i aktualizowanie tablicy z aktualnym stanem gry.
*/
public class GraController extends Canvas {

    public boolean Rozgrywka = false;
    public Rectangle prostokat;

    @FXML
    public SplitPane panel;
    @FXML
    public AnchorPane planszaGry;
    @FXML
    public AnchorPane panelInfo;
    @FXML
    public Label tablicaWyniku;
    @FXML
    public Label tablicaAmunicji;
    @FXML
    public Label tablicaPrzeciwnikow;
    @FXML
    public Label tablicaCzasu;
    @FXML
    public Label tablicaLevelu;
    @FXML
    public Label gratulacje;
    @FXML
    public Button przyciskStartu;
    @FXML
    public Button awans;
    @FXML
    public Button pause;
    @FXML
    public Label fps;


    public Random r = new Random();
    public int randTime;
    public Timeline timeLine;
    public Timeline timeLine2;
    public int czasLevelu;
    public int czyOdejmowac;
    public double l = 1;
    public double d = 1;
    /**
     * {@link #initialize()} Funkcja wywolujaca sie przy wyswietlaniu okna,
     * ustawia tlo okna w zaleznosci od parametryzacji, celownik, poczatkowe parametry gry,
     * a takze zdarzenie wykonujace sie gdy klikamy na plansze gry.
     */
    public void initialize() throws Exception {

        skalowanie();

        przyciskStartu.setVisible(true);
        Parametryzacja.aktualnyLevel=0;
        Parametryzacja.punkty = 0;
        Image image = new Image(new FileInputStream("celownik.png"));
        planszaGry.setCursor(new ImageCursor(image,image.getWidth()/2,image.getHeight()/2));

        if(Parametryzacja.tlo.equals("jednolite")) {
            panel.setStyle("-fx-background-color: rgb("+Parametryzacja.kolorTla[0]+","+Parametryzacja.kolorTla[1]+","+Parametryzacja.kolorTla[2]+")");
        }
        if(Parametryzacja.tlo.equals("plikGraficzny")) {
            try{
            Image image2 = new Image(new FileInputStream(Parametryzacja.plikTla));
            BackgroundSize backgroundSize = new BackgroundSize(planszaGry.getWidth(), planszaGry.getHeight(), true, true, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(image2, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
            Background background = new Background(backgroundImage);
            panel.setBackground(background);}
            catch(FileNotFoundException ex)
            {
                System.out.println("Nie znaleziono pliku tla");
            }
        }

        planszaGry.setOnMouseClicked(e -> {
            if(czyOdejmowac==1){
                if(e.getTarget().equals(planszaGry))
                {
                    Parametryzacja.punkty+=Parametryzacja.parametryPoziomow[Parametryzacja.aktualnyLevel][4];

                    if(Parametryzacja.punkty>=0) {
                        tablicaWyniku.setText(Integer.toString(Parametryzacja.punkty));                    }
                    if(Parametryzacja.punkty<0){
                        Parametryzacja.punkty=0;
                        tablicaWyniku.setText(Integer.toString(Parametryzacja.punkty));                    }

                    Parametryzacja.amunicja-=1;
                    if(Parametryzacja.amunicja>=0) {
                        tablicaAmunicji.setText(Integer.toString(Parametryzacja.amunicja));
                    }
                    if(Parametryzacja.amunicja<0){
                        Parametryzacja.amunicja=0;
                        tablicaAmunicji.setText(Integer.toString(Parametryzacja.amunicja));
                    }
                }
            }
        });

    }


    /**
     * {@link #run()} Funkcja startujaca mechanike gry, wlacza stoper odmierzajacy czas gry,
     * ustawia parametry gry zgodne z aktualnym levelem. Funkcja ta opisuje takze warunki ukonczenia levelu,
     * awansu oraz porazki.
     */
    public void run()
    {

                zliczajCzas();
                czyOdejmowac=1;
                tablicaWyniku.setText(Integer.toString(Parametryzacja.punkty));
                tablicaLevelu.setText(Integer.toString(Parametryzacja.aktualnyLevel+Parametryzacja.numeracjaPoziomowZaczynaSieOd));
                Parametryzacja.przeciwnicyDoZestrzelenia=Parametryzacja.parametryPoziomow[Parametryzacja.aktualnyLevel][2];
                tablicaPrzeciwnikow.setText(Integer.toString(Parametryzacja.przeciwnicyDoZestrzelenia));
                Parametryzacja.amunicja = Parametryzacja.parametryPoziomow[Parametryzacja.aktualnyLevel][1];
                tablicaAmunicji.setText(Integer.toString(Parametryzacja.amunicja));


                timeLine = new Timeline(new KeyFrame(Duration.millis(czasWidocznosciObiektu()), actionEvent -> {
                   // timeLine = new Timeline(new KeyFrame(Duration.millis(20), actionEvent -> {

                    dodajPrzeciwnika();


                    if (Parametryzacja.przeciwnicyDoZestrzelenia == 0) {

                        czyOdejmowac=0;
                        timeLine.stop();
                        planszaGry.getChildren().clear();
                        planszaGry.getChildren().add(awans);
                        planszaGry.getChildren().add(gratulacje);
                        awans.setAlignment(Pos.CENTER);
                        awans.setText("Wystartuj poziom " + Integer.toString(Parametryzacja.aktualnyLevel+Parametryzacja.numeracjaPoziomowZaczynaSieOd+1));
                        awans.setVisible(true);
                        gratulacje.setVisible(true);
                        Parametryzacja.aktualnyLevel+=1;
                    }

                    if (Parametryzacja.przeciwnicyDoZestrzelenia == 0 && Parametryzacja.aktualnyLevel==(Parametryzacja.liczbaPoziomow)) {
                        czyOdejmowac=0;
                        timeLine.stop();
                        planszaGry.getChildren().clear();
                        planszaGry.getChildren().add(gratulacje);
                        gratulacje.setAlignment(Pos.CENTER);
                        gratulacje.setText("PRZESZEDLES CALA GRE");
                        gratulacje.setVisible(true);
                        if(Parametryzacja.statusSerwera==0){
                            zapiszNajlepszeWyniki();
                        }
                        if(Parametryzacja.statusSerwera==1) {
                            wyslanieWynikuDoSerwera();
                        }


                    }

                    if((czasLevelu==0 && Parametryzacja.przeciwnicyDoZestrzelenia>0) || (Parametryzacja.amunicja<=0 && Parametryzacja.przeciwnicyDoZestrzelenia!=0)){
                        czyOdejmowac=0;
                        timeLine.stop();
                        planszaGry.getChildren().clear();
                        planszaGry.getChildren().add(gratulacje);
                        gratulacje.setAlignment(Pos.CENTER);
                        gratulacje.setText("Porazka!");
                        gratulacje.setVisible(true);
                        if(Parametryzacja.statusSerwera==0){
                            zapiszNajlepszeWyniki();
                        }
                        if(Parametryzacja.statusSerwera==1) {
                            wyslanieWynikuDoSerwera();
                        }
                    }



                    //czasLevelu-=1;
                    //tablicaCzasu.setText(Integer.toString(czasLevelu));

                }));
                timeLine.setCycleCount(Animation.INDEFINITE);
                timeLine.play();

    }


    public void zapiszNajlepszeWyniki()
    {

        //FileWriter pw = null;
        try {
            FileWriter writer = new FileWriter("rekordy.txt",true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            //bufferWriter.write(Parametryzacja.nick + " " + Parametryzacja.punkty + " pkt. " + Parametryzacja.poziomTrudosci + " poziom trudosci");
            bufferWriter.newLine();
            bufferWriter.write(Parametryzacja.nick + " " + Parametryzacja.punkty + " " + Parametryzacja.poziomTrudosci + " ");
            //bufferWriter.newLine();
            bufferWriter.close();
        } catch (IOException ioe) {
            System.out.println("Nie udalo sie otworzyc pliku parametrycznego ");
        }



    }

    /**
     * {@link #awans()} Funkcja uaktywniana za pomoca ptzycisku.
     * Startuje nastepny level oraz chowa przyciski.
     */
    public void awans()
    {

        //planszaGry.getChildren().clear();
        awans.setVisible(false);
        gratulacje.setVisible(false);
        run();

    }

    /**
     * {@link #zliczajCzas()} Funkcja odliczajaca czas gry oraz uaktualniajaca jego wartosc,
     * ktory jest ustawiany w zaleznosci od parametryzacji.
     */
    public void zliczajCzas()
    {

        czasLevelu = Parametryzacja.czasGry;
        tablicaCzasu.setText(Integer.toString(czasLevelu));
        timeLine2 = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            czasLevelu-=1;
            tablicaCzasu.setText(Integer.toString(czasLevelu));

            if((czasLevelu==0 && Parametryzacja.przeciwnicyDoZestrzelenia>0)|| (Parametryzacja.amunicja<=0 && Parametryzacja.przeciwnicyDoZestrzelenia>0 )){
                czyOdejmowac=0;
                timeLine2.stop();
                planszaGry.getChildren().clear();
                planszaGry.getChildren().add(gratulacje);
                gratulacje.setAlignment(Pos.CENTER);
                gratulacje.setText("Porazka!");
                gratulacje.setVisible(true);
                }
            if(Parametryzacja.przeciwnicyDoZestrzelenia==0){timeLine2.stop();}
            Fps fps1= new Fps();
            fps.setText(Double.toString(fps1.setFps()));
        }));
        timeLine2.setCycleCount(Animation.INDEFINITE);
        timeLine2.play();

    }



    /**
     * {@link #dodajPrzeciwnika()} Funkcja rysujaca obiekt do zestrzelenia, w zaleznosci od parametryzacji
     * moga byc one prostokatem, trojkatem, kwadratem, kolem,a takze obiektem graficznym.
     * Funkcja ta rysuje takze bonusowy obiekt z szansa 10% co pojawienie sie przeciwnikow.
     */
    public void dodajPrzeciwnika(){

        l =1;

        switch(Parametryzacja.numerFigury) {
            case 0:
                PrzeciwnikGraficzny pg = new PrzeciwnikGraficzny(planszaGry,tablicaWyniku,tablicaPrzeciwnikow,tablicaAmunicji, szerkoscObiektu());
                pg.paint();
                break;

            case 1:
                Prostokat p = new Prostokat(planszaGry,tablicaWyniku,tablicaPrzeciwnikow,tablicaAmunicji, szerkoscObiektu());
                p.paint();
                break;

            case 2:
                Kwadrat kw = new Kwadrat(planszaGry,tablicaWyniku,tablicaPrzeciwnikow,tablicaAmunicji, szerkoscObiektu());
                kw.paint();
                break;

            case 3:
                Trojkat t = new Trojkat(planszaGry,tablicaWyniku,tablicaPrzeciwnikow,tablicaAmunicji, szerkoscObiektu());
                t.paint();
                break;

            case 4:
                Kolko k = new Kolko(planszaGry,tablicaWyniku,tablicaPrzeciwnikow,tablicaAmunicji, szerkoscObiektu());
                k.paint();
                break;

            default:
                System.out.println("Nie ma zadnego przeciwnika");
                break;
        }

        randTime = r.nextInt(10);
        if(randTime==0)
        {
            Bonus b = new Bonus(planszaGry,tablicaWyniku,tablicaPrzeciwnikow,tablicaAmunicji, szerkoscObiektu());
            b.paint();

        }

    }

    /**
     * {@link #czasWidocznosciObiektu()} Funkcja zwracajaca czas na ile pojawiaja
     * sie obiekty w milisekundach w zaleznosci od stopnia trudnosci.
     */
    public int czasWidocznosciObiektu() {
        float x = 1;
        float z = (float)Parametryzacja.zmianaStopniaTrudnosci/100;
        for (int i = 1; i < Parametryzacja.poziomTrudosci; i++) {
            x *=(1 - z);
        }

        int y = (int)(Parametryzacja.czasWidocznosciObiektu*x);
        System.out.println(Parametryzacja.poziomTrudosci);
        return y;
    }


    /**
     * {@link #szerkoscObiektu()} Funkcja wyliczajaca ulamek szerokosci ekranu jaki musi miec
     * obiekt do zestrzelenia zaleznosci od stopnia trudnosci.
     */
    public float szerkoscObiektu(){
        float x = 1;
        float z = (float)Parametryzacja.zmianaStopniaTrudnosci/100;
        for (int i = 1; i < Parametryzacja.poziomTrudosci; i++) {
            x *=(1 - z);
        }
        return x;
    }

    /**
     * {@link #potwierdzenieGotowosci()} Funkcja wywolujaca sie po kliknieciu na przycisk gotowosci,
     * startujaca gre oraz chowajaca przyciski z planszy gry
     */
    public void potwierdzenieGotowosci(){
        przyciskStartu.setVisible(false);
        run();
        System.out.println("Zaczelismy");

    }

    public void skalowanie(){

        planszaGry.widthProperty().addListener(new ChangeListener<>() {


            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {

                if((double)oldSceneWidth==0.0){
                    oldSceneWidth =(double)newSceneWidth;
                }

                l = l*(double)newSceneWidth/(double)oldSceneWidth;


                for( Node tab : planszaGry.getChildren()) {
                    System.out.println(tab.getClass());

                    tab.setScaleX(l*d);
                    tab.setScaleY(l*d);

                    if(tab.getClass().toString().equals("class javafx.scene.control.Button")||
                            tab.getClass().toString().equals("class javafx.scene.control.Label")||
                            tab.getClass().toString().equals("class javafx.scene.shape.Polygon"))
                    {
                        double nowyLayout = tab.getLayoutX()+((double)newSceneWidth-(double)oldSceneWidth)/2;
                        tab.setLayoutX(nowyLayout);
                    }

                    if(tab.getClass().toString().equals("class javafx.scene.shape.Rectangle"))
                    {
                        Rectangle r = (Rectangle)tab;
                        double nowyLayout = r.getX()*((double)newSceneWidth/(double)oldSceneWidth);
                        r.setX(nowyLayout);
                    }

                    if(tab.getClass().toString().equals("class javafx.scene.shape.Circle"))
                    {
                        Circle c = (Circle)tab;
                        double nowyLayout = c.getCenterX()*((double)newSceneWidth/(double)oldSceneWidth);
                        c.setCenterX(nowyLayout);
                    }

                    if(tab.getClass().toString().equals("class javafx.scene.shape.Polygon"))
                    {
                        Polygon p = (Polygon)tab;
                        double nowyLayout = p.getPoints().get(0)*((double)newSceneWidth/(double)oldSceneWidth);
                    }

                }
            }
        });

        planszaGry.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {


                if ((double) oldSceneHeight == 0.0) {
                    oldSceneHeight = (double)newSceneHeight;
                }

                d = d*(double)newSceneHeight/(double)oldSceneHeight;

                for( Node tab : planszaGry.getChildren()) {

                    tab.setScaleX(l*d);
                    tab.setScaleY(l*d);

                    if(tab.getClass().toString().equals("class javafx.scene.control.Button")||
                            tab.getClass().toString().equals("class javafx.scene.control.Label")||
                            tab.getClass().toString().equals("class javafx.scene.shape.Polygon"))
                    {
                        double nowyLayout = tab.getLayoutY()+((double)newSceneHeight-(double)oldSceneHeight)/2;
                        tab.setLayoutY(nowyLayout);
                    }

                    if(tab.getClass().toString().equals("class javafx.scene.shape.Circle"))
                    {
                        Circle c = (Circle)tab;
                        double nowyLayout = c.getCenterY()*((double)newSceneHeight/(double)oldSceneHeight);
                        c.setCenterY(nowyLayout);
                    }

                    if(tab.getClass().toString().equals("class javafx.scene.shape.Rectangle"))
                    {
                        Rectangle r = (Rectangle)tab;
                        double nowyLayout = r.getY()*((double)newSceneHeight/(double)oldSceneHeight);
                        r.setY(nowyLayout);
                    }

                }

            }
        });

    }

    /**
     * {@link #wyslanieWynikuDoSerwera()}
     * Funkcja wysylajaca wynik na aktywny serwer po zakonczeniu gry, nick uzytkownika i na jakim poziomie gra
     */
    public void wyslanieWynikuDoSerwera(){

        try {

            Socket soket = new Socket("localhost", 4333);
            OutputStream os1 = soket.getOutputStream();
            PrintWriter pw1 = new PrintWriter(os1, true);
            pw1.println("SEND_RECORDS");
            pw1.println(Parametryzacja.nick + " " + (Parametryzacja.punkty) + " " + Parametryzacja.poziomTrudosci + " ");
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

    }

    boolean a = true;
    public void pause(){
        if(a)
        {
            planszaGry.setDisable(true);
            a=false;
            timeLine.stop();
            timeLine2.stop();
        }
        else
        {
            planszaGry.setDisable(false);
            a=true;
            timeLine.play();
            timeLine2.play();
        }
    }





}
