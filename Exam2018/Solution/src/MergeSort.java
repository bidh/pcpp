import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class StartMessage implements Serializable { public StartMessage() { } }

class InitMessage implements Serializable{
    final ActorRef sorter;

    public InitMessage(ActorRef sorter){
        this.sorter=sorter;
    }
}
class Sorted implements Serializable{
    final List<Integer> lst;

    public Sorted(List<Integer> lst){
        this.lst=lst;
    }
}
class SortMessage implements Serializable{
    final List<Integer> lst; ActorRef X;

    public SortMessage(List<Integer> lst, ActorRef X){
        this.lst=lst;
        this.X=X;
    }
}
class ResultMessage implements Serializable{
    final ActorRef X;
    public ResultMessage(ActorRef X){
        this.X=X;
    }
}


class Sorter extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof SortMessage){
            ActorRef X=((SortMessage)o).X;
            List<Integer> L=((SortMessage)o).lst;
            if(L.size()>1){
                ActorRef M = getContext().actorOf(Props.create(Merger.class), "Merger");
                M.tell(new ResultMessage(X),ActorRef.noSender());
                int size=L.size();
                List<Integer> header=L.subList(0,size/2);
                List<Integer> tail=L.subList(size/2,size);

                ActorRef S1 = getContext().actorOf(Props.create(Sorter.class), "Sorter1");
                S1.tell(new SortMessage(header,M),ActorRef.noSender());

                ActorRef S2 = getContext().actorOf(Props.create(Sorter.class), "Sorter2");
                S2.tell(new SortMessage(tail,M),ActorRef.noSender());
            }else{
                X.tell(new Sorted(L),ActorRef.noSender());
            }
        }
    }
}

class Merger extends UntypedActor{
    List<Integer> L1,L2;
    ActorRef X;

    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof ResultMessage){
            X=((ResultMessage)o).X;
        }else if(o instanceof Sorted){
            if(L1==null){
                L1=((Sorted)o).lst;
            }else{
                L2=((Sorted)o).lst;
                List<Integer> finalL=merge(L1,L2);
                System.out.println("Merged: "+finalL);
                X.tell(new Sorted(finalL),ActorRef.noSender());
            }
        }
    }
    private static List<Integer> merge(List<Integer> L1,List<Integer> L2){

        if(L1.isEmpty()){
            return L2;
        }else if(L2.isEmpty()){
            return L1;
        }else{
            int h1=L1.get(0);
            int h2=L2.get(0);
            List<Integer> t1=L1.subList(1,L1.size());
            List<Integer> t2=L2.subList(1,L2.size());
            if(h1<h2){
                List<Integer> headList= Collections.singletonList(h1);
                return Stream.concat(headList.stream(),merge(t1,L2).stream()).collect(Collectors.toList());

            }else{
                List<Integer> headList= Collections.singletonList(h2);
                return Stream.concat(headList.stream(),merge(L1,t2).stream()).collect(Collectors.toList());
            }
        }
    }
}

class Tester extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof InitMessage){
            Integer[] myArray=new Integer[]{7,3,5,74,35,43,65,45,-1};
            Integer[] myArray1=new Integer[]{8,7,6,5,3,2,1,4,-1,9};
            Integer[] myArray2=new Integer[]{6,7,3,5,4,1,2,8};

            List<Integer> lst= Arrays.asList(myArray);

            ActorRef sorter=((InitMessage)o).sorter;
            sorter.tell(new SortMessage(lst,getSelf()),ActorRef.noSender());
        }
        else if(o instanceof Sorted){
            List<Integer> lst=((Sorted)o).lst;
            System.out.print("RESULT: ");
            for(int i:lst)
                System.out.print(i+" ");
        }
    }
}

class StartActor extends UntypedActor {
    public void onReceive(Object o) throws Exception {
        if (o instanceof StartMessage) {
            ActorRef testerActor = getContext().actorOf(Props.create(Tester.class), "tester");
            ActorRef sorterActor = getContext().actorOf(Props.create(Sorter.class), "sorter");
            testerActor.tell(new InitMessage(sorterActor),getSelf());
        }
    }
}
public class MergeSort {
    public static void main(String[] args){
        final ActorSystem system = ActorSystem.create("MergeSort");
        final ActorRef starter = system.actorOf(Props.create(StartActor.class), "starter");
        starter.tell(new StartMessage(), ActorRef.noSender());
    }
}