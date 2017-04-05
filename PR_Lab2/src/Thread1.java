import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Thread1 extends Thread{
    private final CyclicBarrier barrier;
    private final CountDownLatch latch;
    Thread1(CyclicBarrier barrier,  CountDownLatch latch){
        this.barrier = barrier;
        this.latch = latch;
    }
    public void run() {
        System.out.println("Thread 1 begins!");
        try {
            Thread.sleep(300);
            latch.countDown();
            System.out.println("Thread 1 finishes!");
            barrier.await();
        } catch (InterruptedException ex) {
            return;
        } catch (BrokenBarrierException ex) {
            return;
        }
    }
}
