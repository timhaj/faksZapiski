import java.io.File;
import java.util.Scanner;
import java.math.BigInteger;

public class DN05{
    public static void main(String args[]){
        switch(args[0]){
            case "izpisi":
                int slika[][] = preberiSliko(args[1]);
                izpisiSliko(slika);
                break;
            case "histogram":
                slika = preberiSliko(args[1]);                
                izpisiHistogram(slika);
                break;
            case "svetlost":
                slika = preberiSliko(args[1]);                
                System.out.printf("Srednja vrednost sivine na sliki %s je: %.2f", args[1], svetlostSlike(slika));
                break;
            case "zrcali":
                slika = preberiSliko(args[1]);                
                int zrcaljeno[][] = zrcaliSliko(slika);
                izpisiSliko(zrcaljeno);
                break;
            case "rotiraj":
                slika = preberiSliko(args[1]);                
                int rotirano[][] = rotirajSliko(slika);
                izpisiSliko(rotirano);
                break;
            case "vrstica":
                slika = preberiSliko(args[1]);                
                System.out.printf("Max razlika svetlo - temno je v %d. vrstici.", poisciMaxVrstico(slika));
                break; 
            case "zmanjsaj":
                slika = preberiSliko(args[1]);                
                int zmanjsano[][] = zmanjsajSliko(slika);
                izpisiSliko(zmanjsano);
                break;
            case "barvna":
                int slikaBarvna[][][] = preberiBarvnoSliko(args[1]); 
                izpisiBarvnoSliko(slikaBarvna);
                break;
            case "sivinska":
                slikaBarvna = preberiBarvnoSliko(args[1]);
                int slikaBarvna_siva[][] = pretvoriVSivinsko(slikaBarvna);
                izpisiSliko(slikaBarvna_siva);
                break;   
            case "uredi":
                String imena[] = new String[args.length-1];
                for(int i = 0;i<imena.length;i++){
                    imena[i] = args[i+1];
                }
                preberiVseInIzpisi(imena);
                break;  
            case "jedro":
                slika = preberiSliko(args[1]);   
                konvolucijaJedro(slika);
                break;   
            case "glajenje":
                slika = preberiSliko(args[1]);
                int razsirjena[][] = new int[slika.length+2][slika[0].length+2];
                razsirjena[0][0] = slika[0][0];
                razsirjena[slika.length+1][slika[0].length+1] = slika[slika.length-1][slika[0].length-1];
                razsirjena[slika.length+1][0] = slika[slika.length-1][0];
                razsirjena[0][slika[0].length+1] = slika[0][slika[0].length-1];
                for(int i = 0;i<slika.length;i++){
                    for(int j = 0;j<slika[0].length;j++){
                        razsirjena[i+1][j+1] = slika[i][j];
                        if(i == 0){
                            razsirjena[i][j+1] = slika[i][j];
                        }
                        else if(i == slika.length - 1){
                            razsirjena[i+2][j+1] = slika[i][j];
                        }
                        if(j == 0){
                            razsirjena[i+1][j] = slika[i][j];
                        }
                        else if(j == slika[0].length - 1){
                            razsirjena[i+1][j+2] = slika[i][j];
                        }
                    }
                }
                konvolucijaGlajenje(razsirjena);
                break;
            case "robovi":
                slika = preberiSliko(args[1]);
                razsirjena = new int[slika.length+2][slika[0].length+2];
                razsirjena[0][0] = slika[0][0];
                razsirjena[slika.length+1][slika[0].length+1] = slika[slika.length-1][slika[0].length-1];
                razsirjena[slika.length+1][0] = slika[slika.length-1][0];
                razsirjena[0][slika[0].length+1] = slika[0][slika[0].length-1];
                for(int i = 0;i<slika.length;i++){
                    for(int j = 0;j<slika[0].length;j++){
                        razsirjena[i+1][j+1] = slika[i][j];
                        if(i == 0){
                            razsirjena[i][j+1] = slika[i][j];
                        }
                        else if(i == slika.length - 1){
                            razsirjena[i+2][j+1] = slika[i][j];
                        }
                        if(j == 0){
                            razsirjena[i+1][j] = slika[i][j];
                        }
                        else if(j == slika[0].length - 1){
                            razsirjena[i+1][j+2] = slika[i][j];
                        }
                    }
                }
                konvolucijaRobovi(razsirjena);
                break;              
        }
    }

    static void konvolucijaRobovi(int[][] slika){
        int roboviNavpicno[][] = new int[slika.length-2][slika[0].length-2];
        int roboviVodoravno[][] = new int[slika.length-2][slika[0].length-2];
        int roboviSkupaj[][] = new int[slika.length-2][slika[0].length-2];
        int roboviKoncni[][] = new int[slika.length-2][slika[0].length-2];
        int max = 0;
        for(int i = 1;i<slika.length-1;i++){
            for(int j = 1;j<slika[0].length-1;j++){
                roboviNavpicno[i-1][j-1] = slika[i-1][j-1]*1 + slika[i-1][j]*0 + slika[i-1][j+1]*(-1) + slika[i][j-1]*2 + slika[i][j]*0 + slika[i][j+1]*(-2) + slika[i+1][j-1]*1 + slika[i+1][j]*0 + slika[i+1][j+1]*(-1);
                roboviVodoravno[i-1][j-1] = slika[i-1][j-1]*1 + slika[i-1][j]*2 + slika[i-1][j+1]*1 + slika[i][j-1]*0 + slika[i][j]*0 + slika[i][j+1]*0 + slika[i+1][j-1]*(-1) + slika[i+1][j]*(-2) + slika[i+1][j+1]*(-1);
                roboviSkupaj[i-1][j-1] = (int) Math.round(Math.sqrt(Math.pow(roboviNavpicno[i-1][j-1],2) + Math.pow(roboviVodoravno[i-1][j-1],2)));
                if(roboviSkupaj[i-1][j-1] > max){
                    max = roboviSkupaj[i-1][j-1];
                }
            }
        }
        for(int i = 0;i<roboviKoncni.length;i++){
            for(int j = 0;j<roboviKoncni[0].length;j++){
                roboviKoncni[i][j] = (int) Math.round(roboviSkupaj[i][j] * (255.0/max));
            }
        }
        izpisiSliko(roboviKoncni);
    }

    static void konvolucijaGlajenje(int slika[][]){
        int arr[][] = new int[slika.length-2][slika[0].length-2];
        for(int i = 1;i<slika.length-1;i++){
            for(int j = 1;j<slika[0].length-1;j++){
                arr[i-1][j-1] = (int) (Math.round(slika[i-1][j-1]*(1/16.0)) + Math.round(slika[i-1][j]*(1/8.0)) + Math.round(slika[i-1][j+1]*(1/16.0)) + Math.round(slika[i][j-1]*(1/8.0)) + Math.round(slika[i][j]*(1/4.0)) + Math.round(slika[i][j+1]*(1/8.0)) + Math.round(slika[i+1][j-1]*(1/16.0)) + Math.round(slika[i+1][j]*(1/8.0)) + Math.round(slika[i+1][j+1]*(1/16.0)));
            }
        }
        izpisiSliko(arr);
    }

    static void konvolucijaJedro(int slika[][]){
        int arr[][] = new int[slika.length-2][slika[0].length-2];
        for(int i = 1;i<slika.length-1;i++){
            for(int j = 1;j<slika[0].length-1;j++){
                arr[i-1][j-1] = slika[i-1][j-1] + slika[i-1][j] + slika[i-1][j+1] + slika[i][j-1] + slika[i][j] + slika[i][j+1] + slika[i+1][j-1] + slika[i+1][j] + slika[i+1][j+1];
            }
        }
        izpisiSliko(arr);
    }

    static void preberiVseInIzpisi(String[] imenaSlik){
        int svetlosti[] = new int[imenaSlik.length];
        for(int i = 0;i<svetlosti.length;i++){
            svetlosti[i] = (int) Math.round(svetlostSlike(preberiSliko(imenaSlik[i])));
        }
        for(int i = 0;i<svetlosti.length;i++){
            for(int j = 0;j<svetlosti.length - 1 - i;j++){
                if(svetlosti[j] < svetlosti[j+1]){
                    int tmp = svetlosti[j];
                    svetlosti[j] = svetlosti[j+1];
                    svetlosti[j+1] = tmp;
                    String temp = imenaSlik[j];
                    imenaSlik[j] = imenaSlik[j+1];
                    imenaSlik[j+1] = temp;
                }
            }
        }
        for(int i = 0;i<svetlosti.length;i++){
            for(int j = 0;j<svetlosti.length - 1 - i;j++){
                if(svetlosti[j] == svetlosti[j+1] && (imenaSlik[j].toLowerCase().compareTo(imenaSlik[j+1].toLowerCase()) > 0)){
                    int tmp = svetlosti[j];
                    svetlosti[j] = svetlosti[j+1];
                    svetlosti[j+1] = tmp;
                    String temp = imenaSlik[j];
                    imenaSlik[j] = imenaSlik[j+1];
                    imenaSlik[j+1] = temp;                    
                }
            }
        }

        for(int i = 0;i<imenaSlik.length;i++){
            System.out.printf("%s (%d)\n", imenaSlik[i], svetlosti[i]);
        }
    }

    static int[][] pretvoriVSivinsko(int slika[][][]){
        int arr[][] = new int[slika.length][slika[0].length];
        for(int i = 0;i<arr.length;i++){
            for(int j = 0;j<arr[0].length;j++){
                arr[i][j] = (int) (((slika[i][j][0] + slika[i][j][1] + slika[i][j][2])/3) * (255/1023.0));
            }
        }
        return arr;
    }

    static void izpisiBarvnoSliko(int slika[][][]){
        if(slika.length > 0){
            System.out.printf("velikost slike: %s x %s\n", slika[0].length, slika.length);
            for(int i = 0;i<slika.length;i++){
                for(int j = 0;j<slika[0].length;j++){
                    System.out.printf("(%4d,%4d,%4d) ", slika[i][j][0], slika[i][j][1], slika[i][j][2]);
                }
                System.out.println();
            }
        }        
    }

    static int[][][] preberiBarvnoSliko(String ime){
        try{
            Scanner sc = new Scanner(new File(ime));
            if(sc.hasNextLine()){
                String format[] = sc.nextLine().split(" ");
                if(format.length != 4){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2B.");
                    return new int[0][0][0];                    
                }                
                if(!format[2].equals("x") || !format[0].equals("P2B:")){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2B.");
                    return new int[0][0][0];
                }  
                if(!(jeNumericen(format[1]) && jeNumericen(format[3]))){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2B (velikost slike ni pravilna).");
                    return new int[0][0][0];                    
                } 
                BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
                BigInteger value1 = new BigInteger(format[1]);
                BigInteger value2 = new BigInteger(format[3]);
                if(value1.compareTo(maxInt) > 0 || value2.compareTo(maxInt) > 0){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2B (velikost slike ni pravilna).");
                    return new int[0][0][0];                       
                }                                             
                int visina = Integer.parseInt(format[3]);
                int sirina = Integer.parseInt(format[1]);
                if(visina <= 0 || sirina <= 0){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2B (velikost slike je 0 ali negativna).");
                    return new int[0][0][0];
                }                
                int arr[][][] = new int[visina][sirina][3];
                if(sc.hasNextLine()){
                    String data[] = sc.nextLine().split(" ");
                    if(data.length < visina*sirina){
                        System.out.println("Napaka: datoteka " + ime + " vsebuje premalo podatkov.");
                        return new int[0][0][0];                        
                    }                    
                    int n = 0;
                    for(int i = 0;i<visina;i++){
                        for(int j = 0;j<sirina;j++){
                            int mask = 0b1111111111;
                            int pow = 1;
                            for(int k = 2;k>=0;k--){
                                int piksel = Integer.parseInt(data[n]);
                                arr[i][j][k] = (piksel & mask)/pow; 
                                mask <<= 10;
                                pow *= 1024;
                            }
                            n++;
                        }
                    }
                    return arr;
                }
                else{
                    System.out.println("Napaka: datoteka " + ime + " vsebuje premalo podatkov.");
                    return new int[0][0][0];                    
                }
            }
            else{
                System.out.println("Napaka: Datoteka " + ime + " je prazna.");
                return new int[0][0][0];                
            }
        } catch(Exception e){
            System.out.println("Napaka: Datoteka " + ime + " ne obstaja.");
        }
        return new int[0][0][0];
    }

    static int[][] zmanjsajSliko(int slika[][]){
        if(slika.length < 3 || slika[0].length < 3){
            return slika;
        }
        int arr[][] = new int[slika.length/2][slika[0].length/2];
        for(int i = 0;i<slika.length-1;i+=2){
            for(int j = 0;j<slika[0].length-1;j+=2){
                arr[i/2][j/2] = (slika[i][j] + slika[i+1][j] + slika[i][j+1] + slika[i+1][j+1])/4; 
            }
        }
        return arr;
    }

    static int poisciMaxVrstico(int slika[][]){
        int vrstica = 0;
        int maksVrednost = 0;
        for(int i = 0;i<slika.length;i++){
            int min = 256;
            int maks = -1;
            for(int j = 0;j<slika[0].length;j++){
                if(slika[i][j] < min){
                    min = slika[i][j];
                }
                if(slika[i][j] > maks){
                    maks = slika[i][j];
                }
            }
            if((maks - min) > maksVrednost){
                vrstica = i;
                maksVrednost = maks - min;
            }
        }
        return vrstica + 1;
    }

    static int[][] rotirajSliko(int slika[][]){
        int arr[][] = new int[slika[0].length][slika.length];
        for(int i = 0;i<arr.length;i++){
            for(int j = 0;j<arr[0].length;j++){
                arr[i][j] = slika[arr[0].length - 1 - j][i];
            }
        }
        return arr;
    }

    static int[][] zrcaliSliko(int slika[][]){
        int arr[][] = new int[slika.length][slika[0].length];
        for(int i = 0;i<slika.length;i++){
            for(int j = slika[0].length - 1; j >= 0;j--){
                arr[i][(slika[0].length - 1 - j)] = slika[i][j];
            }
        }
        return arr;
    }

    static double svetlostSlike(int slika[][]){
        double rez = 0.0;
        for(int i = 0;i<slika.length;i++){
            for(int j = 0;j<slika[0].length;j++){
                rez += slika[i][j];
            }
        }
        return rez/(slika.length*slika[0].length);
    }

    static void izpisiHistogram(int slika[][]){
        if(slika.length > 0){
            int razdelki[] = new int[256];
            for(int i = 0;i<slika.length;i++){
                for(int j = 0;j<slika[0].length;j++){
                    razdelki[slika[i][j]] += 1;
                }
            }
            System.out.println("sivina : # pojavitev");
            for(int i = 0;i<razdelki.length;i++){
                if(razdelki[i] != 0){
                    System.out.printf("%5d  :   %d\n", i, razdelki[i]);
                }
            }
        }
    }

    static void izpisiSliko(int slika[][]){
        if(slika.length > 0){
            System.out.printf("velikost slike: %s x %s\n", slika[0].length, slika.length);
            for(int i = 0;i<slika.length;i++){
                for(int j = 0;j<slika[0].length;j++){
                    System.out.printf("%3d ", slika[i][j]);
                }
                System.out.println();
            }
        }
    }

    static int[][] preberiSliko(String ime){
        try{
            Scanner sc = new Scanner(new File(ime));
            if(sc.hasNextLine()){
                String format[] = sc.nextLine().split(" ");
                if(format.length != 4){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2.");
                    return new int[0][0];                    
                }
                if(!(jeNumericen(format[1]) && jeNumericen(format[3]))){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2 (velikost slike ni pravilna).");
                    return new int[0][0];                    
                }
                BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
                BigInteger value1 = new BigInteger(format[1]);
                BigInteger value2 = new BigInteger(format[3]);
                if(value1.compareTo(maxInt) > 0 || value2.compareTo(maxInt) > 0){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2 (velikost slike ni pravilna).");
                    return new int[0][0];                       
                }
                int sirina = Integer.parseInt(format[1]);
                int visina = Integer.parseInt(format[3]);
                if(visina <= 0 || sirina <= 0){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2 (velikost slike je 0 ali negativna).");
                    return new int[0][0];
                }
                if(!format[2].equals("x") || !format[0].equals("P2:")){
                    System.out.println("Napaka: datoteka " + ime + " ni v formatu P2.");
                    return new int[0][0];
                }
                if(sc.hasNextLine()){
                    String piksli[] = sc.nextLine().split(" ");
                    int arr[][] = new int[visina][sirina];
                    if(piksli.length < visina*sirina){
                        System.out.println("Napaka: datoteka " + ime + " vsebuje premalo podatkov.");
                        return new int[0][0];                        
                    }
                    int n = 0;
                    for(int i = 0;i<visina;i++){
                        for(int j = 0;j<sirina;j++){
                            int piksel = Integer.parseInt(piksli[n]);
                            n++;
                            if(piksel >= 0 && piksel <=255){
                                arr[i][j] = piksel;
                            }
                            else{
                                System.out.println("Napaka: datoteka " + ime + " vsebuje podatke izven obsega 0 do 255.");
                                return new int[0][0];
                            }
                        }
                    }
                    sc.close();
                    return arr;
                }
                else{
                    System.out.println("Napaka: datoteka " + ime + " vsebuje premalo podatkov.");
                    return new int[0][0];
                }
            }
            else{
                System.out.println("Napaka: Datoteka " + ime + " je prazna.");
                return new int[0][0];
            }

        } catch(Exception e){
            System.out.println("Napaka: datoteka " + ime + " ne obstaja.");
        }
        return new int[0][0]; 
    }

    static boolean jeNumericen(String str){
        for(int i = 0;i<str.length();i++){
            if(!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }
}