public class Izziv1 {
    private static int[] generateTable(int n){
        int[] arr = new int[n];
        for(int i=0;i<n;i++){
            arr[i] = i+1;
        }
        return arr;
    }

    private static int findLinear(int[] a, int v){
        for(int i=0;i<a.length;i++){
            if(a[i] == v){
                return i;
            }
        }
        return -1;
    }

    private static int findBinary(int[] a, int l, int r, int v){ //rekurzivno se klice, zato mas l pa r notri pod args
        if (r >= l) {
            int sredina = (r + l) / 2;
            if (a[sredina] == v)
                return sredina;
            if (a[sredina] > v)
                return findBinary(a, l, sredina - 1, v);
            return findBinary(a, sredina + 1, r, v);
        }
        return -1;
    }

    static long timeLinear(int n){
        int[] arr = generateTable(n);
        long startTime = System.nanoTime();
        for(int i=0;i<1000;i++){
            int rand = (int) (Math.random() * n) + 1;
            int r = findLinear(arr, rand);
        }
        return (System.nanoTime() - startTime) / 1000;
    }

    static long timeBinary(int n){
        int[] arr = generateTable(n);
        long startTime = System.nanoTime();
        for(int i=0;i<1000;i++){
            int rand = (int) (Math.random() * n) + 1;
            int r = findBinary(arr, 0, n-1, rand);
        }
        return (System.nanoTime() - startTime) / 1000;
    }

    public static void main(String[] args) {
        System.out.println("   n       |     linearno  |   dvojisko  |\n" +
                "-----------+--------------+----------------");
        for(int n = 20000;n<=1000000;n+=20000){
            String form = String.format("%10d  | %10d  | %10d", n, timeLinear(n), timeBinary(n));
            System.out.println(form);
        }
    }
}
