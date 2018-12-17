/*
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.io.Serializable;

class InitMessage implements Serializable{
    final ActorRef odd,even,collector;

    public InitMessage(ActorRef odd,ActorRef even,ActorRef collector){
        this.odd=odd;
        this.even=even;
        this.collector=collector;
    }
    public InitMessage(ActorRef collector){
        this.collector=collector;
        this.odd=null;
        this.even=null;
    }
}
class NumMessage implements Serializable{
    final int num;
    public NumMessage(int num){
        this.num=num;
    }
}

class Dispatcher extends UntypedActor{
    ActorRef odd,even;
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof InitMessage){
            InitMessage initMessage=(InitMessage)o;
            this.odd=initMessage.odd;
            this.even=initMessage.even;
            odd.tell(new InitMessage(initMessage.collector),ActorRef.noSender());
            even.tell(new InitMessage(initMessage.collector),ActorRef.noSender());
        }
        else if(o instanceof NumMessage){
            int num=((NumMessage) o).num;
            boolean result= num % 2==0;
            if(result){
                even.tell(new NumMessage(num),ActorRef.noSender());
            }else{
                odd.tell(new NumMessage(num),ActorRef.noSender());
            }
        }
    }
}
class Worker extends UntypedActor{
    ActorRef collector;
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof InitMessage){
            InitMessage initMessage=(InitMessage)o;
            this.collector=initMessage.collector;
        }
        else if(o instanceof NumMessage){
            int num=((NumMessage) o).num;
            int square=num*num;
            collector.tell(new NumMessage(square),ActorRef.noSender());
        }
    }
}
class Collector extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof NumMessage){
            NumMessage numMessage=(NumMessage)o;
            System.out.println(numMessage.num);
        }
    }
}

public class start {
    public static void main(String[] args){
        final ActorSystem system= ActorSystem.create("Start");
        ActorRef dispatcher= system.actorOf(Props.create(Dispatcher.class),"dispatcherActor");
        ActorRef odd= system.actorOf(Props.create(Worker.class),"oddActor");
        ActorRef even= system.actorOf(Props.create(Worker.class),"evenActor");
        ActorRef collector= system.actorOf(Props.create(Collector.class),"colletorActor");

        dispatcher.tell(new InitMessage(odd,even,collector), ActorRef.noSender());
        dispatcher.tell(new NumMessage(1),ActorRef.noSender());
        dispatcher.tell(new NumMessage(2),ActorRef.noSender());
        dispatcher.tell(new NumMessage(3),ActorRef.noSender());
        dispatcher.tell(new NumMessage(4),ActorRef.noSender());
        dispatcher.tell(new NumMessage(5),ActorRef.noSender());
        dispatcher.tell(new NumMessage(6),ActorRef.noSender());
        dispatcher.tell(new NumMessage(7),ActorRef.noSender());
        dispatcher.tell(new NumMessage(8),ActorRef.noSender());
        dispatcher.tell(new NumMessage(9),ActorRef.noSender());
        dispatcher.tell(new NumMessage(10),ActorRef.noSender());
    }
}
*/
