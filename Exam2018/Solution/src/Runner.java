import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;
import java.util.function.*;

public class Runner {
    public static void main(String[] args) {
       // final int numberOfAccounts = 1;
       // testAccounts(new UnsafeAccounts(numberOfAccounts), numberOfAccounts);
        /* Question 1.2
        final int nPairs=1;
        final int nTrials=1000;

        ConcurrentTestRaceCondition concurrentTestRaceCondition=
                new ConcurrentTestRaceCondition(new UnsafeAccounts(numberOfAccounts),numberOfAccounts,nPairs,nTrials);
        ExecutorService service= Executors.newCachedThreadPool();
        concurrentTestRaceCondition.test(service);*/


       //Question 1.3
       /*
        //testing 1.2
        final int numberOfAccounts=1;
        final int nPairs=1;
        final int nTrials=1000;

        ConcurrentTestRaceCondition concurrentTestRaceCondition=
                new ConcurrentTestRaceCondition(new LockAccounts(numberOfAccounts),numberOfAccounts,nPairs,nTrials);
        ExecutorService service= Executors.newCachedThreadPool();
        concurrentTestRaceCondition.test2(service); */

        //testing 1.1
       /* final int numberOfAccounts = 100;
        final int nPairs=8;
        final int nTrials=100_000;

        ConcurrentTestRaceCondition concurrentTestRaceCondition=
                new ConcurrentTestRaceCondition(new LockAccounts(numberOfAccounts),numberOfAccounts,nPairs,nTrials);
        ExecutorService service= Executors.newCachedThreadPool();
        concurrentTestRaceCondition.test1(service);
        System.out.println("passed");*/

        final int numberOfAccounts = 100;
        final int nPairs=8;
        final int nTrials=100_000;

        ConcurrentTestRaceCondition concurrentTestRaceCondition=
                new ConcurrentTestRaceCondition(new LockAccountsFast(numberOfAccounts),numberOfAccounts,nPairs,nTrials);
        ExecutorService service= Executors.newCachedThreadPool();
        concurrentTestRaceCondition.test1(service);
        System.out.println("passed");


/*
        final int numberOfTransactions = 1000;
        applyTransactionsLoop(numberOfAccounts, numberOfTransactions, () -> new UnsafeAccounts(numberOfAccounts));
        applyTransactionsCollect(numberOfAccounts, numberOfTransactions, () -> new UnsafeAccounts(numberOfAccounts));*/
    }

    public static void testAccounts(Accounts accounts, final int n) {
        if (n <= 2) {
            System.out.println("Accounts must be larger that 2 for this test to work");
            assert (false); // test only supports larger accounts that 2.
            return;
        }
        assert (accounts.sumBalances() == 0);
        accounts.deposit(n - 1, 55);
        assert (accounts.get(n - 1) == 55);
        assert (accounts.get(n - 2) == 0);
        assert (accounts.sumBalances() == 55);
        accounts.deposit(0, 45);
        assert (accounts.sumBalances() == 100);

        accounts.transfer(0, n - 1, -10);
        assert (accounts.sumBalances() == 100);
        assert (accounts.get(n - 1) == 45);
        assert (accounts.get(0) == 55);
        accounts.transfer(1, n - 1, 10);
        assert (accounts.get(n - 1) == 55);
        assert (accounts.get(1) == -10);
        assert (accounts.get(0) == 55);
        assert (accounts.sumBalances() == 100);

        accounts.transferAccount(accounts);
        assert (accounts.get(n - 1) == 55 * 2);
        assert (accounts.get(1) == -10 * 2);
        assert (accounts.get(0) == 55 * 2);
        assert (accounts.sumBalances() == 200);

        System.out.printf(accounts.getClass() + " passed sequential tests\n");
    }
    // Question 1.7.1
    private static void applyTransactionsLoop(int numberOfAccounts, int numberOfTransactions,
            Supplier<Accounts> generator) {
        // remember that if "from" is -1 in transaction then it is considered a deposit
        // otherwise it is a transfer.
        final Accounts accounts = generator.get();
        Stream<Transaction> transaction = IntStream.range(0, numberOfTransactions).parallel()
                .mapToObj((i) -> new Transaction(numberOfAccounts, i));
        // implement applying each transaction by using a for-loop
        // Modify it to run with a parallel stream.
 // YOUR CODE GOES HERE 
    }

    // Question 1.7.2
    private static void applyTransactionsCollect(int numberOfAccounts, int numberOfTransactions,
                                                 Supplier<Accounts> generator) {
        // remember that if "from" is -1 in transaction then it is considered a deposit
        // otherwise it is a transfer.
        Stream<Transaction> transactions = IntStream.range(0, numberOfTransactions).parallel()
                .mapToObj((i) -> new Transaction(numberOfAccounts, i));

        // Implement applying each transaction by using the collect stream operator.
        // Modify it to run with a parallel stream.
 // YOUR CODE GOES HERE 
    }
}
class ConcurrentTestRaceCondition extends Test{
    protected CyclicBarrier startBarrier,stopBarrier;
    protected final int nPairs, nrTrials;
    protected final Accounts accounts;
    protected final AtomicInteger getSum,depositSum;
    protected final int numberOfAccounts;

    public ConcurrentTestRaceCondition(Accounts accounts,int numberOfAccounts, int nPairs,
                                       int nTrials){
        this.accounts=accounts;
        this.nPairs=nPairs;
        this.nrTrials=nTrials;
        this.numberOfAccounts=numberOfAccounts;
        getSum=new AtomicInteger(0);
        depositSum=new AtomicInteger(0);
        this.startBarrier = new CyclicBarrier(nPairs + 1);
        this.stopBarrier = new CyclicBarrier(nPairs + 1);;
    }
    void test1(ExecutorService pool){
        try{
            for(int i=0;i<nPairs;i++){
                pool.execute(new Depositer());
            }
            startBarrier.await();
            stopBarrier.await();
            int sumBalance=accounts.sumBalances();
            assertEquals(sumBalance,depositSum.get());
            System.out.println(sumBalance +" equals "+depositSum.get());
            for(int i:((LockAccountsFast)accounts).sums){
                System.out.println(i);
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }
    void test2(ExecutorService pool){
        try{
            for(int i=0;i<nPairs;i++){
                pool.execute(new Depositer());
            }
            pool.execute(new Reader());
            startBarrier.await();
            stopBarrier.await();
            int sumBalance=accounts.sumBalances();
            assertEquals(sumBalance,depositSum.get());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }
    class Depositer implements Runnable{
        public void run(){
            try{
                int sum=0;
                startBarrier.await();
                for(int i=0;i<numberOfAccounts;i++){
                    for (int j=1;j<=nrTrials;j++){
                        accounts.deposit(i,1);
                        sum+=1;
                    }
                }
                depositSum.getAndAdd(sum);
                stopBarrier.await();
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }
    }
    class Reader implements Runnable{
        int threshold=500;
        public void run(){
            try{
                startBarrier.await();
                boolean sumTooHigh = false;
                while(!sumTooHigh){
                    int temp=accounts.get(0);
                    if(temp>threshold){
                        System.out.println("Balance is : "+temp);
                        sumTooHigh = true;
                    }
                }
                System.out.println("Balance is higher than threshold : "+ threshold);
                stopBarrier.await();
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }
    }
}
class Test {
    public static void assertEquals(int x, int y) throws Exception {
        if (x != y)
            throw new Exception(String.format("ERROR: %s not equal to %s%n", x, y));
    }

    public static void assertTrue(boolean b) throws Exception {
        if (!b)
            throw new Exception(String.format("ERROR: assertTrue"));
    }
}