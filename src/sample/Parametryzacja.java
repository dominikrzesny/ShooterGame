package sample;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.lang.String;

/** klasa statyczna do wczytytwania parametrow z dwoch plikow tekstowych gg xd*/
public class Parametryzacja
{


    static String nazwaGry;
    public static int liczbaPoziomow;
    public static int numeracjaPoziomowZaczynaSieOd;
    static int liczbaStopniTrudnosci;
    static int  zmianaStopniaTrudnosci;
    static int poczatkowaSzerokoscPlanszy;
    static int poczatkowaWysokoscPlanszy;
    static double poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy;

    static String rozszerzeniePlikuZOpisemPoziomu;
    static String nazwaBazowaPlikuZOpisemPoziomu;
    static String plikzOpisemPoziomow;

    static String tlo;
    static int[] kolorTla = new int[3];
    static String[] kolorTla1 = new String[3];
    static String plikTla;

    static String obiektyGry;
    static String figuraObiektuGry;
    static int numerFigury;
    static String plikObiektu;


    static int czasGry;
    static int liczbaNajlepszychWynikow;
    static int czasWidocznosciObiektu;

    static int poziomTrudosci;
    static String nick;
    static int punkty = 0;

    static String[][] parametryPoziomow1 = new String[liczbaPoziomow][5];
    static int[][] parametryPoziomow = new int[liczbaPoziomow][5];
    static int aktualnyLevel = 0;
    static int przeciwnicyDoZestrzelenia;
    static int amunicja;
    public static int statusSerwera = 0;
    static String[][] tablicaRekordow = new String[liczbaNajlepszychWynikow][2];
    static String test;
    static String test2;
    static String [] parametryPoziomu1 = new String[5];
    static String [] parametryPoziomu2 = new String[5];
    static String [] parametryPoziomu3 = new String[5];
    static String [] parametryPoziomu4 = new String[5];
    static String [] parametryPoziomu5 = new String[5];
    static String [] parametryPoziomu6 = new String[5];
    static String [] parametryPoziomu7 = new String[5];

    /**
     * {@link #wczytajParametry()} Funkcja wczytujaca
     * z 2 plikow parametrycznych, zapisujaca parametry do statycznych zmiennych,
     * tak by mozna bylo miec do nich dostep w kazdym miejscu programu.
     */
    public static void  wczytajParametry()
    {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            Reader in = new InputStreamReader(new FileInputStream("par.txt"),"UTF-8");
            // load a properties file
            prop.load(in);
            // get the property value and print it out

            nazwaGry = prop.getProperty("nazwaGry");
            System.out.println(nazwaGry);

            liczbaPoziomow = Integer.parseInt(prop.getProperty("liczbaPoziomow"));
            System.out.println(liczbaPoziomow);

            numeracjaPoziomowZaczynaSieOd = Integer.parseInt(prop.getProperty("numeracjaPoziomowZaczynaSieOd"));
            System.out.println(numeracjaPoziomowZaczynaSieOd);

            liczbaStopniTrudnosci = Integer.parseInt(prop.getProperty("liczbaStopniTrudnosci"));
            System.out.println(liczbaStopniTrudnosci);

            zmianaStopniaTrudnosci = Integer.parseInt(prop.getProperty("zmianaStopniaTrudnosci"));
            System.out.println(zmianaStopniaTrudnosci);

            poczatkowaSzerokoscPlanszy = Integer.parseInt(prop.getProperty("poczatkowaSzerokoscPlanszy"));
            System.out.println(poczatkowaSzerokoscPlanszy);

            poczatkowaWysokoscPlanszy = Integer.parseInt(prop.getProperty("poczatkowaWysokoscPlanszy"));
            System.out.println(poczatkowaWysokoscPlanszy);

            poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy = Float.parseFloat(prop.getProperty("poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy"));
            System.out.println(poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy);

            rozszerzeniePlikuZOpisemPoziomu = prop.getProperty("rozszerzeniePlikuZOpisemPoziomu");
            System.out.println(rozszerzeniePlikuZOpisemPoziomu);

            nazwaBazowaPlikuZOpisemPoziomu = prop.getProperty("nazwaBazowaPlikuZOpisemPoziomu");
            System.out.println(nazwaBazowaPlikuZOpisemPoziomu);

            plikzOpisemPoziomow = nazwaBazowaPlikuZOpisemPoziomu + "." + rozszerzeniePlikuZOpisemPoziomu;
            System.out.println(plikzOpisemPoziomow);

            tlo = prop.getProperty("tlo");
            System.out.println(tlo);


            if(tlo.equals("jednolite"))
            {
                kolorTla1 = (prop.getProperty("kolorTla").split(" "));
                for (int i = 0; i < kolorTla1.length; i++)
                {
                    kolorTla[i] = Integer.parseInt(kolorTla1[i]);
                }
                System.out.println(kolorTla[0] + " " + kolorTla[1] + " " + kolorTla[2]);
            }
            else
            {
                plikTla = prop.getProperty("plikTla");
                System.out.println(plikTla);
            }


            obiektyGry = prop.getProperty("obiektyGry");
            System.out.println(obiektyGry);

            if(obiektyGry.equals("figuryGeometryczne"))
            {
                figuraObiektuGry = prop.getProperty("figuraObiektuGry");
                if(figuraObiektuGry.equals("prostokaty")){numerFigury=1;}
                if(figuraObiektuGry.equals("kwadraty")){numerFigury=2;}
                if(figuraObiektuGry.equals("trojkaty")){numerFigury=3;}
                if(figuraObiektuGry.equals("kolka")){numerFigury=4;}
                System.out.println(figuraObiektuGry);
            }
            else
            {
                plikObiektu = prop.getProperty("plikObiektu");
                numerFigury=0;
                System.out.println(plikObiektu);
            }

            in.close();

            //Odzczytywanie drugiego pliku z naszymi parametrami
            Reader in2 = new InputStreamReader(new FileInputStream(plikzOpisemPoziomow),"UTF-8");
            prop.load(in2);

            czasGry = Integer.parseInt(prop.getProperty("czasGry"));
            System.out.println(czasGry);

            liczbaNajlepszychWynikow = Integer.parseInt(prop.getProperty("liczbaNajlepszychWynikow"));
            System.out.println(liczbaNajlepszychWynikow);

            czasWidocznosciObiektu = Integer.parseInt(prop.getProperty("czasWidocznosciObiektu"));
            System.out.println(czasWidocznosciObiektu);


            parametryPoziomow1 = new String[liczbaPoziomow][5];
            parametryPoziomow = new int[liczbaPoziomow][5];

            for(int i=0;i<liczbaPoziomow;i++)
            {
                parametryPoziomow1[i] = (prop.getProperty("parametryPoziomu"+(i+1)).split(" "));
            }

            for (int i = 0; i < liczbaPoziomow; i++)
            {
                for(int j=0;j<5;j++)
                {
                    parametryPoziomow[i][j] = Integer.parseInt(parametryPoziomow1[i][j]);
                    System.out.print(parametryPoziomow[i][j]+" ");
                }
                System.out.println();
            }
            System.out.println(parametryPoziomow[0][3]);
            in2.close();


        }
        catch (IOException ex)
        {
            System.out.println("Nie znaleziono pliku!");
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("Wystapil blad podczas odczytu pliku!");
        }
        catch (NullPointerException npe)
        {
            System.out.println("Wystapil blad podczas odczytu pliku!");
        }

        finally
        {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Wystapil blad podczas odczytu pliku!");
                }
            }
        }

    }

    /**
     * {@link #wczytajParametryZSerwera()} Funkcja zczytujaca parametry z serwera, zapisuje je do zmiennych
     */
    public static void wczytajParametryZSerwera()
    {
        try {
            Socket soket = new Socket("localhost", 4333);
            OutputStream os = soket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            InputStream is = soket.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            pw.println("GET_PARAMETER: nazwaGry" + " " +
                    "liczbaPoziomow" + " " +
                    "numeracjaPoziomowZaczynaSieOd"+ " " +
                    "liczbaStopniTrudnosci"+ " " +
                    "zmianaStopniaTrudnosci"+ " " +
                    "poczatkowaSzerokoscPlanszy"+ " " +
                    "poczatkowaWysokoscPlanszy"+ " " +
                    "poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy"+ " " +
                    "rozszerzeniePlikuZOpisemPoziomu"+ " " +
                    "nazwaBazowaPlikuZOpisemPoziomu"+ " " +
                    "tlo"+ " " +
                    "kolorTla"+ " " +
                    "plikTla"+ " " +
                    "obiektyGry"+ " " +
                    "figuraObiektuGry"+ " " +
                    "plikObiektu");

            nazwaGry = br.readLine();
            System.out.println(nazwaGry);

            liczbaPoziomow = Integer.parseInt(br.readLine());
            System.out.println(liczbaPoziomow);

            numeracjaPoziomowZaczynaSieOd = Integer.parseInt(br.readLine());
            System.out.println(numeracjaPoziomowZaczynaSieOd);

            liczbaStopniTrudnosci = Integer.parseInt(br.readLine());
            System.out.println(liczbaStopniTrudnosci);

            zmianaStopniaTrudnosci = Integer.parseInt(br.readLine());
            System.out.println(zmianaStopniaTrudnosci);

            poczatkowaSzerokoscPlanszy = Integer.parseInt(br.readLine());
            System.out.println(poczatkowaSzerokoscPlanszy);

            poczatkowaWysokoscPlanszy = Integer.parseInt(br.readLine());
            System.out.println(poczatkowaWysokoscPlanszy);

            poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy = Float.parseFloat(br.readLine());
            System.out.println(poczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy);

            rozszerzeniePlikuZOpisemPoziomu = br.readLine();
            System.out.println(rozszerzeniePlikuZOpisemPoziomu);

            nazwaBazowaPlikuZOpisemPoziomu = br.readLine();
            System.out.println(nazwaBazowaPlikuZOpisemPoziomu);

            plikzOpisemPoziomow = nazwaBazowaPlikuZOpisemPoziomu + "." + rozszerzeniePlikuZOpisemPoziomu;
            System.out.println(plikzOpisemPoziomow);

            tlo = br.readLine();
            System.out.println(tlo);

            kolorTla1 = (br.readLine().split(" "));

            plikTla = br.readLine();
            System.out.println(plikTla);

            if(tlo.equals("jednolite"))
            {

                for (int i = 0; i < kolorTla1.length; i++)
                {
                    kolorTla[i] = Integer.parseInt(kolorTla1[i]);
                }
                System.out.println(kolorTla[0] + " " + kolorTla[1] + " " + kolorTla[2]);
            }

            obiektyGry = br.readLine();
            System.out.println(obiektyGry);

            figuraObiektuGry = br.readLine();
            System.out.println(figuraObiektuGry);

            plikObiektu = br.readLine();
            System.out.println(plikObiektu);

            if(obiektyGry.equals("figuryGeometryczne"))
            {
                if(figuraObiektuGry.equals("prostokaty")){numerFigury=1;}
                if(figuraObiektuGry.equals("kwadraty")){numerFigury=2;}
                if(figuraObiektuGry.equals("trojkaty")){numerFigury=3;}
                if(figuraObiektuGry.equals("kolka")){numerFigury=4;}
            }
            else
            {
                numerFigury=0;
            }


        }
        catch (UnknownHostException h){
            System.out.println("Nie udalo sie nawiazac polaczenia z hostem");
        }
        catch(IOException e){
            System.out.println("Grasz offline");
        }
    }

    /**
     * {@link #wczytajParametryLeveliZSerwera()} Funkcja zczytujaca parametry leveli z serwera, zapisuje je do zmiennych
     */
    public static void wczytajParametryLeveliZSerwera() {
        try {
            Socket soket = new Socket("localhost", 4333);
            OutputStream os = soket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            InputStream is = soket.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            if(liczbaPoziomow==2){
            pw.println("GET_LEVEL_PARAMETER: czasGry" + " " +
                    "liczbaNajlepszychWynikow" + " " +
                    "czasWidocznosciObiektu"+ " " +
                    "parametryPoziomu1"+ " " +
                    "parametryPoziomu2");
            }
            if(liczbaPoziomow==3){
                pw.println("GET_LEVEL_PARAMETER: czasGry" + " " +
                        "liczbaNajlepszychWynikow" + " " +
                        "czasWidocznosciObiektu"+ " " +
                        "parametryPoziomu1"+ " " +
                        "parametryPoziomu2"+ " " +
                        "parametryPoziomu3");
            }
            if(liczbaPoziomow==4){
                pw.println("GET_LEVEL_PARAMETER: czasGry" + " " +
                        "liczbaNajlepszychWynikow" + " " +
                        "czasWidocznosciObiektu"+ " " +
                        "parametryPoziomu1"+ " " +
                        "parametryPoziomu2"+ " " +
                        "parametryPoziomu3"+ " " +
                        "parametryPoziomu4");
            }
            if(liczbaPoziomow==5){
                pw.println("GET_LEVEL_PARAMETER: czasGry" + " " +
                        "liczbaNajlepszychWynikow" + " " +
                        "czasWidocznosciObiektu"+ " " +
                        "parametryPoziomu1"+ " " +
                        "parametryPoziomu2"+ " " +
                        "parametryPoziomu3"+ " " +
                        "parametryPoziomu4"+ " " +
                        "parametryPoziomu5");
            }
            if(liczbaPoziomow==6){
                pw.println("GET_LEVEL_PARAMETER: czasGry" + " " +
                        "liczbaNajlepszychWynikow" + " " +
                        "czasWidocznosciObiektu"+ " " +
                        "parametryPoziomu1"+ " " +
                        "parametryPoziomu2"+ " " +
                        "parametryPoziomu3"+ " " +
                        "parametryPoziomu4"+ " " +
                        "parametryPoziomu5"+ " " +
                        "parametryPoziomu6");
            }
            if(liczbaPoziomow==7){
                pw.println("GET_LEVEL_PARAMETER: czasGry" + " " +
                        "liczbaNajlepszychWynikow" + " " +
                        "czasWidocznosciObiektu"+ " " +
                        "parametryPoziomu1"+ " " +
                        "parametryPoziomu2"+ " " +
                        "parametryPoziomu3"+ " " +
                        "parametryPoziomu4"+ " " +
                        "parametryPoziomu5"+ " " +
                        "parametryPoziomu6"+ " " +
                        "parametryPoziomu7");
            }

            czasGry = Integer.parseInt(br.readLine());
            System.out.println(czasGry);

            liczbaNajlepszychWynikow = Integer.parseInt(br.readLine());
            System.out.println(liczbaNajlepszychWynikow);

            czasWidocznosciObiektu = Integer.parseInt(br.readLine());
            System.out.println(czasWidocznosciObiektu);

            parametryPoziomow1 = new String[liczbaPoziomow][5];
            parametryPoziomow = new int[liczbaPoziomow][5];


            for(int i=0;i<liczbaPoziomow;i++)
            {
                parametryPoziomow1[i] = br.readLine().split(" ");
            }

            for (int i = 0; i < liczbaPoziomow; i++)
            {
                for(int j=0;j<5;j++)
                {
                    parametryPoziomow[i][j] = Integer.parseInt(parametryPoziomow1[i][j]);
                    System.out.print(parametryPoziomow[i][j]+" ");
                }
                System.out.println();
            }


        } catch (IOException e) {
            System.out.println("Grasz offline");
        }

    }
}