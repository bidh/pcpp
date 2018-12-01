/*import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.collection.script.Start;

import java.io.IOException;
import java.io.Serializable;

class StartMessage implements Serializable{
    public final ActorRef ecco;
    public StartMessage(ActorRef ecco){
        this.ecco=ecco;
    }
}
class MyMessage implements Serializable{
    final String message;
    public MyMessage(String message){
        this.message=message;
    }
}
class PersonActor extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof StartMessage){
            StartMessage startMessage=(StartMessage) o;
            ActorRef ecco=startMessage.ecco;
            String s="hvad drikker moller";
            System.out.println("[says] "+s);
            ecco.tell(new MyMessage(s), getSelf());
        }else if(o instanceof MyMessage){
            MyMessage myMessage=(MyMessage) o;
            System.out.println("hears] "+myMessage.message);
        }
    }
}
class EccoActor extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof MyMessage){
            MyMessage myMessage=(MyMessage) o;
            String s=myMessage.message;
            MyMessage reply;
            if(s.length()>5)
                reply=new MyMessage("..."+s.substring(s.length()-5));
            else reply=new MyMessage("....");
            getSender().tell(reply,getSelf());
            getSender().tell(reply,getSelf());
            getSender().tell(reply,getSelf());
        }
    }
}
public class Ecco {
    public static void main(String[] args){
        final ActorSystem system=ActorSystem.create("EccoSystem");
        final ActorRef personActor=system.actorOf(Props.create(PersonActor.class),"PersonActor");
        final ActorRef eccoActor=system.actorOf(Props.create(EccoActor.class),"EccoActor");

        personActor.tell(new StartMessage(eccoActor), ActorRef.noSender());
        try{
            System.out.println("Press return to terminate");
            System.in.read();
        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            system.shutdown();
        }
    }
}*/
