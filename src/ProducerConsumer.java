import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class MessageQueue {
    ArrayList<String> queue;
    int capcaity;

    public MessageQueue(int capacity) {
        queue = new ArrayList<>();
        this.capcaity = capacity;
    }

    public boolean isFull(){
        return queue.size() == capcaity;
    }

    public boolean isEmpty(){
        return queue.size() == 0;
    }

    public synchronized void enqueue(String msg){
        while(isFull()){
            try{
                System.out.println("Thread who get into wait under enqueue : " + Thread.currentThread());
                this.wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println("Thread who get into notify under enqueue : " + Thread.currentThread());
        queue.add(msg);
        this.notify();
    }

    public synchronized String  dequeue(String msg){
        while(isEmpty()){
            try{
                System.out.println("Thread who get into wait under dequeue : " + Thread.currentThread());
                this.wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        String msgStr = queue.remove(0);
        System.out.println("Thread who get into notify under dequeue : " + Thread.currentThread());
        this.notify();
        return msgStr;
    }
}

class ProducerThread extends Thread {
    MessageQueue queue;
    public ProducerThread(MessageQueue queue){
        this.queue = queue;
    }
    @Override
    public void run(){
        for(int i=0 ;i<10; i++){
            queue.enqueue("Producer : " + i);
            System.out.println("Producer : " + i);
        }
    }
}

class ConsumerThread extends Thread {
    MessageQueue queue;
    public ConsumerThread(MessageQueue queue){
        this.queue = queue;
    }
    @Override
    public void run(){
        for(int i=0 ;i<10; i++){
            queue.dequeue("Consumer : " + i);
            System.out.println("Consumer : " + i);
        }
    }

}

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        MessageQueue queue = new MessageQueue(3);
        new ProducerThread(queue).start();
        new ConsumerThread(queue).start();
    }
}

// Change size of capcity for bettter understanding
//Producer thread and Consuner thread working on shared msgQueue, Handling enqueue and dequeue through synchronization and wait, notify kryword.
// KEY THINGS ::: synchronized, wait(), notifyAll() :::


//
//Explanation -
//        Here we have a class MessageQueue which acts as a buffer between producer thread and the consumer thread. And the buffer is bounded, so we can set the limit during object creation.
//
//        isFull() returns true if messages size reaches the limit.
//
//        isEmpty() return true if messages size is equal to zero.
//
//        enqueue() - Here it is invoked only by ProducerThread; it adds (at the end) the message to the messages array if it is not full, if full it calls the wait() over the queue object (this) till the consumer consumes the message and notifies it. Also once it adds the message to the queue, it calls the notify() in order to end the consumer's wait() if any.
//
//        dequeue()  - Here it is invoked only by ConsumerThread; it removes the first element and returns it. If the queue is empty it calls the wait() over the queue object till the producer produces the message. Once it consumes the message it calls the notify() to end the producer's wait() if any.
//
//        main() initiates these operations, by creating the MessageQueue object and passing it to both the producer and consumer threads.