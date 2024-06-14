public class DN02{
    public static void main(String[] args){
        if(args.length != 0){
            int dolzina = args[0].length();
            int stranica = (int) Math.ceil(Math.sqrt(dolzina));
            int tmp = 0;
            for(int i = 0;i<stranica;i++){
                for(int j = 0;j<stranica;j++){
                    if(tmp >= dolzina){
                        System.out.print(".  ");
                    }
                    else{
                        System.out.print(args[0].charAt(tmp) + "  ");
                    }
                    tmp++;
                }
                System.out.println();
            }
        }
    }
}