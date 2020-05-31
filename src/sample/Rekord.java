package sample;
/** Klasa Rekord, ktora jest tworzona dla mozliwosci
 * porownywania rekordow przy pomocy interfejsu Comparable i metody compareTo .
 */
public class Rekord implements Comparable<Rekord> {
    public String name ;
    public Integer rekord;
    public Integer level;

    public Rekord(String name, Integer rekord, Integer level) {
        this.name = name;
        this.rekord = rekord;
        this.level = level;
    }

    public int compareTo(Rekord r)
    {
        return r.rekord.compareTo(rekord);
    }
}
