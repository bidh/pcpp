/*
import akka.actor.*;
import akka.actor.dsl.Creators;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

class MyMessage implements Serializable {
    final String message;
    public MyMessage(String message){
        this.message=message;
    }
}
class SubscribeMessage implements Serializable{
    final ActorRef subscriber;
    public SubscribeMessage(ActorRef subscriber){
        this.subscriber=subscriber;
    }
}
class UnsubscribeMessage implements Serializable{
    final ActorRef unsubscriber;
    public UnsubscribeMessage(ActorRef unsubscriber){
        this.unsubscriber=unsubscriber;
    }
}
class BroadcasterActor extends UntypedActor{
    List<ActorRef> lstActors=new LinkedList<>();
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof SubscribeMessage){
            SubscribeMessage m=(SubscribeMessage)o;
            lstActors.add(m.subscriber);
        }else if(o instanceof UnsubscribeMessage){
            UnsubscribeMessage m=(UnsubscribeMessage)o;
            lstActors.remove(m.unsubscriber);
        }else if(o instanceof MyMessage){
            MyMessage myMessage=(MyMessage)o;
            for(ActorRef personActor:lstActors){
                personActor.tell(myMessage,getSelf());
            }
        }
    }
}
class PersonActor extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof MyMessage){
            MyMessage m=(MyMessage)o;
            System.out.println(m.message);
        }
    }
}

public class Broadcast {
    public static void main(String[] args){
        final ActorSystem system= ActorSystem.create("EccoSystem");
        final ActorRef broadcasterActor=system.actorOf(Props.create(BroadcasterActor.class),"BroadcasterActor");
        final ActorRef p1Actor=system.actorOf(Props.create(PersonActor.class),"P1Actor");
        final ActorRef p2Actor=system.actorOf(Props.create(PersonActor.class),"P2Actor");
        final ActorRef p3Actor=system.actorOf(Props.create(PersonActor.class),"P3Actor");

        broadcasterActor.tell(new SubscribeMessage(p1Actor), ActorRef.noSender());
        broadcasterActor.tell(new SubscribeMessage(p2Actor), ActorRef.noSender());
        broadcasterActor.tell(new SubscribeMessage(p3Actor), ActorRef.noSender());

        broadcasterActor.tell(new MyMessage("foo"), ActorRef.noSender());
        broadcasterActor.tell(new UnsubscribeMessage(p2Actor), ActorRef.noSender());
        broadcasterActor.tell(new MyMessage("bar"), ActorRef.noSender());


        try{
            System.out.println("Press return to terminate");
            System.in.read();
        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            system.shutdown();
        }
    }
}
*/
