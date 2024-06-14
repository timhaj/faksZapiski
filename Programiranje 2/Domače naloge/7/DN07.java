import java.lang.Math;
import java.util.Scanner;
import java.io.File;

class Planet {
    public String ime;
    public int radij;

    public double povrsina(){
        return (double) this.radij*4*Math.PI*this.radij;
    }

    public void setIme(String ime){
        this.ime = ime.toLowerCase();
    }

    public void setRadij(int radij){
        this.radij = radij;
    }

    public String getIme(){
        return this.ime;
    }

    public int getRadij(){
        return this.radij;
    }

}

public class DN07{

    public static void main(String args[]){
        Planet planeti[] = preberi(args[0]);
        izpisiPovrsino(args[1], planeti);
    }

    public static Planet[] preberi(String datoteka){
        Planet planeti[] = new Planet[8];
        try{
            Scanner sc = new Scanner(new File(datoteka));
            int i = 0;
            while(sc.hasNextLine()){
                planeti[i] = new Planet();
                String info[] = sc.nextLine().split(":");
                planeti[i].setIme(info[0]);
                planeti[i].setRadij(Integer.parseInt(info[1]));
                i++;
            }
            sc.close();
            return planeti;
        }
        catch (Exception e){
            System.out.println("Napaka pri odpiranju datoteke.");
        }
        return planeti;
    }

    public static void izpisiPovrsino(String arg, Planet planeti[]){
        String imenaPlanetov[] = arg.split("[+]");
        double skupnaPovrsina = 0.0;
        for(int i = 0;i<imenaPlanetov.length;i++){
            for(int j = 0;j<planeti.length;j++){
                if(imenaPlanetov[i].toLowerCase().equals(planeti[j].getIme())) {
                    skupnaPovrsina += planeti[j].povrsina();
                }
            }
        }
        skupnaPovrsina /= 1000000;
        System.out.printf("Povrsina planetov \"%s\" je %d milijonov km2", arg, (int) skupnaPovrsina);
    }

}