/*import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.io.IOException;
import java.io.Serializable;

class MyMessage implements Serializable{
    public final String s;
    public MyMessage(String s){
        this.s=s;
    }
}
class MyActor extends UntypedActor{
    private int count=0;
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof MyMessage){
            MyMessage myMessage=(MyMessage) o;
            System.out.println(myMessage.s + "(" + count + ")");
            count++;
        }
    }
}
public class HelloWorld {
    public static void main(String[] args){
        final ActorSystem system=ActorSystem.create("HelloWorldSystem");

        final ActorRef myactor=system.actorOf(Props.create(MyActor.class),"myActor");
        myactor.tell(new MyMessage("this is hello"),ActorRef.noSender());
        myactor.tell(new MyMessage("this is hello hello world"),ActorRef.noSender());
        myactor.tell(new MyMessage("this is hello hello hello world"),ActorRef.noSender());

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
