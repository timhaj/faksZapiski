public class Zanke{
    public static void main(String[] args) {
        //pravokotnikStevil(15,3);
        //pravokotnik(5,7,3);
        //trikotnikStevil(3);
        //trikotnikStevilObrnjen(3);
        //trikotnikStevil(5);
        // trikotnik(1,3);
        //trikotnikObrnjen(1,5);
        //romb(2,5);
        //smreka(3);
        //rombA(1,5);
        //rombPrazen(3,5);
        iks(3);
    }
    // 1. naloga
    /* a)
    static void pravokotnikStevil(int sirina, int visina){
        for(int i = 1;i<=visina;i++){
            for(int j = 1;j<=sirina;j++){
                System.out.print(i % 10);
            }
            System.out.println();
        }
    }
    */

    // b)
    static void pravokotnikStevil(int sirina, int visina){
        for(int i = 1;i<=visina;i++){
            for(int j = 1;j<=sirina;j++){
                System.out.print(j % 10);
            }
            System.out.println();
        }
    }

    // c)
    static void pravokotnik(int odmik, int sirina, int visina){
        String str = " ";
        for(int i = 0;i<visina;i++){
            System.out.print(str.repeat(odmik));
            for(int j = 0;j<sirina;j++){
                System.out.print("X");
            }
            System.out.println();
        }
    }

    // 2. naloga
    /*
    static void trikotnikStevil(int visina) {
       for(int i = 1;i<=visina;i++){
           for(int j = 0;j<i;j++){
               System.out.print(i % 10);
           }
           System.out.println();
       }
    }
    */
    /* b)
    static void trikotnikStevil(int visina) {
        for(int i = 1;i<=visina;i++){
            for(int j = 0;j<i;j++){
                System.out.print((j % 10) + 1);
            }
            System.out.println();
        }
    }
    */
    /* c)
    static void trikotnikStevilObrnjen(int visina){
        for(int i = visina; i>= 1;i--){
            for(int j = i; j >= 1; j--){
                System.out.print(i % 10);
            }
            System.out.println();
        }
    }
     */
    // d)
    static void trikotnikStevilObrnjen(int visina){
        for(int i = visina; i>= 1;i--){
            for(int j = 1; j <= i; j++){
                System.out.print(j % 10);
            }
            System.out.println();
        }
    }
    // e)
    static void trikotnikStevil(int visina){
        String str = " ";
        for(int i = 1;i<=visina;i++){
            System.out.print(str.repeat(visina-i));
            for(int j = 1;j<=(i*2 - 1);j++){
                System.out.print(j % 10);
            }
            System.out.println();
        }
    }
    // f)
    static void trikotnik(int odmik, int visina){
        String str = " ";
        for(int i = 1;i<=visina;i++){
            System.out.print(str.repeat(visina-i+odmik));
            for(int j = 1;j<=(i*2 - 1);j++){
                System.out.print(j % 10);
            }
            System.out.println();
        }
    }
    //pomozna metoda
    static void trikotnikSZvezdico(int odmik, int visina){
        String str = " ";
        for(int i = 1;i<visina;i++){
            System.out.print(str.repeat(visina-i+odmik));
            for(int j = 1;j<=(i*2 - 1);j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }
    static void trikotnikSZvezdicoSMREKA(int odmik, int visina){
        String str = " ";
        for(int i = 1;i<=visina;i++){
            System.out.print(str.repeat(visina-i+odmik));
            for(int j = 1;j<=(i*2 - 1);j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }

    // g)
    static void trikotnikObrnjen(int odmik, int visina){
        String str = " ";
        for(int i = visina;i>=1;i--){
            System.out.print(str.repeat(visina-i+odmik));
            for(int j = (i*2 - 1);j>=1;j--){
                System.out.print("*");
            }
            System.out.println();
        }
    }
    // 3. naloga
    // a)
    static void romb(int odmik, int velikost){
        trikotnikSZvezdico(odmik, velikost);
        trikotnikObrnjen(odmik, velikost);
    }
    //4. naloga
    static void smreka(int velikost){
        String str = " ";
        //krosnja
        for(int i = 0;i<velikost;i++){
            trikotnikSZvezdicoSMREKA((velikost-i-1)*2,(i+1)*2);
        }
        //deblo
        for(int i = 0;i<(velikost*2);i++){
            System.out.print(str.repeat(velikost+1));
            if(velikost % 2 == 0){
                for(int j = 0;j<velikost+1;j++){
                    System.out.print("X");
                }
                System.out.println();
            }
            else{
                for(int j = 0;j<velikost;j++){
                    System.out.print("X");
                }
                System.out.println();
            }
        }
    }
    //Dodatni izzivi
    // a)
    static void rombA(int odmik, int velikost){
        String str = " ";
        for(int i = 1;i<=velikost;i++){
            System.out.print(str.repeat((odmik*2) + (velikost - i)*2));
            for(int j = 1;j<=(i*2 - 1);j++){
                System.out.print("#");
                System.out.print(" ");
            }
            System.out.println();
        }
        for(int i = velikost-1;i >= 1;i--){
            System.out.print(str.repeat((odmik*2) + (velikost - i)*2));
            for(int j = (i*2 - 1);j >= 1;j--){
                System.out.print("#");
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    // b)
    static void rombPrazen(int odmik, int velikost){
        String str = " ";
        String str2 = "# ";
        System.out.print(str.repeat(odmik));
        System.out.print(str2.repeat((2*velikost)-1));
        System.out.println();
        for(int i = 1; i<=velikost;i++){
            if(i!=velikost){
                System.out.print(str.repeat(odmik));
                for(int j = 0;j<velikost - i;j++){
                    System.out.print("#");
                    System.out.print(" ");
                }
                if(i!=velikost){
                    System.out.print(str.repeat(i*2));
                    System.out.print(str.repeat((i-1)*2));
                }
                for(int j = 0;j<velikost - i;j++){
                    System.out.print("#");
                    System.out.print(" ");
                }
                System.out.println();
            }
        }
        for(int i = velikost-1; i > 1;i--){
            System.out.print(str.repeat(odmik));
            for(int j = velikost - i;j >= 0;j--){
                System.out.print("#");
                System.out.print(" ");
            }
            System.out.print(str.repeat((i-1)*2));
            System.out.print(str.repeat((i-2)*2));
            for(int j = velikost - i;j >= 0;j--){
                System.out.print("#");
                System.out.print(" ");
            }
            System.out.println();
        }

        System.out.print(str.repeat(odmik));
        System.out.print(str2.repeat((2*velikost)-1));
        System.out.println();
    }

    static void dvaPravokotnika(int odmik, int sirina, int visina, int razmik){
        String str = " ";
        for(int i = 0;i<visina;i++){
            System.out.print(str.repeat(odmik));
            for(int j = 0;j<5;j++){
                System.out.print("X");
            }
            System.out.print(str.repeat(razmik));
            for(int j = 0;j<sirina;j++){
                System.out.print("X");
            }
            System.out.println();
        }
    }

    static void iks(int velikost){
        for(int i = 0;i<velikost-1;i++){
            dvaPravokotnika(i*5,5,3,(velikost - (i*2))*5);
        }
        pravokotnik((velikost-1)*5,5,3);
        for(int i = velikost-1;i>0;i--){
            dvaPravokotnika((i-1)*5,5,3,(velikost - ((i-1)*2))*5);
        }
    }

    //uganke:
    /*  Včasih moker, včasih suh,
        včasih rahel kakor puh,
        včasih trd in zledenel,
        sprva vedno čisto bel. 
        ODGOVOR: SNEG
    */
    /*  Iz oblakov belo belih
        bele zvezdice letijo,
        jih lovim, da bi jih gledal,
        pa se mi takoj stopijo.
        ODGOVOR: SNEŽINKE
    */
    /*  Do konca oblečeno sredi pripeke,
        v mrazu in snegu pa kar brez obleke. 
        ODGOVOR: DREVO
    */
}