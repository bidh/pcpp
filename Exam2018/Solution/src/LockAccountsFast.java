import java.util.concurrent.atomic.AtomicInteger;

public class LockAccountsFast implements Accounts{
    private int[] accounts;
    final int numberOfThreads=8;
    Object[] locks;
    Object[] sumLocks;
    public int[] sums;


    public LockAccountsFast(int n) {
        accounts = new int[n];
        locks=new Object[n];
        sumLocks=new Object[numberOfThreads];
        sums=new int[numberOfThreads];
        initLocks();
    }
    private void initLocks(){
        for(int i=0;i<locks.length;i++){
            locks[i]=new Object();
        }
        for(int i=0;i<numberOfThreads;i++){
            sumLocks[i]=new Object();
        }
    }
    private static <K> int getHash(Thread t) {
        final int kh = t.hashCode();
        return (kh ^ (kh >>> 16)) & 0x7FFFFFFF;
    }
    public void init(int n) {
        accounts = new int[n];
    }

    public int get(int account) {
        synchronized (locks[account]){
            return accounts[account];
        }
    }

    public int sumBalances() {
        AtomicInteger sum = new AtomicInteger();
        lockAllAndThen(()->{
            for(int i=0;i<sums.length;i++){
                sum.addAndGet(sums[i]);
            }
        });
        return sum.get();
    }

    public void deposit(int to, int amount) {
        final int h = getHash(Thread.currentThread()), stripe = h % sumLocks.length;
        synchronized (locks[to]){
            accounts[to] += amount;
        }
        synchronized (sumLocks[stripe]){
            sums[stripe]+=amount;
        }
    }

    public void transfer(int from, int to, int amount) {
        synchronized (locks[from]){
            accounts[from] -= amount;
        }
        synchronized (locks[to]){
            accounts[to] += amount;
        }
    }

    public void transferAccount(Accounts other) {
        for (int i = 0; i < accounts.length; i++) {
            deposit(i,other.get(i));
        }
    }

    public String toString() {
        String res = "";
        if (accounts.length > 0) {
            res = "" + accounts[0];
            for (int i = 1; i < accounts.length; i++) {
                res = res + " " + accounts[i];
            }
        }
        return res;
    }
    public int[] getAccounts(){
        return accounts;
    }
    private void lockAllAndThen(Runnable action) {
        lockAllAndThen(0, action);
    }

    private void lockAllAndThen(int nextStripe, Runnable action) {
        if (nextStripe >= sumLocks.length)
            action.run();
        else
            synchronized (sumLocks[nextStripe]) {
                lockAllAndThen(nextStripe + 1, action);
            }
    }
}
