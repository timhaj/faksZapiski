public class DN06{
    public static void main(String args[]){
        StdDraw.setScale(0,500);
        StdDraw.enableDoubleBuffering();
        for(int i = 0;i<=9;i++){
            if(i % 3 == 0){
                StdDraw.setPenRadius(0.01);
                StdDraw.line(50, i*40 + 50, 450, i*40 + 50);
                StdDraw.line(i*44.44 + 50, 50, i*44.44 + 50, 410);
            }
            else{
                StdDraw.setPenRadius(0.0025);
                StdDraw.line(50, i*40 + 50, 450, i*40 + 50);
                StdDraw.line(i*44.44 + 50, 50, i*44.44 + 50, 410);
            }
        }
        int n = 0;
        for(int i = 8;i>=0;i--){
            for(int j = 0;j<9;j++){
                if(args[0].charAt(n) != '0'){
                    StdDraw.text(j*44.4 + 72,i*39.5 + 71, Character.toString(args[0].charAt(n)));
                }
                n++;
            }
        }
        StdDraw.show();
    }
}