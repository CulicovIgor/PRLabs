import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Main {
    private final Object thread3Waiter = new Object();
    private final Object thread6Waiter = new Object();

    private void runThreads(){

        final CyclicBarrier barrier3 = new CyclicBarrier(2,() -> {
            System.out.println("BARRIER3!");
            synchronized (thread3Waiter){
                thread3Waiter.notify();
            }
        }
        );
        final CyclicBarrier barrier6 = new CyclicBarrier(2,() -> {
            System.out.println("BARRIER6!");
            synchronized (thread6Waiter){
                thread6Waiter.notify();
            }
        }
        );
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch4 = new CountDownLatch(2);
        CountDownLatch latch5 = new CountDownLatch(1);
        Thread1 thread1 = new Thread1(barrier3,latch);
        Thread2 thread2 = new Thread2(barrier3,latch,latch4);
        Thread3 thread3 = new Thread3(thread3Waiter, latch4);
        Thread4 thread4 = new Thread4(latch4, latch5, barrier6);
        Thread5 thread5 = new Thread5(latch5, barrier6);
        Thread6 thread6 = new Thread6(thread6Waiter);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.runThreads();
    }
}