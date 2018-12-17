/*
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class initMessage implements Serializable{
    final int N;

    public initMessage(int N){
        this.N=N;
    }
}
class isPrimeMessage implements Serializable{
    final int N;

    public isPrimeMessage(int N){
        this.N=N;
    }
}
class SlaveActor extends UntypedActor{
    private int Id;

    private static boolean isPrime(int n) {
        int k = 2;
        while (k * k <= n && n % k != 0)
            k++;
        return n >= 2 && k * k > n;
    }
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof isPrimeMessage){
            int p=((isPrimeMessage)o).N;
            if(isPrime(p)){
                System.out.println("("+ p%7 + ")"+p);
            }
        }
    }
}
class PrimerActor extends UntypedActor{
    List<ActorRef> slaves=new ArrayList<>();
    private void createSlave(int Max){
        for(int i=0;i<=Max;i++){
            final ActorRef slave=getContext().actorOf(Props.create(SlaveActor.class),"p"+i);
            slaves.add(slave);
        }
    }
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof initMessage){
            initMessage message=(initMessage)o;
            if(message.N<=0){
                throw new RuntimeException();
            }else{
                createSlave(message.N);
            }
        }
        if(o instanceof isPrimeMessage){
            isPrimeMessage message=(isPrimeMessage)o;
            if(slaves.isEmpty())
                throw new RuntimeException();
            else if(message.N<=0){
                throw new RuntimeException();
            }else{
               int slaveid=message.N % slaves.size();
               slaves.get(slaveid).tell(o,getSelf());
            }
        }
    }
}

public class Primer {
    private static void spam(ActorRef primer,int N,int Max){
        for(int i=N;i<=Max;i++){
            primer.tell(new isPrimeMessage(i),ActorRef.noSender());
        }
    }
    public static void main(String[] args){
        final ActorSystem system= ActorSystem.create("PrimerSystem");

        final ActorRef PrimerActor=system.actorOf(Props.create(PrimerActor.class),"PrimerActor");
        PrimerActor.tell(new initMessage(7),ActorRef.noSender());
        try {
            System.out.println("Press return to initiate...");
            System.in.read();
            spam(PrimerActor, 2, 100);
            System.out.println("Press return to terminate...");
            System.in.read();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            system.shutdown();
        }
    }
}
*/
