public class DN04 {
    public static void main(String[] args){
        if(args.length != 0){
            for(int i = 0;i<args[0].length()/8;i++){
                String podniz = args[0].substring(i*8, (i+1)*8);
                int vrednost = Integer.parseInt(String.valueOf(podniz),2);
                char crka = (char) vrednost;
                System.out.print(crka);
            }
        }
    }
}