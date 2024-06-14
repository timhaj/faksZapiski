public class kviz2{
    public static void main(String[] args){
        //System.out.println(vsotaStevk("1abc2"));     	
        //System.out.println(preveriRep("Danes je dan","petek"));
        //System.out.printf("%.3f", koren(9, 3));
        //System.out.print(prepleti("pomlad", "JESEN"));
        //System.out.println(fibo(3));
        //System.out.print(vMorse("SOS"));
    }

    static int vsotaStevk(String str){
        int vsota = 0;
        for(int i = 0;i<str.length();i++){
            if((int) str.charAt(i) >= 48 && (int) str.charAt(i) <= 57){
                vsota += Integer.parseInt(String.valueOf(str.charAt(i)));
            }
        }
        return vsota;
    }

    static boolean preveriRep(String a, String b){
        String lowercase_a = a.toLowerCase();
        String lowercase_b = b.toLowerCase();
        for(int i = lowercase_a.length()-1; i>=0;i--){
            if(lowercase_a.length()-1-i > lowercase_b.length()-1){
                break;
            }
            if(lowercase_a.charAt(i) != lowercase_b.charAt(lowercase_b.length()-1-(lowercase_a.length()-1-i))){
                return false;
            }
        }
        return true;
    }

    static int[] range(int a, int b, int c){
        int velikost = (b-a)/c == ((float) b - a)/c ? (b-a)/c : (b-a)/c+1;
        int[] rez = new int[velikost];
        int stevec = 0;
        for(int i = a;i<b;i+=c){
            rez[stevec] = i;
            stevec++;
        }
        return rez;
    }

    static void rotiraj(int[] tabela, int n){
        int tmp = 0;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < tabela.length-1; k++) {
                for (int j = tabela.length-1; j > 0; j--) {
                    tmp = tabela[j];
                    tabela[j] = tabela[j - 1];
                    tabela[j - 1] = tmp;
                }
            }
        }
    }

    static int [] duplikati(int[] tabela){
        int stevec = 0;
        int[] tmp = new int[tabela.length];
        boolean razlicen = true;
        for(int i = 0;i<tabela.length;i++){
            for(int j = 0;j<tmp.length;j++){
                if(tabela[i] == tmp[j]){
                    razlicen = false;
                }
            }
            if(razlicen){
                tmp[stevec] = tabela[i];
                stevec++;
            }
            else{
                razlicen = true;
            }
        }
        int[] rez = new int[stevec];
        for(int i = 0;i<stevec;i++){
            rez[i] = tmp[i];
        }
        return rez;
    }

    static int vsotaSkupnihCifer(int a, int b){
        int[] prepovedana = new int[Integer.toString(a).length() + Integer.toString(b).length()];
        int tmp = b;
        int rez = 0;
        boolean flag = true;
        for(int stevec = 0;a > 0; a /= 10){
            for(;tmp > 0; tmp /= 10){
                if(a % 10 == tmp % 10){
                    for(int i = 0;i<prepovedana.length;i++){
                        if(prepovedana[i] == a % 10){
                            flag = false;
                        }
                    }
                    if(flag){
                        rez += a % 10;
                        prepovedana[stevec] = a % 10;
                        stevec++;
                    }
                    else{
                        flag = true;
                    }
                }
            }
            tmp = b;
        }
        return rez;
    }

    static String prepleti(String niz1, String niz2){
        String rez = "";
        if(niz1.length() == niz2.length()){
            for(int i = 0;i<niz1.length();i++){
                rez += String.format("%c%c",niz1.charAt(i),niz2.charAt(i));
            }
        }
        else if(niz1.length() > niz2.length()){
            for(int i = 0;i<niz1.length();i++){
                if(i > niz2.length()-1){
                    rez += String.format("%c ",niz1.charAt(i));
                }
                else{
                    rez += String.format("%c%c",niz1.charAt(i),niz2.charAt(i));
                }
            }
        }
        else{
            for(int i = 0;i<niz2.length();i++){
                if(i > niz1.length()-1){
                    rez += String.format(" %c",niz2.charAt(i));
                }
                else{
                    rez += String.format("%c%c",niz1.charAt(i),niz2.charAt(i));
                }
            }
        }
        return rez;
    }

    static void odpleti(String niz){
        String rez1 = "";
        String rez2 = "";
        for(int i = 0;i<niz.length();i+=2){
            rez1 += String.format("%c", niz.charAt(i));
        }
        for(int i = 1;i<niz.length();i+=2){
            rez2 += String.format("%c", niz.charAt(i));
        }
        System.out.println(String.format("%s\n%s", rez1, rez2));
    }

    static int fibo(int n){
        int vsota = 0;
        int[][] tab = new int[n][n];
        int c = 1;
        int p = 0;
        int tmp = 0;
        for(int i = 0;i<n;i++){
            for(int j = 0;j<n;j++){
                if(i == 0 && j == 0){
                    tab[i][j] = 1;
                }
                else{
                    tab[i][j] = c+p;
                    tmp = p;
                    p = c;
                    c = c + tmp;
                }
            }
        }
        for(int i = 0;i<n;i++){
            vsota += tab[i][i];
        }
        for(int i = n-1;i>=0;i--){
            vsota += tab[i][n-i-1];
        }
        return vsota;
    }

    static String kodiraj(String vhod, int odmik){
        String rez = "";
        for(int i = 0;i<vhod.length();i++){
            rez += String.format("%c", (char) (vhod.charAt(i) + odmik));
        }
        System.out.println(rez);
        return rez;
    }

    static String dekodiraj(String vhod, int odmik){
        String rez = "";
        for(int i = 0;i<vhod.length();i++){
            rez += String.format("%c", (char) (vhod.charAt(i) - odmik));
        }
        System.out.println(rez);
        return rez;
    }

    static int[] nicleSpredaj(int [] tabela){
        int n = 0;
        for(int i = 0;i<tabela.length;i++){
            if(tabela[i] == 0){
                n++;
            }
        }
        int rez[] = new int[tabela.length];
        for(int i = 0;i<n;i++){
            rez[i] = 0;
        }
        for(int i = 0;i<tabela.length;i++){
            if(tabela[i] != 0){
                rez[n] = tabela[i];
                n++;
            }
        }
        return rez;
    }

    static String binarnoSestej(String s, String b){
        String rez = "";
        if(s.length() == b.length()){
            boolean flag = false;
            for(int i = s.length()-1;i>=0;i--){
                if(s.charAt(i) == '1' && b.charAt(i) == '1'){
                    if(flag){
                        rez = "1" + rez;
                        flag = true;
                    }
                    else{
                        rez = "0" + rez;
                        flag = true;
                    }
                }
                else if((s.charAt(i) == '0' && b.charAt(i) == '1') || (s.charAt(i) == '1' && b.charAt(i) == '0')){
                    if(flag){
                        rez = "0" + rez;
                        flag = true;
                    }
                    else{
                        rez = "1" + rez;
                        flag = false;
                    }
                }
                else{
                    if(flag){
                        rez = "1" + rez;
                        flag = false;
                    }
                    else{
                        rez = "0" + rez;
                        flag = false;
                    }
                }
            }
            if(flag){
                rez = "1" + rez;
            }
        }
        else if(s.length() > b.length()){
            b = "0".repeat(s.length()-b.length()) + b;
            boolean flag = false;
            for(int i = s.length()-1;i>=0;i--){
                if(s.charAt(i) == '1' && b.charAt(i) == '1'){
                    if(flag){
                        rez = "1" + rez;
                        flag = true;
                    }
                    else{
                        rez = "0" + rez;
                        flag = true;
                    }
                }
                else if((s.charAt(i) == '0' && b.charAt(i) == '1') || (s.charAt(i) == '1' && b.charAt(i) == '0')){
                    if(flag){
                        rez = "0" + rez;
                        flag = true;
                    }
                    else{
                        rez = "1" + rez;
                        flag = false;
                    }
                }
                else{
                    if(flag){
                        rez = "1" + rez;
                        flag = false;
                    }
                    else{
                        rez = "0" + rez;
                        flag = false;
                    }
                }
            }
            if(flag){
                rez = "1" + rez;
            }            
        }
        else{
            s = "0".repeat(b.length()-s.length()) + s;
            boolean flag = false;
            for(int i = s.length()-1;i>=0;i--){
                if(s.charAt(i) == '1' && b.charAt(i) == '1'){
                    if(flag){
                        rez = "1" + rez;
                        flag = true;
                    }
                    else{
                        rez = "0" + rez;
                        flag = true;
                    }
                }
                else if((s.charAt(i) == '0' && b.charAt(i) == '1') || (s.charAt(i) == '1' && b.charAt(i) == '0')){
                    if(flag){
                        rez = "0" + rez;
                        flag = true;
                    }
                    else{
                        rez = "1" + rez;
                        flag = false;
                    }
                }
                else{
                    if(flag){
                        rez = "1" + rez;
                        flag = false;
                    }
                    else{
                        rez = "0" + rez;
                        flag = false;
                    }
                }
            }
            if(flag){
                rez = "1" + rez;
            } 
        }
        return rez;
    }

    static double koren(int x, int d){
        double rez = 0.0;
        //celo stevilo
        for(int i = 1;i<x;i++){
            if(i*i <= x && (i+1)*(i+1) > x){
                rez += i;
            }
        }
        //decimalno
        int tmp = 10;
        for(int i = 0;i<d;i++){
            for(int j = 1;j<x;j++){
                double clen = rez + ((float) j/tmp);
                if(clen * clen <= (float) x && (clen + (float) 1/tmp)*(clen + (float) 1/tmp) > (float) x){
                    rez += clen - rez;
                }
            }
            tmp *= 10;
        }
        return rez;
    }    

    static String vMorse(String niz){
        niz = niz.toUpperCase();
        String rez = "";
        for(int i = 0;i<niz.length();i++){
            switch(niz.charAt(i)){
                case 'A':
                    rez += ".- ";
                    break;
                case 'B':
                    rez += "-... ";
                    break;
                case 'C':
                    rez += "-.-. ";
                    break;
                case 'D':
                    rez += "-.. ";
                    break;
                case 'E':
                    rez += ". ";
                    break;
                case 'F':
                    rez += "..-. ";
                    break;
                case 'G':
                    rez += "--. ";
                    break;
                case 'H':
                    rez += ".... ";
                    break;
                case 'I':
                    rez += ".. ";
                    break;
                case 'J':
                    rez += ".--- ";
                    break;
                case 'K':
                    rez += "-.- ";
                    break;
                case 'L':
                    rez += ".-.. ";
                    break;
                case 'M':
                    rez += "-- ";
                    break;
                case 'N':
                    rez += "-. ";
                    break;
                case 'O':
                    rez += "--- ";
                    break;
                case 'P':
                    rez += ".--. ";
                    break;
                case 'Q':
                    rez += "--.- ";
                    break;
                case 'R':
                    rez += ".-. ";
                    break;
                case 'S':
                    rez += "... ";
                    break;
                case 'T':
                    rez += "- ";
                    break;
                case 'U':
                    rez += "..- ";
                    break;
                case 'V':
                    rez += "...- ";
                    break;
                case 'W':
                    rez += ".-- ";
                    break;
                case 'X':
                    rez += "-..- ";
                    break;
                case 'Y':
                    rez += "-.-- ";
                    break;
                case 'Z':
                    rez += "--.. ";
                    break;
                case '0':
                    rez += "----- ";
                    break;
                case '1':
                    rez += ".---- ";
                    break;
                case '2':
                    rez += "..--- ";
                    break;
                case '3':
                    rez += "...-- ";
                    break;
                case '4':
                    rez += "....- ";
                    break;
                case '5':
                    rez += "..... ";
                    break;
                case '6':
                    rez += "-.... ";
                    break;
                case '7':
                    rez += "--... ";
                    break;
                case '8':
                    rez += "---.. ";
                    break;
                case '9':
                    rez += "----. ";
                    break;
                case '.':
                    rez += ".-.-.- ";
                    break;
                case ',':
                    rez += "--..-- ";
                    break;
                case '?':
                    rez += "..--.. ";
                    break;
                case '!':
                    rez += "-.-.-- ";
                    break;
                case ':':
                    rez += "---... ";
                    break;
                case ';':
                    rez += "-.-.-. ";
                    break;
                case '-':
                    rez += "-....- ";
                    break;
                case '/':
                    rez += "-..-. ";
                    break;
                case '(':
                    rez += "-.--. ";
                    break;
                case ')':
                    rez += "-.--.- ";
                    break;                    
                case ' ':
                    rez += " ";
                    break;
            }
        }
        return rez;
    }

    static double[][] zmnoziMatriki(double[][] a, double[][] b){
        if(a[0].length == b.length){
            double[][] c = new double[a.length][b[0].length];
            for(int i = 0;i<a.length;i++){
                for(int j = 0;j<b[0].length;j++){
                    for(int k = 0;k<a[0].length;k++){
                        c[i][j] += a[i][k]*b[k][j];
                    }
                }
            }
            return c;
        }
        else{
            System.out.println("Tabeli nemoremo zmnožiti!");
            return new double[0][0];
        }
    }

    static int strStej(String str, String sub){
        int n = 0;
        for(int i = 0;i<str.length();i++){
            if((i+sub.length()) > str.length()){
                break;
            }
            String subr = str.substring(i, i+sub.length());
            if(sub.equals(subr)){
                n++;
            }
        }
        return n;
    }

    static int[] pascal(int n){
        if(n == 1){
            return new int[]{1};
        }
        else if(n == 2){
            return new int[]{1,1};
        }
        int arr[] = {1,1};
        for(int j = 0;j<n-2;j++){
            int tmp[] = new int[arr.length+1];
            tmp[0] = 1;
            tmp[arr.length] = 1;
            for(int i = 0;i<arr.length-1;i++){
                tmp[i+1] = arr[i] + arr[i+1];
            }
            arr = tmp;
        }
        return arr;
    }

}