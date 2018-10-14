import java.util.Arrays;

public class Exercise4_2 {
    private static boolean isPrime(int n) {
        int k = 2;
        while (k * k <= n && n % k != 0)
            k++;
        return n >= 2 && k * k > n;
    }
    public static void main(String[] args){
        final int N=10_000_001;
        int[] arr=new int[N];
        //Exercise 4.2.1
        Arrays.parallelSetAll(arr, x->isPrime(x) ?1 : 0);
        System.out.println(Arrays.toString(arr));
        //Exercise 4.2.2
        Arrays.parallelPrefix(arr, (x,y)-> x+y );
        System.out.println(arr[N-1]);
        //Exercise 4.2.3
        final int from = (N-1)/10;
        for(int i=from; i<N;i+=from){
            double ratio = arr[i] / (i/Math.log(i));
            System.out.println(ratio);
        }
    }
}
