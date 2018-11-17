import java.io.Reader;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleRWTryLock {
    private final AtomicReference<Holders> holders=new AtomicReference<>();
    public  boolean readerTryLock(){
        while(true){
            final Thread current=Thread.currentThread();
            ReaderList value= (ReaderList) holders.get();
            if(value==null || value.equals(new ReaderList(current,value))){
                holders.compareAndSet(value,new ReaderList(current,(ReaderList)holders.get()));
            }
        }
    }
    public void readerUnlock(){

    }
    public boolean writeTryLock(){
        final Thread current=Thread.currentThread();
        return holders.compareAndSet(null, new Writer(current));
    }
    public void writerUnlock(){
        final Thread current=Thread.currentThread();
        if(!holders.compareAndSet(new Writer(current),null))
            throw new RuntimeException("Not lock holders");
    }
    private static abstract class Holders{

    }
    private static class ReaderList extends Holders{
        private final Thread thread;
        private final ReaderList next;

        private ReaderList(Thread thread, ReaderList next) {
            this.thread = thread;
            this.next = next;
        }
    }
    private static class Writer extends Holders{
        public final Thread thread;

        private Writer(Thread thread) {
            this.thread = thread;
        }
    }
}

