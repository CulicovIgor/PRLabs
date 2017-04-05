import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Thread2 extends Thread{
    private final CyclicBarrier barrier;
    private final CountDownLatch latch;
    private final CountDownLatch latch4;

    public Thread2(CyclicBarrier barrier, CountDownLatch latch, CountDownLatch latch4){
        this.barrier = barrier;
        this.latch = latch;
        this.latch4 = latch4;
    }
    @Override
    public void run(){
        System.out.println("Thread 2 begins!");
        try {
            System.out.println("Thread 2 awaits!");
            latch.await();
            Thread.sleep(400);
            latch4.countDown();
            System.out.println("Thread 2 finishes!");
            barrier.await();
        } catch (InterruptedException ex) {
            return;
        } catch (BrokenBarrierException ex) {
            return;
        }
    }

}
