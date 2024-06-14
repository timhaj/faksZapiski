
abstract class Lik {
  abstract public double obseg();
}

class Pravokotnik extends Lik{
    int a;
    int b;

    Pravokotnik(int a, int b){
        this.a = a;
        this.b = b;
    }

    public double obseg(){
        return 2*(this.a+this.b);
    }
}

class NKotnik extends Lik{
    int st_stranic;
    int a;

    NKotnik(int st_stranic, int a){
        this.st_stranic = st_stranic;
        this.a = a;
    }

    public double obseg(){
        return this.st_stranic*this.a;
    }
}

class Kvadrat extends Lik{
    int a;

    Kvadrat(int a){
        this.a = a;
    }

    public double obseg(){
        return 4*this.a;
    }
}

public class DN08{

    public static void main(String args[]){
        Lik liki[] = new Lik[args.length];
        for(int i = 0;i<args.length;i++){
            String data[] = args[i].split(":");
            switch(data[0]){
                case "kvadrat":
                    liki[i] = new Kvadrat(Integer.parseInt(data[1]));
                    break;
                case "pravokotnik":
                    liki[i] = new Pravokotnik(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                    break;
                case "nkotnik":
                    liki[i] = new NKotnik(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                    break;
            }
        }
        skupniObseg(liki);
    }


    public static void skupniObseg(Lik liki[]){
        int n = 0;
        for(int i = 0;i<liki.length;i++){
            n += liki[i].obseg();
        }
        System.out.println(n);
    }

}