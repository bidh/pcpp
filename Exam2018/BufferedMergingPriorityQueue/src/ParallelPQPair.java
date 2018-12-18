import java.util.function.Function;

public class ParallelPQPair implements PQPair{
    PQ left,right;
    public PQPair clone() {
        return new SerialPQPair();
    }
    public void createPairParam(Parameters param, Function<Parameters,PQ> instanceCreator){
        Thread t1=new Thread(()->{
            left  = instanceCreator.apply( param.left() );
        });
        Thread t2=new Thread(()->{
            right = instanceCreator.apply( param.right());
        });
        t1.start();t2.start();

        try{
            t1.join();
            t2.join();
        }catch (InterruptedException ex){

        }

    };
    public PQ getLeft() { return left;}
    public PQ getRight(){ return right;}
}
