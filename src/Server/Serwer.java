package Server;


import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;

/** klasa Serwer, ktora umozliwia polaczenie z serwerem.
 * Daje mozliwosc na wysylanie i odebranie najlepszych wynikow z
 * serwera, pobieranie parametrow gry oraz poziomow.
 */
public class Serwer {

    public static String parameter;
    public static String nazwaPliku;
    public static Socket soket;

    /**
     * {@link #main(String[])} ()}
     * Funkcja glowna, main klasy serwera, tworzy ona gniazdo serwera, ktory nasluchuje na zadania od klienta.
     * Nastepnie zaleznie od zadania, wykonuje odpowiednie funkcje
     */
    public static void main(String[] args) throws Exception {

        String fromClient;
        String fromClient2[] = new String[1000];
        ServerSocket serwer = new ServerSocket(4333);


        while (true) {
            
            soket = serwer.accept();

            InputStream is = soket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            fromClient = br.readLine();

            System.out.println(fromClient);

            if (fromClient.contains("GET_PARAMETER:")) {
                fromClient2 = fromClient.split(" ");
                fromClient = "GET_PARAMETER:";
            }
            if (fromClient.contains("GET_LEVEL_PARAMETER:")) {
                fromClient2 = fromClient.split(" ");
                fromClient = "GET_LEVEL_PARAMETER:";

                try {
                    Properties prop = new Properties();
                    InputStream input = null;
                    Reader in = new InputStreamReader(new FileInputStream("src/Server/par.txt"), "UTF-8");
                    prop.load(in);
                    nazwaPliku= prop.getProperty("nazwaBazowaPlikuZOpisemPoziomu")+"."+ prop.getProperty("rozszerzeniePlikuZOpisemPoziomu");
                    in.close();

                } catch (IOException ex) {
                    System.out.println("Nie znaleziono pliku!");
                } catch (NumberFormatException nfe) {
                    System.out.println("Wystapil blad podczas odczytu pliku!");
                }
            }


            switch (fromClient) {
                case "GET_PARAMETER:":
                    for (int i = 1; i < fromClient2.length; i++)
                        getParameter(fromClient2[i]);
                    break;
                case "LOG IN":
                    considerLogin();
                    break;
                case "LOG OUT":
                    considerLogout();
                case "GET_LEVEL_PARAMETER:":
                    for (int i = 1; i < fromClient2.length; i++)
                        getLevelParameter(fromClient2[i]);
                    break;
                case "SEND_RECORDS":
                    fromClient = br.readLine();
                getRecordFromClient(fromClient);
                    break;
                case "GET_RECORDS":
                    sendRecordsToClient();
                    break;
                default:
                    break;
            }

        }

    }

    /**
     * {@link #getParameter(String)} ()}
     * Funkcja majaca argument typu String. Argument ten jest nazwa zmiennej,
     * ktora wyszukiwana jest w pliku parametrycznym oraz jej zawartosc odsylana jest do klienta
     */
    public static void getParameter(String paramaterName) {

        try {
            Properties prop = new Properties();
            InputStream input = null;
            Reader in = new InputStreamReader(new FileInputStream("src/Server/par.txt"), "UTF-8");
            prop.load(in);
            parameter = prop.getProperty(paramaterName);
            in.close();
            System.out.println(parameter);
            OutputStream os = soket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println(parameter);


        } catch (IOException ex) {
            System.out.println("Nie znaleziono pliku!");
        } catch (NumberFormatException nfe) {
            System.out.println("Wystapil blad podczas odczytu pliku!");
        }
    }
    /**
     * {@link #getLevelParameter(String)} ()}
     * Funkcja majaca argument typu String. Argument ten jest nazwa zmiennej,
     * ktora wyszukiwana jest w pliku tekstowym z levelami oraz jej zawartosc odsylana jest do klienta
     */
    public static void getLevelParameter(String paramaterName){

        try {
            Properties prop = new Properties();
            InputStream input = null;
            Reader in = new InputStreamReader(new FileInputStream("src/Server/"+nazwaPliku+""), "UTF-8");
            prop.load(in);
            parameter = prop.getProperty(paramaterName);
            in.close();
            System.out.println(parameter);
            OutputStream os = soket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println(parameter);


        } catch (IOException ex) {
            System.out.println("Nie znaleziono pliku!");
        } catch (NumberFormatException nfe) {
            System.out.println("Wystapil blad podczas odczytu pliku!");
        }
    }

    /**
     * {@link #considerLogin()} ()}
     * Funkcja wysylajaca do klienta wiadomosc tekstowa ze jest polaczony z serwerem
     */
    public static void considerLogin() {
        try {
            OutputStream os = soket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("YOU ARE CONNECTED");
        } catch (IOException ex) {
            System.out.println("Nie udalo sie polaczyc z hostem");
        }
    }

    /**
     * {@link #considerLogout()} ()} ()}
     * Funkcja wysylajaca do klienta wiadomosc tekstowa ze zostal on rozlaczony z serwerem
     */
    public static void considerLogout() {
        try {
            OutputStream os = soket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("YOU ARE DISCONNECTED");
        } catch (IOException ex) {
            System.out.println("Nie udalo sie polaczyc z hostem");
        }
    }

    /**
     * {@link #getRecordFromClient(String)} ()}
     * Funkcja odbierajaca od klienta linie z wynikiem gracza oraz zapisujaca ja do pliku z rekordami
     */
    public static void getRecordFromClient(String x){

        try {
            FileWriter writer = new FileWriter("src/Server/rekordy2.txt",true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.newLine();
            bufferWriter.write(x);
            bufferWriter.close();
            writer.close();
        } catch (IOException ioe) {
            System.out.println("Nie udalo sie zapisac wyniku do pliku");
        }
    }

    /**
     * {@link #sendRecordsToClient()} ()}
     * Funkcja wysylajaca wszystkie wyniki zapisane w pliku tekstowym z wynikami.
     * Wyniki te wysylane sa w jednej lini tekstu, odesparowane sa spacjami i @
     */
    public static void sendRecordsToClient(){

        try {
            OutputStream os = soket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            File file = new File("src/Server/rekordy2.txt");
            Scanner sc = new Scanner(file);

            String records = "";
            while(sc.hasNextLine())
            {
            records = records+sc.nextLine()+"@";
            }
            System.out.println(records);
            pw.println(records);
            //soket.close();
        }
        catch(IOException io){
            System.out.println("Nie udalo sie polaczyc z hostem");
        }
    }

}
