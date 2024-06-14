public class Fakulteta {

    //do n = 20 je long primeren za fakulteta

    public static void main(String[] args) {
        //dodatna naloga b)
        System.out.printf("%3s %8s %26s   %10s\n", "k", "Math.PI", "PI (Nilakantha)", "razlika");
        System.out.print("-".repeat(58));
        for(int i = 1;i<=22;i++){
            double sussy = Math.PI;
            double wussy = izracunajPiNilakantha(i);
            //double napaka = 100 * ((fact - stir)/ fact) ;
            System.out.printf("\n%3d  %16.15f  %16.15f   %+.15f", i, sussy, wussy, (double) (sussy - wussy));
        }

        // naloga 5
/*        System.out.printf("%3s %16s %16s   %6s\n", "n", "n!", "Stirling(n)", "napaka (%)");
        System.out.print("-".repeat(58));
        for(int i = 1;i<=100;i++){
            double fact = fakultetaD(i);
            double stir = stirlingD(i);
            double napaka = 100 * ((fact - stir)/ fact) ;
            System.out.printf("\n%3d  %16.9E  %16.9E   %.6f", i, fact, stir, (double) (fact - stir)* 100 / fact);
        }*/

        //naloga 2
        /*
        System.out.printf("%3s %21s %21s   %6s\n", "n", "n!", "Stirling(n)", "napaka (%)");
        System.out.print("-".repeat(58));
        for(int i = 1;i<=50;i++){
            long fact = fakultetaL(i);
            long stir = stirlingL(i);
            double napaka = 100 * ((fact - stir)/ fact) ;
            System.out.printf("\n%3d %21d %21d   %.6f", i, fact, stir, (double) (fact - stir)* 100 / fact);
        }
        */
    }

    public static long fakultetaL(int n){
        long rezultat = 1;
        for(int i = 1;i<=n;i++){
            rezultat *= i;
        }
        return rezultat;
    }

    public static long stirlingL(int n){
        return Math.round(Math.sqrt(2*Math.PI*n)*Math.pow((n/Math.exp(1)), n));
    }

    public static double fakultetaD(int n){
        double rezultat = 1;
        for(int i = 1;i<=n;i++){
            rezultat *= i;
        }
        return rezultat;
    }

    public static double stirlingD(int n){
        return Math.sqrt(2*Math.PI*n)*Math.pow((n/Math.exp(1)), n);
    }

    public static double izracunajPiNilakantha(int k){
        double rezultat = 3.0;
        for(int i = 1; i<k;i++){
            int zacetek = i * 2;
            if( i % 2 == 1){
                rezultat += (double) (4) / ((zacetek)*(zacetek +1 )*(zacetek + 2));
            }
            else{
                rezultat -= (double) (4) / ((zacetek)*(zacetek +1 )*(zacetek + 2));
            }
        }
        return rezultat;
    }
}