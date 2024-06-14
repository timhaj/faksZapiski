import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.TreeMap;

class Tekmovalec {
    String drzava;
    String izvajalec;
    String naslov_pesmi;

    public Tekmovalec(String drzava, String izvajalec, String naslov_pesmi){
        this.drzava = drzava;
        this.izvajalec = izvajalec;
        this.naslov_pesmi = naslov_pesmi;
    }

    public String getDrzava(){
        return this.drzava;
    }

    public String getIzvajalec(){
        return this.izvajalec;
    }

    public String getNaslovPesmi(){
        return this.naslov_pesmi;
    }

    public String toString(){
        return String.format("(%s) %s - %s", this.drzava, this.izvajalec, this.naslov_pesmi);
    }

}

class Glas {
    String odDrzave;
    String zaDrzavo;
    int stTock;

    public Glas(String od, String za, int tocke){
        this.odDrzave = od;
        this.zaDrzavo = za;
        this.stTock = tocke;
    }

    public String getOdDrzave(){
        return this.odDrzave;
    }

    public String getZaDrzavo(){
        return this.zaDrzavo;
    }

    public int getStTock(){
        return this.stTock;
    }

    public String toString(){
        return String.format("%s --%dt-> %s", this.odDrzave, this.stTock, this.zaDrzavo);
    }

}

class LocenGlas extends Glas{
    private int stTockGlasovi;
    private int stTockZirija;

    public LocenGlas(String odDrzave, String zaDrzavo, int stTock, int stTockGlasovi, int stTockZirije){
        super(odDrzave, zaDrzavo, stTock);
        this.stTockGlasovi = stTockGlasovi;
        this.stTockZirija = stTockZirije;
    }

    public int getStTockGlasovi(){
        return this.stTockGlasovi;
    }

    public int getStTockZirija(){
        return this.stTockZirija;
    }
}

class UtezeniKriterij implements Kriterij{
    float utezGlasovanja;
    float utezZirija;

    public UtezeniKriterij(float utezGlasovanja, float utezZirije){
        this.utezGlasovanja = utezGlasovanja;
        this.utezZirija = utezZirije;
    }

    public int steviloTock(Tekmovanje tekmovanje, String drzava){
        double skup = 0;
        double zirija = 0;
        double glas = 0;
        for(int i = 0;i<tekmovanje.seznamGlasov.size();i++){
            Glas cur = tekmovanje.seznamGlasov.get(i);
            if(drzava.equals(cur.zaDrzavo)){
                if(cur instanceof LocenGlas){
                    LocenGlas curr = (LocenGlas) cur;
                    zirija += curr.getStTockZirija() * utezZirija;
                    glas += curr.getStTockGlasovi() * utezGlasovanja;
                }
                else{
                    skup += cur.stTock * utezZirija;
                }
            }
        }
        return (int) Math.round(skup + glas + zirija);
    }

}

class ZgodovinaTekmovanj{
    ArrayList<Tekmovanje> seznamTekmovanj;

    public ZgodovinaTekmovanj(ArrayList<Tekmovanje> tek){
        this.seznamTekmovanj = tek;
    }

    public static ZgodovinaTekmovanj izDatotek(String datotekaTekmovalci, String datotekaGlasovi) {
        try {
            ArrayList<Tekmovanje> tekmovanja = new ArrayList<>();
            ArrayList<Integer> leta = new ArrayList<>();
            TreeMap<Integer, ArrayList<Tekmovalec>> tekmovalci = new TreeMap<>();
            TreeMap<Integer, ArrayList<Glas>> glasovi = new TreeMap<>();
            Scanner sc = new Scanner(new File(datotekaTekmovalci));
            sc.nextLine();
            while (sc.hasNextLine()) {
                String data[] = sc.nextLine().split(";");
                int leto = Integer.parseInt(data[0]);
                if (!tekmovalci.containsKey(leto)) {
                    leta.add(leto);
                    ArrayList<Tekmovalec> tmp = new ArrayList<>();
                    tekmovalci.put(leto, tmp);
                }
                Tekmovalec tmp = new Tekmovalec(data[1], data[2], data[3]);
                tekmovalci.get(leto).add(tmp);
            }
            sc.close();
            sc = new Scanner(new File(datotekaGlasovi));
            sc.nextLine();
            while (sc.hasNextLine()) {
                String data[] = sc.nextLine().split(";");
                int leto = Integer.parseInt(data[0]);
                if (!glasovi.containsKey(leto)) {
                    ArrayList<Glas> tmp = new ArrayList<>();
                    glasovi.put(leto, tmp);
                }
                if (leto < 2016) {
                    Glas tmp = new Glas(data[2], data[3], Integer.parseInt(data[4]));
                    glasovi.get(leto).add(tmp);
                }
                else {
                    if (data.length < 7) {
                        LocenGlas tmp = new LocenGlas(data[2], data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]), 0);
                        glasovi.get(leto).add(tmp);
                    }
                    else {
                        LocenGlas tmp = new LocenGlas(data[2], data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]));
                        glasovi.get(leto).add(tmp);
                    }
                }
            }
            sc.close();
            for(int i = 0;i<leta.size();i++){
                int leto = leta.get(i);
                tekmovanja.add(new Tekmovanje(tekmovalci.get(leto), glasovi.get(leto)));
            }
            return new ZgodovinaTekmovanj(tekmovanja);
        }
        catch(Exception e) {
            return null;
        }
    }

    public int getSkupnoSteviloTock(String drzava){
        int skupno = 0;
        for(Tekmovanje tmp : this.seznamTekmovanj){
            skupno += tmp.steviloTock(drzava);
        }
        return skupno;
    }

    public float povprecnaUvrstitev(String drzava){
        int skupno = 0;
        int n = 0;
        for(Tekmovanje tmp : this.seznamTekmovanj){
            if(!(tmp.getMesto(drzava) == -1)){
                skupno += tmp.getMesto(drzava);
                n++;
            }
        }
        return (float) skupno /n;
    }

    public int najboljsaUvrstitev(String drzava){
        int naj = 999;
        for(Tekmovanje tmp : this.seznamTekmovanj){
            if(tmp.getMesto(drzava) < naj && tmp.getMesto(drzava) != -1){
                naj = tmp.getMesto(drzava);
            }
        }
        return naj;
    }

}

class Tekmovanje {
    ArrayList<Tekmovalec> seznamTekmovalcev;
    ArrayList<Glas> seznamGlasov;
    Kriterij kriterij;
    boolean urejeno = false;

    public ArrayList<Tekmovalec> getSeznamTekmovalcev(){
        return this.seznamTekmovalcev;
    }

    public ArrayList<Glas> getSeznamGlasov(){
        return this.seznamGlasov;
    }

    public Tekmovanje(ArrayList<Tekmovalec> tek, ArrayList<Glas> gla){
        this.seznamTekmovalcev = tek;
        this.seznamGlasov = gla;
        this.kriterij = new OsnovniKriterij();
    }

    public void setKriterij(Kriterij kriterij){
        this.kriterij = kriterij;
        this.urejeno = false;
    }

    public static Tekmovanje izDatotek(String datotekaTekmovalci, String datotekaGlasovi){
        try{
            ArrayList<Tekmovalec> tekmovalci = new ArrayList<>();
            ArrayList<Glas> glasovi = new ArrayList<>();
            Scanner sc = new Scanner(new File(datotekaTekmovalci));
            sc.nextLine();
            while(sc.hasNextLine()){
                String data[] = sc.nextLine().split(";");
                Tekmovalec tmp = new Tekmovalec(data[1], data[2], data[3]);
                tekmovalci.add(tmp);
            }
            sc.close();
            sc = new Scanner(new File(datotekaGlasovi));
            sc.nextLine();
            while(sc.hasNextLine()){
                String data[] = sc.nextLine().split(";");
                if(Integer.parseInt(data[0]) < 2016) {
                    Glas tmp = new Glas(data[2], data[3], Integer.parseInt(data[4]));
                    glasovi.add(tmp);                
                }
                else{
                    if(data.length < 7){
                        LocenGlas tmp = new LocenGlas(data[2], data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]), 0);
                        glasovi.add(tmp);
                    }
                    else{
                        LocenGlas tmp = new LocenGlas(data[2], data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]));
                        glasovi.add(tmp);
                    }
                }
            }
            sc.close();
            return new Tekmovanje(tekmovalci, glasovi);
        }
        catch(Exception e){
            return null;            
        }
    }

    public void izpisiTekmovalce(){
        if(this.seznamTekmovalcev.size() == 0){
            System.out.println("Seznam tekmovalcev je prazen.");
        }
        else{
            System.out.println("Seznam tekmovalcev:");
            for(Tekmovalec tmp : this.seznamTekmovalcev){
                System.out.println(tmp);
            }
        }
    }

    public void izpisiGlasove(){
        if(this.seznamGlasov.size() == 0){
            System.out.println("Seznam glasov je prazen.");
        }
        else{
            System.out.println("Seznam glasov:");
            for(Glas tmp : this.seznamGlasov){
                System.out.println(tmp);
            }
        }
    }

    public int steviloTock(String drzava){
        return kriterij.steviloTock(this, drzava);
    }

    public void izpisiTocke(){
        if(this.seznamTekmovalcev.size() == 0){
            System.out.println("Seznam tekmovalcev je prazen.");
        }
        else{
            System.out.println("Seznam tekmovalcev in njihovih tock:");
            for(Tekmovalec tmp : this.seznamTekmovalcev){
                System.out.printf("%s: %dt\n", tmp, steviloTock(tmp.drzava));
            }
        }        
    }

    public Tekmovalec getZmagovalec(){
        int max = -1;
        Tekmovalec cur = new Tekmovalec("", "", "");
        for(Tekmovalec tmp : this.seznamTekmovalcev){
            if(steviloTock(tmp.drzava) > max){
                cur = tmp;
                max = steviloTock(tmp.drzava);
            }
        }
        return cur;
    }

    private void urediPoTockah(){
        int tocke[] = new int[this.seznamTekmovalcev.size()];
        for(int i = 0;i<this.seznamTekmovalcev.size();i++){
            tocke[i] = steviloTock(this.seznamTekmovalcev.get(i).drzava);
        }
        for(int i = 0;i<this.seznamTekmovalcev.size();i++){
            for(int j = 0;j<this.seznamTekmovalcev.size() - 1 - i;j++){
                if(tocke[j] < tocke[j+1]){
                    Tekmovalec tmp = this.seznamTekmovalcev.get(j);
                    this.seznamTekmovalcev.set(j, this.seznamTekmovalcev.get(j+1));
                    this.seznamTekmovalcev.set(j+1, tmp);
                    int temp = tocke[j];
                    tocke[j] = tocke[j+1];
                    tocke[j+1] = temp;
                }
            }
        }
        this.urejeno = true;
    }

    public int getMesto(String drzava){
        urediPoTockah();
        int mesto = -1;
        for(int i = 0;i<this.seznamTekmovalcev.size();i++){
            if(this.seznamTekmovalcev.get(i).drzava.equals(drzava)){
                mesto = i;
            }
        }
        if(mesto == -1){
            return -1;
        }
        return mesto + 1;
    }

    public void izpisiRezultateUrejeno(int topK){
        urediPoTockah();
        int terms = topK <= this.seznamTekmovalcev.size() ? topK : this.seznamTekmovalcev.size();
        System.out.println("Najboljse uvrsceni tekmovalci:");
        for(int i = 0;i<terms;i++){
            Tekmovalec tmp = this.seznamTekmovalcev.get(i);
            System.out.printf("%d. %s: %dt\n", i+1, tmp, steviloTock(tmp.drzava));
        }
    }

}

interface Kriterij {
    int steviloTock(Tekmovanje tekmovanje, String drzava);
}

class OsnovniKriterij implements Kriterij{
    public int steviloTock(Tekmovanje tekmovanje, String drzava){
        int n = 0;
        for(Glas tmp : tekmovanje.seznamGlasov){
            if(drzava.equals(tmp.zaDrzavo)){
                n += tmp.stTock;
            }
        }
        return n;
    }
}

public class DN09 {
    public static void main(String args[]) {
        if (args.length > 0) {
            Tekmovanje tek = Tekmovanje.izDatotek(args[1], args[2]);
            switch (args[0]) {
                case "izpisiTekmovanje":
                    tek.izpisiTekmovalce();
                    System.out.println();
                    tek.izpisiGlasove();
                    break;
                case "izpisiTocke":
                    tek.izpisiTocke();
                    break;
                case "najboljse":
                    tek.izpisiRezultateUrejeno(Integer.parseInt(args[3]));
                    break;
                case "utezeno":
                    UtezeniKriterij krit = new UtezeniKriterij(Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                    tek.setKriterij(krit);
                    tek.izpisiRezultateUrejeno(Integer.parseInt(args[3]));
                    break;
            }
        }
    }
}