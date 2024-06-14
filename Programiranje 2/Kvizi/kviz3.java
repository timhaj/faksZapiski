class Krogla{
    double polmer;

    public Krogla(double polmer){
        this.polmer = polmer;
    }

    public void setPolmer(double polmer){
        this.polmer = polmer;
    }

    public double getPolmer(){
        return this.polmer;
    }

    public double getPremer(){
        return 2*this.polmer;
    }

    public double getObseg(){
        return 2*Math.PI*this.polmer;
    }

    public double getPovrsina(){
        return 4*Math.PI*this.polmer*this.polmer;
    }

    public double getVolumen(){
        return 4.0/3.0*Math.PI*this.polmer*this.polmer*this.polmer;
    }

    public String toString(){
        return String.format("[ Krogla polmer=%.1f ]", this.polmer);
    }

}


public class kviz3{
    public static void main(String args[]){

    }

    public static int[] zmnoziPolinoma(int a[], int b[]){
        int arr[] = new int[(a.length-1)+(b.length-1)+1];
        //obrnemo tabeli
        int tmp[] = new int[a.length];
        for(int i = 0;i<a.length;i++){
            tmp[i] = a[a.length-i-1];
        }
        a = tmp;
        tmp = new int[b.length];
        for(int i = 0;i<b.length;i++){
            tmp[i] = b[b.length-i-1];
        }
        b = tmp;
        //zmnozimo
        for(int i = 0;i<a.length;i++){
            for(int j = 0;j<b.length;j++){
                arr[i+j] += a[i]*b[j];
            }
        }
        tmp = new int[arr.length];
        for(int i = 0;i<arr.length;i++){
            tmp[i] = arr[arr.length-i-1];
        }
        arr = tmp;
        return arr;
    }

    public static int[] sestejPolinoma(int[] a, int[] b){
        int arr[] = new int[a.length > b.length ? a.length : b.length];
        for(int i = 0;i<arr.length;i++){
            if(i < a.length){
                arr[i] += a[i];
            }
            if(i < b.length){
                arr[i] += b[i];
            }
        }
        return arr;
    }
}