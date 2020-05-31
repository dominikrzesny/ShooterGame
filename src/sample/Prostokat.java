package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
/** klasa Prostokat, ktora odpowiada za naliczenie dodatkowych punktow albo amunicji,
 *  w zaleznisci od losowania. Zachowuje sie jako objekt dziedziciacy po klasie Przeciwnik.
 */
public class Prostokat extends Przeciwnik {

    public double dlugosc;

    Prostokat(AnchorPane planszaGry, Label tablicaWyniku, Label tablicaPrzeciwnikow,Label tablicaAmunicji, float szer){
        this.planszaGry = planszaGry;
        this.tablicaWyniku = tablicaWyniku;
        this.tablicaPrzeciwnikow = tablicaPrzeciwnikow;
        this.tablicaAmunicji = tablicaAmunicji;
        this.szer = szer;
    }

    /**
     * {@link #paint()} Funkcja odpowiadajaca za rysowanie objektu Prostokat.
     * Wyliczenie szerokosci obiektu i umieszczenie jego na plansze gry.
     * Sprawdzenie warunku na dodanie tego obiektu wewnatrz planszy (zeby ten
     * objekt nie wyszedl za granice naszej planszy). Przydzielenie objektowi
     * zdarzenia klikniecia.
     */
    @Override
    public void paint(){

        planszaGry.getChildren().clear();
        szerokosc = Parametryzacja.poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy*Parametryzacja.poczatkowaSzerokoscPlanszy/100*szer*(planszaGry.getHeight()/Parametryzacja.poczatkowaWysokoscPlanszy)*
                (planszaGry.getWidth()/Parametryzacja.poczatkowaSzerokoscPlanszy);
        dlugosc = szerokosc*2/3;
        y = r.nextDouble() * (planszaGry.getHeight()-dlugosc);
        x = r.nextDouble() * (planszaGry.getWidth()-szerokosc);

        Rectangle prostokat = new Rectangle(x, y, szerokosc, dlugosc);
        planszaGry.getChildren().add(prostokat);

        EventHandler<MouseEvent> MouseHandler1 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent)
            {

                Parametryzacja.punkty+=Parametryzacja.parametryPoziomow[Parametryzacja.aktualnyLevel][3];
                prostokat.setVisible(false);
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

        prostokat.addEventHandler(MouseEvent.MOUSE_CLICKED, MouseHandler1);
    }
}
