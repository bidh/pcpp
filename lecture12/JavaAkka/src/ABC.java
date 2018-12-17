/*
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.dsl.Creators;

import java.io.Serializable;
import java.util.Random;

class StartTransferMessage implements Serializable{
    final ActorRef bank,from,to;
    public StartTransferMessage(ActorRef bank,ActorRef from,ActorRef to){
        this.bank=bank;
        this.from=from;
        this.to=to;
    }
}
class TransferMessage implements Serializable{
    final int Amount;
    final ActorRef From,To;

    public TransferMessage(int Amount, ActorRef From, ActorRef To){
        this.Amount=Amount;
        this.From=From;
        this.To=To;
    }
}
class DepositMessage implements Serializable{
    final int message;
    public DepositMessage(int message){
        this.message=message;
    }
}
class PrintBalanceMessage implements Serializable{
    final String message;
    public PrintBalanceMessage(String message){
        this.message=message;
    }
}

class BankActor extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof TransferMessage){
            TransferMessage transferMessage=(TransferMessage)o;
            transferMessage.From.tell(new DepositMessage(-(transferMessage.Amount)),ActorRef.noSender());
            transferMessage.To.tell(new DepositMessage((transferMessage.Amount)),ActorRef.noSender());
        }
    }
}
class AccountActor extends UntypedActor{
    int balance=0;
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof DepositMessage){
            DepositMessage depositMessage=(DepositMessage)o;
            balance= balance+depositMessage.message;
        }
        if(o instanceof PrintBalanceMessage){
            PrintBalanceMessage printBalanceMessage=(PrintBalanceMessage)o;
            System.out.println(printBalanceMessage.message+ balance);
        }
    }
}
class ClerkActor extends UntypedActor{
    private void ntransfers(int n, ActorRef bank, ActorRef from, ActorRef to){
        Random rand=new Random();
        for(int i=0;i<100;i++){
           int amount=rand.nextInt();
           bank.tell(new TransferMessage(amount,from,to),ActorRef.noSender());
        }
    }
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof StartTransferMessage){
            StartTransferMessage startTransferMessage=(StartTransferMessage)o;
            ntransfers(100, startTransferMessage.bank,startTransferMessage.from,startTransferMessage.to);
        }
    }
}
public class ABC {
    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system=ActorSystem.create("ABCSystem");
        final ActorRef a1Actor=system.actorOf(Props.create(AccountActor.class),"A1Actor");
        final ActorRef a2Actor=system.actorOf(Props.create(AccountActor.class),"A2Actor");
        final ActorRef b1Actor=system.actorOf(Props.create(BankActor.class),"B1Actor");
        final ActorRef b2Actor=system.actorOf(Props.create(BankActor.class),"B2Actor");
        final ActorRef c1Actor=system.actorOf(Props.create(ClerkActor.class),"C1Actor");
        final ActorRef c2Actor=system.actorOf(Props.create(ClerkActor.class),"C2Actor");

        c1Actor.tell(new StartTransferMessage(b1Actor, a1Actor, a2Actor),ActorRef.noSender());
        c2Actor.tell(new StartTransferMessage(b2Actor, a2Actor, a1Actor),ActorRef.noSender());
        Thread.sleep(1000);

        a1Actor.tell(new PrintBalanceMessage("Balance = "),ActorRef.noSender());
        a2Actor.tell(new PrintBalanceMessage("Balance = "),ActorRef.noSender());

    }
}
*/
