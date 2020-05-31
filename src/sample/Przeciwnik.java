package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.Random;
/** Abstrakcyjna klasa Przeciwnik, ktora jest tworzona dla mozliwosci
 * dziedziczenia innych klas.
 */
public abstract class Przeciwnik {

    AnchorPane planszaGry;
    Label tablicaWyniku;
    Label tablicaPrzeciwnikow;
    Label tablicaAmunicji;

    double x;
    double y;
    double szerokosc;
    float szer;
    public Random r = new Random();

    /**
     * {@link #paint()} Funkcja ktora jest tworzona dla mozliwosci
     * dziedziczenia innych klas.
     */
    public void paint(){}

}
