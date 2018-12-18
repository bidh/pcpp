// COMPILE:
// javac -cp scala.jar:akka-actor.jar ScatterGather.java
// RUN:
// java -cp scala.jar:akka-actor.jar:akka-config.jar:. ScatterGather

import java.util.Random;
import java.io.*;
import akka.actor.*;

// -- MESSAGES --------------------------------------------------

class StartMessage implements Serializable { public StartMessage() { } }

class SplitMessage implements Serializable { public SplitMessage() { } }

class CallerMessage implements Serializable {
    public final ActorRef caller;
    public CallerMessage(ActorRef caller) { this.caller = caller; }
}

class ComputeMessage implements Serializable {
    public final int number;
    public final ActorRef caller;
    public ComputeMessage(int number, ActorRef caller) {
        this.number = number;
        this.caller = caller;
    }
}

class ResultMessage implements Serializable {
    public final double result;
    public ResultMessage(double result) { this.result = result; }
}

// -- ACTORS --------------------------------------------------

class WorkerScatterActor extends UntypedActor {
    // used by scatter (and for indicating whether actor role is 'worker' or 'scatter'):
    private ActorRef left, right; // 'worker' whenever null, 'scatter' whenever non-null.

    // used by 'worker':
    private final Random rnd = new Random();
    private int random(int n) { return rnd.nextInt(n); }
    private int compute(int n) { return random(n) + 1; }

    private void worker(Object o) throws Exception {
        if (o instanceof SplitMessage) {
            left = getContext().actorOf(Props.create(WorkerScatterActor.class), "left");
            right = getContext().actorOf(Props.create(WorkerScatterActor.class), "right");
        } else if (o instanceof ComputeMessage) {
            ComputeMessage m = (ComputeMessage) o;
            int result = compute(m.number);
            System.out.println(result);
            m.caller.tell(new ResultMessage(result), ActorRef.noSender());
        }
    }

    private void scatter(Object o) throws Exception {
        if (o instanceof SplitMessage) {
            left.forward(o, getContext());
            right.forward(o, getContext());
        } else if (o instanceof ComputeMessage) {
            ComputeMessage m = (ComputeMessage) o;
            ActorRef gather = getContext().actorOf(Props.create(GatherActor.class), "gather");
            gather.tell(new CallerMessage(m.caller), ActorRef.noSender()); // instead of arguments to constructor
            left.tell(new ComputeMessage(m.number, gather), ActorRef.noSender());
            right.tell(new ComputeMessage(m.number, gather), ActorRef.noSender());
        }
    }

    public void onReceive(Object o) throws Exception {
        // dispatch according to actor role: 'worker' or 'scatter'
        if (left == null) worker(o);
        else scatter(o);
    }
}

class GatherActor extends UntypedActor {
    double res1;
    ActorRef caller;

    private double average(double x, double y) { return (x + y) / 2; }

    public void onReceive(Object o) throws Exception {
        if (o instanceof CallerMessage) {
            caller = ((CallerMessage) o).caller;
        } else if (o instanceof ResultMessage) {
            if (caller == null) throw new Exception("no caller address!!!");
            if (res1 == 0) {
                res1 = ((ResultMessage) o).result;
            } else {
                double res2 = ((ResultMessage) o).result;
                double res = average(res1, res2);
                caller.tell(new ResultMessage(res), ActorRef.noSender());
                getContext().stop(getSelf()); // die!
            }
        }
    }
}

class StartActor extends UntypedActor {
    public void onReceive(Object o) throws Exception {
        if (o instanceof StartMessage) {
            ActorRef worker = getContext().actorOf(Props.create(WorkerScatterActor.class), "worker");
            worker.tell(new SplitMessage(), ActorRef.noSender());
            worker.tell(new SplitMessage(), ActorRef.noSender());
            worker.tell(new ComputeMessage(6, getSelf()), ActorRef.noSender());
        } else if (o instanceof ResultMessage) {
            double result = ((ResultMessage) o).result;
            System.out.println("result = "  + result);
        }
    }
}

// -- MAIN --------------------------------------------------

public class ScatterGather {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("HelloWorldSystem");
        final ActorRef starter = system.actorOf(Props.create(StartActor.class), "starter");
        starter.tell(new StartMessage(), ActorRef.noSender());
        try {
            System.out.println("Press return to terminate...");
            System.in.read();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            system.shutdown();
        }
    }
}
