import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Thread4 extends Thread {
    private final CountDownLatch latch4;
    private final CountDownLatch latch5;
    private final CyclicBarrier barrier;

    public Thread4(CountDownLatch latch4, CountDownLatch latch5, CyclicBarrier barrier){
        this.latch4 = latch4;
        this.latch5 = latch5;
        this.barrier = barrier;
    }

    @Override
    public void run(){
        System.out.println("Thread 4 begins!");
        try {
            System.out.println("Thread 4 awaits!");
            latch4.await();
            Thread.sleep(500);
            latch5.countDown();
            System.out.println("Thread 4 finishes!");
            barrier.await();
        } catch (InterruptedException ex) {
            return;
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
