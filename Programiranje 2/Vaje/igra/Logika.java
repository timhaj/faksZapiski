package igra;

import java.util.Random;

public class Logika {

    private static int polja[][];
    private static int tocke;

    private static boolean konec;

    private static void rotiraj() {
        // najprej transponiramo tabelo - zamenjamo stolpce in vrstice
        for (int i = 0; i < polja.length; i++) {
            for (int j = i + 1; j < polja.length; j++) {
                int tmp = polja[i][j];
                polja[i][j] = polja[j][i];
                polja[j][i] = tmp;
            }
        }
        // če rotiramo v desno (v smeri urinega kazalca), obrnemo še vrstni red stolpcev
        for (int i = 0; i < polja.length; i++) {
            for (int j = 0; j < polja.length / 2; j++) {
                int tmp = polja[i][j];
                polja[i][j] = polja[i][polja.length - 1 - j];
                polja[i][polja.length - 1 - j] = tmp;
            }
        }
    }

    public static void dodajNovoPloscico(){
        Random rnd = new Random();
        for(;;){
            int n = rnd.nextInt(10) + 1;
            int x = rnd.nextInt(polja.length);
            int y = rnd.nextInt(polja.length);
            if(vrniPloscico(x,y) == 0){
                if(n == 1){
                    polja[x][y] = 4;
                    break;
                }
                else{
                    polja[x][y] = 2;
                    break;
                }
            }
        }
    }

    public static void zacniNovoIgro(int velikost){
        tocke = 0;
        polja = new int[velikost][velikost];
        konec = false;
        dodajNovoPloscico();
        dodajNovoPloscico();
    }

    public static void koncajIgro(){
        if(!konec){
            konec = true;
        }
        else if(konec){
            System.exit(0);
        }
    }

    public static int vrniPloscico(int i, int j){
        return polja[i][j];
    }

    public static int vrniTocke(){
        return tocke;
    }

    public static boolean jeZmagal(){
        for(int i = 0;i< polja.length;i++){
            for(int j = 0;j< polja.length;j++){
                if(polja[i][j] == 2048){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean jeKonec(){
        return konec;
    }

    public static boolean premikLevo(){
        boolean flag = false;
        for(int i = 0;i<polja.length;i++){
            for(int j = 0;j<polja.length;j++){
                for(int k = 0;k<polja.length-1;k++){
                    if(polja[i][k] == 0 && polja[i][k+1] != 0){
                        int tmp = polja[i][k];
                        polja[i][k] = polja[i][k+1];
                        polja[i][k+1] = tmp;
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    public static boolean zdruziPloscice(){
        boolean flag = false;
        for(int i = 0;i< polja.length;i++){
            for(int j = 0;j< polja.length-1;j++){
                if(polja[i][j] == polja[i][j+1]){
                    polja[i][j] *= 2;
                    tocke += polja[i][j];
                    polja[i][j+1] = 0;
                    flag = true;
                }
            }
        }
        premikLevo();
        return flag;
    }

    public static void naslednjaPoteza(int smer){
        boolean flag;
        switch (smer){
            case 0:
                flag = premikLevo();
                zdruziPloscice();
                if(flag){
                    dodajNovoPloscico();
                }
                break;
            case 1:
                rotiraj();
                rotiraj();
                flag = premikLevo();
                zdruziPloscice();
                if(flag){
                    dodajNovoPloscico();
                }
                rotiraj();
                rotiraj();
                break;
            case 2:
                rotiraj();
                rotiraj();
                rotiraj();
                flag = premikLevo();
                zdruziPloscice();
                if(flag){
                    dodajNovoPloscico();
                }
                rotiraj();
                break;
            case 3:
                rotiraj();
                flag = premikLevo();
                zdruziPloscice();
                if(flag){
                    dodajNovoPloscico();
                }
                rotiraj();
                rotiraj();
                rotiraj();
                break;
        }
    }

    public static void main(String[] args) {
        zacniNovoIgro(4);
    }
}

/*
for(int i = 0;i<velikost;i++){
   for(int j = 0;j<velikost;j++){
         System.out.print(vrniPloscico(i,j) + " ");
      }
    System.out.println();
}
*/