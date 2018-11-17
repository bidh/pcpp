import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;


public class CASHistogram implements Histogram{
    public static void main(String[] args) {
        countPrimeFactorsWithStmHistogram();
    }

    private static void countPrimeFactorsWithStmHistogram() {
        final Histogram histogram = new CASHistogram(30);
        final Histogram total = new CASHistogram(30);
        final int range = 4_000_000;
        final int threadCount = 10, perThread = range / threadCount;
        final CyclicBarrier startBarrier = new CyclicBarrier(threadCount + 1),
                stopBarrier = startBarrier;
        final Thread[] threads = new Thread[threadCount];
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t,
                    to = (t+1 == threadCount) ? range : perThread * (t+1);
            threads[t] =
                    new Thread(() -> {
                        try { startBarrier.await(); } catch (Exception exn) { }
                        for (int p=from; p<to; p++)
                            histogram.increment(countFactors(p));
                        System.out.print("*");
                        try { stopBarrier.await(); } catch (Exception exn) { }
                    });
            threads[t].start();
        }
        try {
            startBarrier.await();
        } catch (Exception exn) {
        }

        for (int i = 0; i < 200; i++) {
            total.transferBins(histogram);
            try {
                Thread.sleep(30);
            } catch (InterruptedException exc) {
            }
        }

        try {
            stopBarrier.await();
        } catch (Exception exn) {
        }
        dump(histogram);
        dump(total);
        //total.transferBins(total);
        //dump(total);
    }

    public static void dump(Histogram histogram) {
        int totalCount = 0;
        for (int bin=0; bin<histogram.getSpan(); bin++) {
            System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
            totalCount += histogram.getCount(bin);
        }
        System.out.printf("      %9d%n", totalCount);
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
    AtomicInteger[] counts;
    public CASHistogram(int span){
        counts=new AtomicInteger[span];
        initHistogram();
    }
    private void initHistogram(){
        for(int i=0;i<getSpan();i++){
            counts[i]=new AtomicInteger(0);
        }
    }
    @Override
    public void increment(int bin) {
        int value;
        do{
            value=getCount(bin);
        }while(! counts[bin].compareAndSet(value,value+1));
    }

    @Override
    public int getCount(int bin) {
        return counts[bin].get();
    }

    @Override
    public int getSpan() {
        return counts.length;
    }

    @Override
    public int[] getBins() {
        int[] deepCopy = new int[getSpan()];
        for (int i = 0; i < getSpan(); i++) {
            deepCopy[i] = counts[i].get();
        }
        return deepCopy; // a fixed snapshot (not a live view)
    }

    @Override
    public int getAndClear(int bin) {
        int value;
        do{
            value=counts[bin].get();
        }while(!counts[bin].compareAndSet(value,0));
        return value;
    }

    @Override
    public void transferBins(Histogram hist) {
        final CASHistogram thisHistogram=this, thatHistogram=(CASHistogram)hist;
        int thisCount;
        for(int i=0;i<this.counts.length;i++){
            int thatCount = thatHistogram.getAndClear(i);
            do{
                thisCount=thisHistogram.counts[i].get();
            }while(!this.counts[i].compareAndSet(thisCount,thisCount+thatCount));
        }
    }
}
interface Histogram{
    void increment(int bin);
    int getCount(int bin);
    int getSpan();
    int[] getBins();
    int getAndClear(int bin);
    void transferBins(Histogram hist);
}
class Timer {
    private long start = 0, spent = 0;
    public Timer() { play(); }
    public double check() { return (start==0 ? spent : System.nanoTime()-start+spent)/1e9; }
    public void pause() { if (start != 0) { spent += System.nanoTime()-start; start = 0; } }
    public void play() { if (start == 0) start = System.nanoTime(); }
}