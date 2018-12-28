import org.multiverse.api.Txn;
import org.multiverse.api.references.TxnInteger;

import static org.multiverse.api.StmUtils.atomic;
import static org.multiverse.api.StmUtils.newTxnInteger;

public class STMAccounts implements Accounts {
    private final TxnInteger[] accounts;

    public STMAccounts(int n) {
        accounts = new TxnInteger[n];
        init(n);
    }

    public void init(int n) {
        for(int i=0;i<accounts.length;i++){
            accounts[i]=newTxnInteger(0);
        }
    }

    public int get(int account) {
        return atomic(()-> accounts[account].get());
    }

    public int sumBalances() {
        return atomic(()->{
            int sum = 0;
            for (int i = 0; i < accounts.length; i++) {
                sum += accounts[i].get();
            }
            return sum;
        });

    }

    public void deposit(int to, int amount) {
        atomic(()->{
            accounts[to].getAndIncrement(amount);
        });
    }

    public void transfer(int from, int to, int amount) {
        atomic(()->{
            accounts[from].getAndIncrement(-amount);
        });
        atomic(()->{
            accounts[to].getAndIncrement(amount);
        });
    }

    public void transferAccount(Accounts other) {
        for (int i = 0; i < accounts.length; i++) {
            final int iF=i;
            atomic(()->{
                accounts[iF].getAndIncrement(other.get(iF));
            });
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
