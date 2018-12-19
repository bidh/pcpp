import java.util.concurrent.atomic.AtomicInteger;

public class CASAccounts implements Accounts{
    private AtomicInteger[] accounts;
     private AtomicInteger globalSum;

    public CASAccounts(int n) {
        accounts = new AtomicInteger[n];
        init(n);
        globalSum=new AtomicInteger(0);
    }

    public void init(int n) {
        for(int i=0;i<accounts.length;i++){
            accounts[i]=new AtomicInteger(0);
        }
    }

    public int get(int account) {
        return accounts[account].get();
    }

    public int sumBalances() {
        return globalSum.get();
    }

    public void deposit(int to, int amount) {
        int value,newValue;
        do{
            value=get(to);
            newValue=value+amount;
        }while(!accounts[to].compareAndSet(value,newValue));
        globalSum.getAndAdd(newValue);
    }

    public void transfer(int from, int to, int amount) {
        int f,t,newValue,newValue1;
        do{
            f=accounts[from].get();
            newValue=f+amount;
        }while(!accounts[from].compareAndSet(f,newValue));

        do{
            t=accounts[from].get();
            newValue1=t+amount;
        }while(!accounts[to].compareAndSet(f,newValue1));

    }

    public void transferAccount(Accounts other) {
        int value,newValue;

        for (int i = 0; i < accounts.length; i++) {
            do{
                value=accounts[i].get();
                newValue=value+other.get(i);
            }while(!accounts[i].compareAndSet(value,newValue));
            globalSum.getAndAdd(newValue);
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
        return null;
    }
}
