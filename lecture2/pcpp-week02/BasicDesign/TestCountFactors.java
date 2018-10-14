// For week 2
// sestoft@itu.dk * 2014-08-29
import java.util.concurrent.atomic.AtomicInteger;
class TestCountFactors {
    public static void main(String[] args) {
    final int range = 5_000_000;
    final int threadCount=10;
    final int perThread=range/threadCount;
    Thread[] threads=new Thread[threadCount];
    final AtomicInteger atomicInteger=new AtomicInteger(0);
    for(int i=0;i<threadCount;i++){
      int from=perThread*i,to= (i+1==threadCount)?range:perThread*(i+1);

      threads[i]=new Thread(()->{
      for (int p=from; p<to; p++)
        atomicInteger.addAndGet(countFactors(p));
      });
    }
      for (int t=0; t<threadCount; t++)
        threads[t].start();
      try {
        for (int t=0; t<threadCount; t++)
          threads[t].join();
      } catch (InterruptedException exn) { }
    System.out.printf("Total number of factors is %9d%n", atomicInteger.get());
  }

  public static int countFactors(int p) {
    if (p < 2)
      return 0;
    int factorCount = 1, k = 2;
    while (p >= k * k) {
      if (p % k == 0) {
	factorCount++;
	p /= k;
      } else
	k++;
    }
    return factorCount;
  }
}
/*class MyAtomicInteger{
  private int sum=0;
  public synchronized int addAndGet(int amount){
    return sum+=amount;
  }
  public synchronized int get(){
    return sum;
  }
}*/
