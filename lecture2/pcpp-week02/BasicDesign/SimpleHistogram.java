// For week 2
// sestoft@itu.dk * 2014-09-04

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.LongAdder;

class SimpleHistogram {
  public static void main(String[] args) {
    final Histogram histogram = new Histogram5(30);
    final int range = 5_000_000;
    final int threadCount=10;
    final int perThread=range/threadCount;
    Thread[] threads=new Thread[threadCount];
    for(int i=0;i<threadCount;i++){
      int from=perThread*i,to= (i+1==threadCount)?range:perThread*(i+1);

      threads[i]=new Thread(()->{
        for (int p=from; p<to; p++)
          histogram.increment(countFactors(p));
      });
    }
    for (int t=0; t<threadCount; t++)
      threads[t].start();
    try {
      for (int t=0; t<threadCount; t++)
        threads[t].join();
    } catch (InterruptedException exn) { }
    //histogram.increment(7);
    //histogram.increment(7);
    //histogram.increment(13);
    dump(histogram);
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
}

interface Histogram {
  public void increment(int bin);
  public int getCount(int bin);
  public int getSpan();
  public int[] getBins();
}

class Histogram1{
  private final int[] counts;
  public Histogram1(int span) {
    this.counts = new int[span];
  }
  public void increment(int bin) {
    counts[bin] = counts[bin] + 1;
  }
  public int getCount(int bin) {
    return counts[bin];
  }
  public int getSpan() {
    return counts.length;
  }
}
class Histogram2 implements Histogram
{
  private final int[] counts;
  public Histogram2(int span) {this.counts = new int[span];}
  public synchronized void increment(int bin) {
    counts[bin] = counts[bin] + 1;
  }
  public synchronized int getCount(int bin) {
    return counts[bin];
  }
  public int getSpan() {
    return counts.length;
  }
  public int[] getBins(){
     //return counts;//live view
     return counts.clone();//fixed view
  }
}
class Histogram3 implements Histogram
{
  private final AtomicInteger[] counts;
  public Histogram3(int span) {
    this.counts = new AtomicInteger[span];
    initHistogram();
  }
  public void increment(int bin){counts[bin].addAndGet(1);}
  public int getCount(int bin) {return counts[bin].get();}
  public int getSpan() {return counts.length;}
  private void initHistogram(){
    for(int i=0;i<getSpan();i++){
      counts[i]=new AtomicInteger(0);
    }
  }
  @Override
  public int[] getBins() {
    int[] deepCopy = new int[getSpan()];
    for (int i = 0; i < getSpan(); i++) {
      deepCopy[i] = counts[i].get();
    }
    return deepCopy; // a fixed snapshot (not a live view)
  }

}
class Histogram4 implements Histogram
{
  private final AtomicIntegerArray counts;
  public Histogram4(int span) {
    this.counts = new AtomicIntegerArray(span);
  }
  public void increment(int bin){counts.incrementAndGet(bin);}
  public int getCount(int bin) {return counts.get(bin);}
  public int getSpan() {return counts.length();}
  @Override
  public int[] getBins() {
    int[] deepCopy = new int[getSpan()];
    for (int i = 0; i < getSpan(); i++) {
      deepCopy[i] = counts.get(i);
    }
    return deepCopy; // a fixed snapshot (not a live view)
  }
}
class Histogram5 implements Histogram
{
  private final LongAdder[] counts;
  public Histogram5(int span) {
    this.counts = new LongAdder[span];
    initHistogram();
  }
  public void increment(int bin){counts[bin].increment();}
  public int getCount(int bin) {return counts[bin].intValue();}
  public int getSpan() {return counts.length;}
  private void initHistogram() {
    for (int i = 0; i < getSpan(); i++) {
      counts[i] = new LongAdder();
    }
  }
  @Override
  public int[] getBins() {
    int[] deepCopy = new int[getSpan()];
    for (int i = 0; i < getSpan(); i++) {
      deepCopy[i] = counts[i].intValue();
    }
    return deepCopy; // a fixed snapshot (not a live view)
  }
}
