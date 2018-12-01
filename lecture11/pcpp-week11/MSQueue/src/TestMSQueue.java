// For week 12
// sestoft@itu.dk * 2014-11-16

// Unbounded list-based lock-free queue by Michael and Scott 1996 (who
// call it non-blocking).

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class TestMSQueue {
    public static void main(String[] args) {
        //testAllQueues();
        ExecutorService service= Executors.newCachedThreadPool();
        final int nPairs=16;
        MSQueue<Integer> queue=new MSQueue<>();
        ConcurrentEnqDeqTest concurrentEnqDeqTest=new ConcurrentEnqDeqTest(queue,
                nPairs,
                100_000);
        concurrentEnqDeqTest.test(service);
        service.shutdown();
        System.out.println("....passed");
    }
    private static void testQueue(UnboundedQueue<Integer> queue){
        System.out.println(queue.getClass());
        assert queue.dequeue()==null;
        queue.enqueue(5);
        queue.enqueue(6);
        queue.enqueue(7);
        assert queue.dequeue()==5;
        assert queue.dequeue()==6;
        assert queue.dequeue()==7;
        assert queue.dequeue()==null;
    }
    private static void testAllQueues(){
        testQueue(new MSQueue<>());
    }
}
class ConcurrentEnqDeqTest extends Test{
    protected CyclicBarrier startBarrier,stopBarrier;
    protected final UnboundedQueue<Integer> queue;
    protected final int nPairs, nrTrials;
    protected final AtomicInteger enqueueSum,dequeueSum;

    public ConcurrentEnqDeqTest(UnboundedQueue<Integer> queue,
                                int nPairs,
                                int nTrials){
        this.queue=queue;
        this.nPairs=nPairs;
        this.nrTrials=nTrials;
        enqueueSum=new AtomicInteger(0);
        dequeueSum=new AtomicInteger(0);
        this.startBarrier = new CyclicBarrier(2*nPairs + 1);
        this.stopBarrier = new CyclicBarrier(2*nPairs + 1);;
    }
    void test(ExecutorService pool){
        try{
            for(int i=0;i<nPairs;i++){
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            startBarrier.await();
            stopBarrier.await();
            assertEquals(enqueueSum.get(),dequeueSum.get());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    class Producer implements Runnable{
        public void run(){
            try{
                Random random=new Random();
                int sum=0;
                startBarrier.await();
                for (int i=nrTrials;i>0;--i){
                    int item=random.nextInt();
                    queue.enqueue(item);
                    sum+=item;
                }
                enqueueSum.getAndAdd(sum);
                stopBarrier.await();
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }
    }
    class Consumer implements Runnable{
        public void run(){
            try{
                startBarrier.await();
                int sum=0;
                Integer item;
                for(int i=nrTrials;i>0;--i){
                    item=queue.dequeue();
                    if(item!=null){
                        sum+=item;
                    }else{
                        i++;
                    }
                }
                dequeueSum.getAndAdd(sum);
                stopBarrier.await();
            }catch (Exception ex){
                 throw new RuntimeException(ex);
            }
        }
    }
}

class Test {
    public static void assertEquals(int x, int y) throws Exception {
        if (x != y)
            throw new Exception(String.format("ERROR: %s not equal to %s%n", x, y));
    }

    public static void assertTrue(boolean b) throws Exception {
        if (!b)
            throw new Exception(String.format("ERROR: assertTrue"));
    }
}


interface UnboundedQueue<T> {
    void enqueue(T item);
    T dequeue();
}

// ------------------------------------------------------------
// Unbounded lock-based queue with sentinel (dummy) node

class LockingQueue<T> implements UnboundedQueue<T> {
    // Invariants:
    // The node referred by tail is reachable from head.
    // If non-empty then head != tail,
    //    and tail points to last item, and head.next to first item.
    // If empty then head == tail.

    private static class Node<T> {
        final T item;
        Node<T> next;

        public Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
        }
    }

    private Node<T> head, tail;

    public LockingQueue() {
        head = tail = new Node<T>(null, null);
    }

    public synchronized void enqueue(T item) { // at tail
        Node<T> node = new Node<T>(item, null);
        tail.next = node;
        tail = node;
    }

    public synchronized T dequeue() {     // from head
        if (head.next == null)
            return null;
        Node<T> first = head;
        head = first.next;
        return head.item;
    }
}


// ------------------------------------------------------------
// Unbounded lock-free queue (non-blocking in M&S terminology),
// using CAS and AtomicReference

// This creates one AtomicReference object for each Node object.  The
// next MSQueueRefl class further below uses one-time reflection to
// create an AtomicReferenceFieldUpdater, thereby avoiding this extra
// object.  In practice the overhead of the extra object apparently
// does not matter much.

class MSQueue<T> implements UnboundedQueue<T> {
    private final AtomicReference<Node<T>> head, tail;

    public MSQueue() {
        Node<T> dummy = new Node<T>(null, null);
        head = new AtomicReference<Node<T>>(dummy);
        tail = new AtomicReference<Node<T>>(dummy);
    }

    public void enqueue(T item) { // at tail
        Node<T> node = new Node<T>(item, null);
        while (true) {
            Node<T> last = tail.get(), next = last.next.get();
            if (last == tail.get()) {         // E7
                if (next == null) {
                    // In quiescent state, try inserting new node
                    if (last.next.compareAndSet(next, node)) { // E9
                        // Insertion succeeded, try advancing tail
                        tail.compareAndSet(last, node);
                        return;
                    }
                } else
                    // Queue in intermediate state, advance tail
                    tail.compareAndSet(last, next);
            }
        }
    }

    public T dequeue() { // from head
        while (true) {
            Node<T> first = head.get(), last = tail.get(), next = first.next.get(); // D3
            if (first == head.get()) {        // D5
                if (first == last) {
                    if (next == null)
                        return null;
                    else
                        tail.compareAndSet(last, next);
                } else {
                    T result = next.item;
                    if (head.compareAndSet(first, next)) // D13
                        return result;
                }
            }
        }
    }

    private static class Node<T> {
        final T item;
        final AtomicReference<Node<T>> next;

        public Node(T item, Node<T> next) {
            this.item = item;
            this.next = new AtomicReference<Node<T>>(next);
        }
    }
}


// --------------------------------------------------
// Lock-free queue, using CAS and reflection on field Node.next

class MSQueueRefl<T> implements UnboundedQueue<T> {
    private final AtomicReference<Node<T>> head, tail;

    public MSQueueRefl() {
        // Essential to NOT make dummy a field as in Goetz p. 334, that
        // would cause a memory management disaster, huge space leak:
        Node<T> dummy = new Node<T>(null, null);
        head = new AtomicReference<Node<T>>(dummy);
        tail = new AtomicReference<Node<T>>(dummy);
    }

    @SuppressWarnings("unchecked")
    // Java's @$#@?!! generics type system: abominable unsafe double type cast
    private final AtomicReferenceFieldUpdater<Node<T>, Node<T>> nextUpdater
            = AtomicReferenceFieldUpdater.newUpdater((Class<Node<T>>)(Class<?>)(Node.class),
            (Class<Node<T>>)(Class<?>)(Node.class),
            "next");

    public void enqueue(T item) { // at tail
        Node<T> node = new Node<T>(item, null);
        while (true) {
            Node<T> last = tail.get(), next = last.next;
            if (last == tail.get()) {         // E7
                if (next == null)  {
                    // In quiescent state, try inserting new node
                    if (nextUpdater.compareAndSet(last, next, node)) {
                        // Insertion succeeded, try advancing tail
                        tail.compareAndSet(last, node);
                        return;
                    }
                } else {
                    // Queue in intermediate state, advance tail
                    tail.compareAndSet(last, next);
                }
            }
        }
    }

    public T dequeue() { // from head
        while (true) {
            Node<T> first = head.get(), last = tail.get(), next = first.next;
            if (first == head.get()) {        // D5
                if (first == last) {
                    if (next == null)
                        return null;
                    else
                        tail.compareAndSet(last, next);
                } else {
                    T result = next.item;
                    if (head.compareAndSet(first, next)) {
                        return result;
                    }
                }
            }
        }
    }

    private static class Node<T> {
        final T item;
        volatile Node<T> next;

        public Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
        }
    }
}
