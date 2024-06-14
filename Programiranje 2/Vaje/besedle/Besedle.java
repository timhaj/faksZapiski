package besedle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Besedle {

    // Konstante barv
    static final int BELA = 0;
    static final int CRNA = 1;
    static final int RUMENA = 2;
    static final int ZELENA = 3;

    // ANSI ukazi (za barvni izpis)
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_GREEN_BG = "\u001b[42m";
    static final String ANSI_YELLOW_BG = "\u001b[43m";
    static final String ANSI_WHITE_BG = "\u001b[47;1m";
    static final String ANSI_BLACK_BG = "\u001b[40m";
    static final String ANSI_WHITE = "\u001b[37m";
    static final String ANSI_BLACK = "\u001b[30m";

    static final String abeceda = "ABCČDEFGHIJKLMNOPRSŠTUVZŽ"; // Veljavne črke
    static final int MAX_POSKUSOV = 6; // Število poskusov

    static String[] seznamBesed; // Seznam vseh možnih besed
    static String iskanaBeseda; // Iskana beseda trenutne igre
    static int[] barveAbecede; // Barve črk pri izpisu abecede

    static Scanner sc = new Scanner(System.in);

    // Izpiše znak v izbrani barvi
    static void izpisiZBarvo(char znak, int barva) {
        String slog;
        if (barva == ZELENA) {
            slog = ANSI_BLACK + ANSI_GREEN_BG;
        } else if (barva == RUMENA) {
            slog = ANSI_BLACK + ANSI_YELLOW_BG;
        } else if (barva == BELA) {
            slog = ANSI_BLACK + ANSI_WHITE_BG;
        } else {
            slog = ANSI_WHITE + ANSI_BLACK_BG;
        }
        System.out.print(slog + " " + znak + " " + ANSI_RESET);
    }

    // Prebere seznam besed iz datoteke
    static void preberiBesede(String datoteka) throws FileNotFoundException {
        try{
            Scanner scx = new Scanner(new File(datoteka));
            int st_besed = scx.nextInt();
            seznamBesed = new String[st_besed];
            scx.nextLine();
            for(int i = 0;scx.hasNextLine();i++){
                seznamBesed[i] = scx.nextLine().toUpperCase();
            }
            scx.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    // Pripravi vse za novo igro
    static void novaIgra() {
        // TODO: implementiraj
        Random rnd = new Random();
        int random = rnd.nextInt(seznamBesed.length);
        iskanaBeseda = seznamBesed[random];
        barveAbecede = new int[abeceda.length()];
        for(int i = 0;i<abeceda.length();i++){
            barveAbecede[i] = 0;
        }
    }

    // Izpiše abecedo
    static void izpisiAbecedo() {
        // TODO: implementiraj
        for(int i = 0;i<abeceda.length();i++){
            izpisiZBarvo(abeceda.charAt(i), barveAbecede[i]);
        }
        System.out.println();
    }

    // Ali je beseda veljavna?
    static boolean veljavnaBeseda(String beseda) {
        // TODO: implementiraj
        if(beseda.length() != 5){
            System.out.println("Nepravilna dolžina besede!");
            return false;
        }
        boolean flag = false;
        for(int i = 0;i<beseda.length();i++){
            if(!abeceda.contains(Character.toString(beseda.charAt(i)))){
                System.out.println("V besedi so neveljavni znaki!");
                return false;
            }
        }
        return true;
    }

    // Določi barve črk v ugibani besedi
    static int[] pobarvajBesedo(String ugibanaBeseda) {
        // TODO: implementiraj
        int arr[] = new int[5];
        for(int i = 0;i<ugibanaBeseda.length();i++){
            if(ugibanaBeseda.charAt(i) == iskanaBeseda.charAt(i)){
                arr[i] = ZELENA;
                barveAbecede[abeceda.indexOf(ugibanaBeseda.charAt(i))] = ZELENA;
            }
            else if(iskanaBeseda.contains(Character.toString(ugibanaBeseda.charAt(i))) && ugibanaBeseda.charAt(i) != iskanaBeseda.charAt(i)){
                arr[i] = RUMENA;
                if(!(barveAbecede[abeceda.indexOf(ugibanaBeseda.charAt(i))] == ZELENA)){
                    barveAbecede[abeceda.indexOf(ugibanaBeseda.charAt(i))] = RUMENA;
                }
            }
            else if(!(iskanaBeseda.contains(Character.toString(ugibanaBeseda.charAt(i))))){
                arr[i] = BELA;
                barveAbecede[abeceda.indexOf(ugibanaBeseda.charAt(i))] = CRNA;
            }
        }
        return arr;
    }

    // Izpiši besedo
    static void izpisiBesedo(String ugibanaBeseda, int[] barve) {
        // TODO: implementiraj
        for(int i = 0;i<ugibanaBeseda.length();i++){
            izpisiZBarvo(ugibanaBeseda.charAt(i), barve[i]);
        }
        System.out.println();
    }


    // Izvede eno igro
    static void igra() {
        novaIgra();
        int poskus = 1;
        boolean uganil = false;
        while (poskus <= MAX_POSKUSOV) {
            izpisiAbecedo();
            System.out.printf("Poskus %d/%d: ", poskus, MAX_POSKUSOV);
            String ugibanaBeseda = sc.nextLine().toUpperCase();

            // Preveri veljavnost
            if (!veljavnaBeseda(ugibanaBeseda))
                continue;

            // Pobarvaj crke v besedi (namigi)
            int[] barve = pobarvajBesedo(ugibanaBeseda);

            // Izpiši pobarvano besedo
            izpisiBesedo(ugibanaBeseda, barve);

            if (ugibanaBeseda.equals(iskanaBeseda)) {
                uganil = true;
                break;
            }
            poskus++;
        }

        if (uganil) {
            System.out.printf("Bravo! Besedo si uganil/a v %d poskusih.\n", poskus);
        } else {
            System.out.printf("Žal ti ni uspelo. Pravilna beseda: %s\n", iskanaBeseda);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        preberiBesede("viri/besede.txt");
        while (true) {
            igra();
            System.out.print("Nova igra? (d/n): ");
            String odg = sc.nextLine();
            if (odg.toLowerCase().charAt(0) != 'd') {
                break;
            }
        }
    }
}