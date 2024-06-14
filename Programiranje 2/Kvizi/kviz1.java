public class kviz1 {
    public static void main(String[] args) {
        //java();
        //kalkulator(2, 0);
        //nicli(1,-7,12);
        //krog(7.5, 3);
        //System.out.println(pretvoriSekunde(-12));
        //javaJavaJava(3);     
        //System.out.println(jeFibonaccijevo(2021)); 
        //System.out.println(jePrastevilo(42));
        //veckratnikDelitelj(44,0);
        //praDvojcek(10);
        //System.out.println(izracunajRazliko("08:23:10", "10:10:05"));
        //vDesetisko(120);
        //System.out.println(pretvoriVDesetisko("FF", 16));
        //trikotnik(5,7);
        //metulj(5,3);
        izrisiZastavo(1);
    }

    static void java() {
        System.out.println("   J    a   v     v  a");
        System.out.println("   J   a a   v   v  a a");
        System.out.println("J  J  aaaaa   V V  aaaaa");
        System.out.println(" JJ  a     a   V  a     a");
    }

    static void kalkulator(int a, int b) {
        if (b == 0) {
            System.out.println("Napaka: deljenje z 0");
        } else {
            System.out.println(a + " + " + b + " = " + (a + b));
            System.out.println(a + " - " + b + " = " + (a - b));
            System.out.println(a + " x " + b + " = " + (a * b));
            System.out.println(a + " / " + b + " = " + (a / b));
            System.out.println(a + " % " + b + " = " + (a % b));
        }
    }

    static void nicli(int a, int b, int c){
        int d = (int) Math.pow(b, 2)- 4*a*c;
        if(d < 0){
            System.out.println("Napaka: nicli enacbe ne obstajata");
        }
        else if(d == 0){
            double x = -b/(2*a);
            System.out.printf("x1=%.2f, x2=%.2f", x, x);
        }
        else{
            double x1 = (-b + Math.sqrt(d))/(2*a);            
            double x2 = (-b - Math.sqrt(d))/(2*a); 
            System.out.printf("x1=%.2f, x2=%.2f", x1, x2);          
        }
    }

    static void krog(double r, int d){
        if(r < 0){
            System.out.println("Napaka: negativen polmer");
        }
        else if(d < 0){
            System.out.println("Napaka: negativen d");
        }
        else{
            System.out.printf("Obseg kroga s polmerom r=%.2f je %." + d + "f\n", r, 2*Math.PI*r); 
            System.out.printf("Ploscina kroga s polmerom r=%.2f je %." + d + "f\n", r, Math.PI*r*r); 
        }
    }

    static String pretvoriSekunde(int sekunde){
        if(sekunde < 0){
            return "Število sekund ne more biti negativno";
        }
        else{
            return String.format("%02d", sekunde/3600) + ":" + String.format("%02d", (sekunde/60)%60) + ":" + String.format("%02d", sekunde%60);
        }
    }

    static void javaJavaJava(int n){
        String str = " ";
        String[] tekst = {"     J    a   v     v  a", "     J   a a   v   v  a a", "  J  J  aaaaa   V V  aaaaa", "   JJ  a     a   V  a     a"};
        if(n < 0){
            System.out.println("Napaka: negativen n");
        }
        else{
            for(int i = 0;i<4;i++){
                for(int j = 0;j<n;j++){
                    System.out.print(tekst[i]);
                    if(i == 0)
                        System.out.print(str.repeat(3));
                    if(i == 1)
                        System.out.print(str.repeat(2));
                    if(i == 2)
                        System.out.print(str.repeat(1));
                    if(i == 3)
                        System.out.print(str.repeat(0));
                }
                System.out.println();
            }
        }
    }

    static boolean jeFibonaccijevo(int n){
        int c = 1;
        int p = 1;
        for(;c<=n;){
            int tmp = 0;
            if(c == n){
                return true;
            }
            tmp = p;
            p = c;
            c = c + tmp;
        }
        return false;
    }

    static boolean jePrastevilo(int n){
        int tmp = 0;
        for(int i = 1;i<=n;i++){
            if(n % i == 0){
                tmp++;
            }
        }
        if(tmp == 2){
            return true;
        }
        return false;
    }

    static void veckratnikDelitelj(int a, int b){
        a = Math.abs(a);
        b = Math.abs(b);
        if(a == 0 || b == 0){
            System.out.print("Napaka: obe števili morata biti različni od nič.");
        }
        else{
            int aa = a;
            int bb = b;
            for(;;){
                if(aa > bb){
                    aa = aa - bb;
                }
                else if(aa < bb){
                    bb = bb - aa;
                }
                if(aa == bb){
                    System.out.println("Največji skupni delitelj je " + aa + ".");
                    System.out.println("Najmanjši skupni večkratnik je " + (a*b)/aa + ".");
                    break;
                }
            }
        }
    }

    static void praDvojcek(int n){
        for(int i = 1;i<n;i++){
            for(int j = i;j<n;j++){
                if(jePrastevilo(i) && jePrastevilo(j) && (j - i == 2)){
                    System.out.println("(" + i + ", " + j + ")");
                }
            }
        }
    }

    static String izracunajRazliko(String prviCas, String drugiCas){
        String[] p1 = prviCas.split(":", 0);
        String[] p2 = drugiCas.split(":", 0);
        int c1 = 0;
        int c2 = 0;
        int odmik = 3600;
        for(int i = 0;i<3;i++){
            c1 += Integer.parseInt(p1[i]) * odmik;
            c2 += Integer.parseInt(p2[i]) * odmik;
            odmik /= 60;
        }
        if(c1 >= c2)
            return pretvoriSekunde(c1-c2);
        return pretvoriSekunde(c2-c1);
    }

    static int[] presek(int[] tabela1, int[] tabela2){
        //stevilo skupnih elementov
        int skp = 0;
        for(int i=0;i<tabela1.length;i++){
            for(int j = 0;j<tabela2.length;j++){
                if(tabela1[i] == tabela2[j]){
                    skp++;
                }
            }
        }
        //najdemo skupne elemente
        int[] inter = new int[skp];
        int p = 0;
        for(int i=0;i<tabela1.length;i++){
            for(int j = 0;j<tabela2.length;j++){
                if(tabela1[i] == tabela2[j]){
                    boolean flag = false;
                    for(int k = 0;k<inter.length;k++){
                        if(inter[k] == tabela1[i]){
                            flag = true;
                        }
                    }
                    if(!flag){
                        inter[p] = tabela1[i];
                        p++;
                    }
                }
            }
        }
        //znebimo se duplikatov
        int[] intersection = new int[p];
        for(int i = 0;i<intersection.length;i++){
            intersection[i] = inter[i];
        }
        return intersection;
    }

    static int[] stik(int[] tabela1, int[] tabela2){
        int[] join = new int[tabela1.length + tabela2.length];
        int st = 0;
        for(int i = 0;i<tabela1.length;i++){
            join[st] = tabela1[i];
            st++;
        }
        for(int i = 0;i<tabela2.length;i++){
            join[st] = tabela2[i];
            st++;
        }
        return join;
    }

    static void pitagoroviTrojcki(int x){
        for(int a = 1;a<=x;a++){
            for(int b = a;b<=x;b++){
                for(int c=b;c<=x;c++){
                    if(Math.pow(a,2) + Math.pow(b,2) == Math.pow(c,2)){
                        System.out.println(a + " " + b + " " + c);
                    }
                }
            }
        }
    }

    static int vsotaPrvih(int n){
        int vsota = 0;
        int st = 0;
        for(int i = 1;st<n;i++){
            if(jePrastevilo(i)){
                vsota += i;
                st++;
            }
        }
        return vsota;
    }

    static void vDesetisko(int n){
        int d = 0;
        int tmp = n;
        for(int i = 1; tmp != 0;){
            if(tmp % 10 == 9 || tmp % 10 == 8){
                System.out.println("Število " + n + " ni število v osmiškem sistemu (števka " + tmp % 10 + ")");
                return;
            }
            else{
                d += (tmp % 10) * i;
                i *= 8;
                tmp = (int) tmp / 10;
            }
        }
        System.out.println(n + "(8) = " + d + "(10)");
    }

    static void trikotnik(int n, int tip){
        String str = " ";
        switch(tip){
            case 1:
                for(int i = 1;i<=n;i++){
                    for(int j = 1;j<=i;j++){
                        System.out.print(j % 10 + " ");
                    }
                    System.out.println();
                }
                break;
            case 2:
                for(int i = n;i>=1;i--){
                    System.out.print(str.repeat((n-i)*2));
                    for(int j = 1;j<=i;j++){
                        System.out.print(j % 10 + " ");
                    }
                    System.out.println();
                }
                break;
            case 3:
                for(int i = 1;i<=n;i++){
                    System.out.print(str.repeat((n-i)*2));
                    for(int j = i;j>=1;j--){
                        System.out.print(j % 10 + " ");
                    }
                    System.out.println();
                }
                break;
            case 4:
                for(int i = n;i>=1;i--){
                    for(int j = i;j>=1;j--){
                        System.out.print(j % 10 + " ");
                    }
                    System.out.println();
                }
                break;
            case 5:
                for(int i = 1;i<=n;i++){
                    System.out.print(str.repeat((n-i)*2));
                    for(int j = 1;j<i;j++){
                        System.out.print(j % 10 + " ");
                    }
                    for(int j = i;j>=1;j--){
                        System.out.print(j % 10 + " ");                        
                    }
                    System.out.println();
                }
                break;
            case 6:
                for(int i = n;i>=1;i--){
                    System.out.print(str.repeat((n-i)*2));
                    for(int j = 1;j<i;j++){
                        System.out.print(j % 10 + " ");
                    }
                    for(int j = i;j>=1;j--){
                        System.out.print(j % 10 + " ");                        
                    }
                    System.out.println();
                }                
                break;
            case 7:
                int s = 1;
                for(int i = 1;i<=n;i++){
                    System.out.print(str.repeat((n-i)*2));
                    for(int j = i;j<s;j++){
                        System.out.print(j % 10 + " ");
                    }
                    for(int j = s;j>=i;j--){
                        System.out.print(j % 10 + " ");                        
                    }
                    System.out.println();
                    s += 2;
                }                
                break;
        }
    }

    static String pretvoriVDesetisko(String n, int b){
        //predvidevam da bi tole moral resiti s try-catch blokom, vendar malo hardcoding-a/brute-force-anja ne skodi
        switch(b){
            case 2:
                for(int i = 0;i<n.length();i++){
                    if(!(n.charAt(i) == '1' || n.charAt(i) == '0')){
                        return "Napaka pri pretvorbi sistema - števka " + n.charAt(i);
                    }
                }
                break;
            case 16:
                for(int i = 0;i<n.length();i++){
                    if(!(n.charAt(i) == '0' || n.charAt(i) == '1' || n.charAt(i) == '2' || n.charAt(i) == '3' || n.charAt(i) == '4' || n.charAt(i) == '5' || n.charAt(i) == '6' || n.charAt(i) == '7' || n.charAt(i) == '8' || n.charAt(i) == '9' || n.charAt(i) == 'A' || n.charAt(i) == 'B' || n.charAt(i) == 'C' || n.charAt(i) == 'D' || n.charAt(i) == 'E' || n.charAt(i) == 'F')) {
                        return "Napaka pri pretvorbi sistema - števka " + n.charAt(i);
                    }
                }
                break;
            case 5:
                for(int i = 0;i<n.length();i++){
                    if(!(n.charAt(i) == '0' || n.charAt(i) == '1' || n.charAt(i) == '2' || n.charAt(i) == '3' || n.charAt(i) == '4')){
                        return "Napaka pri pretvorbi sistema - števka " + n.charAt(i);
                    }
                }
                break;
        }
        int pretvorba = Integer.parseInt(n, b);
        return n + "(" + b + ")=" + pretvorba + "(10)"; 
    }

    static void metulj(int n, int tip){
        String str = " ";
        switch(tip){
            case 1:
                for(int i = 1;i<=n;i++){
                    for(int j = 1;j<=i;j++){
                        System.out.print(j % 10 + " ");
                    }
                    System.out.print(str.repeat((n-i)*2));
                    if(i != n){
                        System.out.print(str.repeat((n-i-1)*2));
                    }
                    for(int j = i;j>=1;j--){
                        if(j != n){
                            System.out.print(j % 10 + " ");
                        }
                    }
                    System.out.println();
                }
                break;
            case 2:
                for(int i = n;i>=1;i--){
                    for(int j = 1;j<=i;j++){
                        System.out.print(j % 10 + " ");
                    }
                    System.out.print(str.repeat((n-i)*2));
                    if(i != n){
                        System.out.print(str.repeat((n-i-1)*2));
                    }
                    for(int j = i;j>=1;j--){
                        if(j != n){
                            System.out.print(j % 10 + " ");
                        }
                    }
                    System.out.println();
                }
                break;
            case 3:
                for(int i = 1;i<=n;i++){
                    for(int j = 1;j<=i;j++){
                        System.out.print(j % 10 + " ");
                    }
                    System.out.print(str.repeat((n-i)*2));
                    if(i != n){
                        System.out.print(str.repeat((n-i-1)*2));
                    }
                    for(int j = i;j>=1;j--){
                        if(j != n){
                            System.out.print(j % 10 + " ");
                        }
                    }
                    System.out.println();
                }
                //
                for(int i = n-1;i>=1;i--){
                    for(int j = 1;j<=i;j++){
                        System.out.print(j % 10 + " ");
                    }
                    System.out.print(str.repeat((n-i)*2));
                    if(i != n){
                        System.out.print(str.repeat((n-i-1)*2));
                    }
                    for(int j = i;j>=1;j--){
                        if(j != n){
                            System.out.print(j % 10 + " ");
                        }
                    }
                    System.out.println();
                }
                break;
        }
    }

    static void izrisiZastavo(int n){
        int stz = (4*n)/2 - 1;
        for(int i = 0;i<5*n;i++){
            for(int j = 0;j<(15*n)+1;){
                if(j < 4*n && i < 3*n){
                    if(i % 2 == 1){
                        System.out.print(" ");
                        for(int k = 0;k<stz;k++){
                            System.out.print("* ");                            
                        }
                        System.out.print(" ");
                        j = 4*n;
                    }
                    else{
                        System.out.print("* ");
                        j += 2;
                    }
                }
                else{
                    System.out.print("=");
                    j++;
                }
            }
            System.out.println();
        }
    }

}