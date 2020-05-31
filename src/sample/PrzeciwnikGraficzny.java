package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
/** klasa PrzeciwnikGraficzny, ktora odpowiada za naliczenie dodatkowych punktow albo amunicji,
 *  w zaleznisci od losowania. Zachowuje sie jako objekt dziedziciacy po klasie Przeciwnik.
 */
public class PrzeciwnikGraficzny extends Przeciwnik {

    double promien;


    PrzeciwnikGraficzny(AnchorPane planszaGry, Label tablicaWyniku, Label tablicaPrzeciwnikow,Label tablicaAmunicji, float szer){
        this.planszaGry = planszaGry;
        this.tablicaWyniku = tablicaWyniku;
        this.tablicaPrzeciwnikow = tablicaPrzeciwnikow;
        this.tablicaAmunicji = tablicaAmunicji;
        this.szer = szer;
    }
    /**
     * {@link #paint()} Funkcja odpowiadajaca za rysowanie objektu PrzeciwnikGraficzny.
     * Wyliczenie szerokosci obiektu i umieszczenie jego na plansze gry.
     * Sprawdzenie warunku na dodanie tego obiektu wewnatrz planszy (zeby ten
     * objekt nie wyszedl za granice naszej planszy). Przydzielenie objektowi
     * zdarzenia klikniecia.
     */
    @Override
    public void paint() {
        try {

            planszaGry.getChildren().clear();
            szerokosc = Parametryzacja.poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy*Parametryzacja.poczatkowaSzerokoscPlanszy/100*szer*
                    (planszaGry.getHeight()/Parametryzacja.poczatkowaWysokoscPlanszy)*(planszaGry.getWidth()/Parametryzacja.poczatkowaSzerokoscPlanszy);
            y = szerokosc/2 + r.nextDouble() * (planszaGry.getHeight()-szerokosc);
            promien = szerokosc/2;
            x = szerokosc/2 + r.nextDouble() * (planszaGry.getWidth()-szerokosc);

            Image przeciwnik = new Image(new FileInputStream(Parametryzacja.plikObiektu));
            Circle kolko = new Circle(x,y,promien);
            kolko.setFill(new ImagePattern(przeciwnik));
            planszaGry.getChildren().add(kolko);

            EventHandler<MouseEvent> MouseHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent)
                {
                    kolko.setVisible(false);
                    Parametryzacja.punkty+=Parametryzacja.parametryPoziomow[Parametryzacja.aktualnyLevel][3];
                    tablicaWyniku.setText(Integer.toString(Parametryzacja.punkty));
                    Parametryzacja.przeciwnicyDoZestrzelenia-=1;
                    tablicaPrzeciwnikow.setText(Integer.toString(Parametryzacja.przeciwnicyDoZestrzelenia));
                    Parametryzacja.amunicja-=1;
                    if(Parametryzacja.amunicja>=0) {
                        tablicaAmunicji.setText(Integer.toString(Parametryzacja.amunicja));
                    }
                    if(Parametryzacja.amunicja<0){
                        Parametryzacja.amunicja=0;
                        tablicaAmunicji.setText(Integer.toString(Parametryzacja.amunicja));
                    }
                }
            };

            kolko.addEventHandler(MouseEvent.MOUSE_CLICKED, MouseHandler);

        }
        catch(FileNotFoundException ex){
            System.out.println("Problem z wczytaniem obiektu gry");
        }
    }
}
