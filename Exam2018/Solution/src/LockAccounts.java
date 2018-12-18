public class LockAccounts implements Accounts{
    private int[] accounts;
    Object[] locks;

    public LockAccounts(int n) {
        accounts = new int[n];
        locks=new Object[n];
        initLocks();
    }
    private void initLocks(){
        for(int i=0;i<locks.length;i++){
            locks[i]=new Object();
        }
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
        int sum = 0;
        for (int i = 0; i < accounts.length; i++) {
            sum += this.get(i);
        }
        return sum;
    }

    public void deposit(int to, int amount) {
        synchronized (locks[to]){
            accounts[to] += amount;
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
            accounts[i] += other.get(i);
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
}
