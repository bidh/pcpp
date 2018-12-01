/*import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.io.IOException;
import java.io.Serializable;

class MeroMessage implements Serializable {
    final String message;
    public MeroMessage(String message){
        this.message=message;
    }
}
class MyActors extends UntypedActor{
    String X="";
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof MeroMessage){
            MeroMessage myMessage=(MeroMessage)o;
            System.out.print(myMessage.message + "(");
            System.out.println(X +")");
            X= X + myMessage.message;
        }
    }
}
public class HelloWorldVariant {
    public static void main(String[] args){
        final ActorSystem actorSystem=ActorSystem.create("HelloWorldVariantSystem");
        final ActorRef myactor=actorSystem.actorOf(Props.create(MyActors.class),"MyActor");
        myactor.tell(new MeroMessage("aaa"),ActorRef.noSender());
        myactor.tell(new MeroMessage("bbb"),ActorRef.noSender());
        myactor.tell(new MeroMessage("ccc"),ActorRef.noSender());

        try{
            System.out.println("Press return to terminate");
            System.in.read();
        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            actorSystem.shutdown();
        }
    }
}*/
