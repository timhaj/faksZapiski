import java.util.Random;
import java.util.Scanner;
import java.io.File;

public class DN03{
    public static void main(String[] args){
        Random rand = new Random(Integer.parseInt(args[2]));
        int dolzina = Integer.parseInt(args[1]);
        String geslo = "";
        try {
            Scanner sc = new Scanner(new File(args[0]));
            int s = sc.nextInt();
            String[] besede = new String[s];
            for(int i = 0;sc.hasNext();i++){
                besede[i] = sc.next();
            }
            sc.close();
            for(int i = 0;i<dolzina;i++){
                int n = rand.nextInt(s);
                int velikost = besede[n].length();
                geslo += besede[n].charAt(rand.nextInt(velikost));
            }
        } catch(Exception e){
            System.out.println("Datoteke ni mogoče najti");
        }
        System.out.println(geslo);
    }
}