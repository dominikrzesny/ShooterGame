package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
/** klasa Bonus, ktora odpowiada za naliczenie dodatkowych punktow albo amunicji,
 *  w zaleznisci od losowania. Zachowuje sie jako objekt dziedziciacy po klasie Przeciwnik.
 */
public class Bonus extends Przeciwnik {

    Bonus(AnchorPane planszaGry, Label tablicaWyniku, Label tablicaPrzeciwnikow, Label tablicaAmunicji, float szer){
        this.planszaGry = planszaGry;
        this.tablicaWyniku = tablicaWyniku;
        this.tablicaPrzeciwnikow = tablicaPrzeciwnikow;
        this.tablicaAmunicji = tablicaAmunicji;
        this.szer = szer;
    }

    /**
     * {@link #paint()} Funkcja odpowiadajaca za rysowanie objektu Bonus.
     * Wyliczenie szerokosci obiektu i umieszczenie jego na plansze gry.
     * Sprawdzenie warunku na dodanie tego obiektu wewnatrz planszy (zeby ten
     * objekt nie wyszedl za granice naszej planszy). Przydzielenie objektowi
     * zdarzenia klikniecia.
     */
    @Override
    public void paint(){
        try{

            szerokosc = Parametryzacja.poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy*Parametryzacja.poczatkowaSzerokoscPlanszy/100*szer* (planszaGry.getHeight()/Parametryzacja.poczatkowaWysokoscPlanszy)*
                    (planszaGry.getWidth()/Parametryzacja.poczatkowaSzerokoscPlanszy);
        y = r.nextDouble() * (planszaGry.getHeight()-szerokosc);
        x = r.nextDouble() * (planszaGry.getWidth()-szerokosc);

        Image przeciwnik = new Image(new FileInputStream("bonus.png"));
        Rectangle bonus = new Rectangle(x, y, szerokosc, szerokosc);
        bonus.setFill(new ImagePattern(przeciwnik));
        planszaGry.getChildren().add(bonus);

        EventHandler<MouseEvent> MouseHandler1 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                bonus.setVisible(false);
                int i;
                Parametryzacja.amunicja-=1;
                i=r.nextInt(2);
                if(i==1){
                    Parametryzacja.punkty+=r.nextInt(15)+1;
                    tablicaWyniku.setText(Integer.toString(Parametryzacja.punkty));}
                else{
                    Parametryzacja.amunicja+=r.nextInt(5)+1;
                    tablicaAmunicji.setText(Integer.toString(Parametryzacja.amunicja));
                }

            }
        };

        bonus.addEventHandler(MouseEvent.MOUSE_CLICKED, MouseHandler1);
    }
        catch(FileNotFoundException ex){
            System.out.println("Problem z wczytaniem obiektu gry");
        }

    }
}
