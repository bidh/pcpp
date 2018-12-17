/*
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class InitialMessage implements Serializable{
    final ActorRef actorRef;
    public InitialMessage(ActorRef actorRef){
        this.actorRef=actorRef;
    }
}
class NumMessage implements Serializable{
    final int num;
    public NumMessage(int num){
        this.num=num;
    }
}
class SorterActor extends UntypedActor{
    ActorRef Out;
    List<Integer> L=new ArrayList<>();
    public void add(int n){
        L.add(n);
        Collections.sort(L);
    }
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof InitialMessage){
            InitialMessage initialMessage=(InitialMessage)o;
            Out=initialMessage.actorRef;
        }
        if(o instanceof NumMessage){
            NumMessage numMessage=(NumMessage)o;
            if(L.size()<4){
                add(numMessage.num);
            }else{
                int n=L.get(0);
                L.remove(0);
                add(numMessage.num);
                Out.tell(new NumMessage(n),ActorRef.noSender());
            }
        }
    }
}
class EchoActor extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof NumMessage){
            NumMessage numMessage=(NumMessage)o;
            System.out.println(numMessage.num);
        }
    }
}

public class SortingPipeline {
    public static void transmit(List<Integer> list,ActorRef actorRef){
        for (Integer i:list ) {
            actorRef.tell(new NumMessage(i),ActorRef.noSender());
        }
    }
    public static void main(String[] args){
        final ActorSystem system= ActorSystem.create("SortingPipelineSystem");

        final ActorRef FirstSorterActor=system.actorOf(Props.create(SorterActor.class),"FirstSorterActor");
        final ActorRef SecondSorterActor=system.actorOf(Props.create(SorterActor.class),"SecondSorterActor");
        final ActorRef EchoActor=system.actorOf(Props.create(EchoActor.class),"EchoActor");

        FirstSorterActor.tell(new InitialMessage(SecondSorterActor),ActorRef.noSender());
        SecondSorterActor.tell(new InitialMessage(EchoActor),ActorRef.noSender());

        List<Integer> l1=new ArrayList<>();
        l1.add(4);l1.add(7);l1.add(2);l1.add(8);l1.add(6);l1.add(1);l1.add(5);l1.add(3);

        List<Integer> l2=new ArrayList<>();
        l2.add(9);l2.add(9);l2.add(9);l2.add(9);l2.add(9);l2.add(9);l2.add(9);l2.add(9);

        transmit(l1, FirstSorterActor);
        transmit(l2, FirstSorterActor);

    }
}
*/
