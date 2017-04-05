import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Thread5 extends Thread{
    private final CountDownLatch latch5;
    private final CyclicBarrier barrier;

    Thread5(CountDownLatch latch5, CyclicBarrier barrier){
        this.latch5 = latch5;
        this.barrier = barrier;
    }
    @Override
    public void run(){
        System.out.println("Thread 5 begins!");
        try {
            System.out.println("Thread 5 awaits!");
            latch5.await();
            Thread.sleep(600);
            System.out.println("Thread 5 finishes!");
            barrier.await();

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}