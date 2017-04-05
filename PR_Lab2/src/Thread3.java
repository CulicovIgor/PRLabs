import java.util.concurrent.CountDownLatch;

public class Thread3 extends Thread{
    private final Object waiter;
    private final CountDownLatch latch4;

    public Thread3(Object waiter, CountDownLatch latch4){
        this.waiter = waiter;
        this.latch4 = latch4;
    }

    @Override
    public void run(){
        System.out.println("Thread 3 begins!");
        synchronized (waiter) {
            try {
                System.out.println("Thread 3 awaits");
                waiter.wait();
                latch4.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Thread 3 finishes!");
    }
}
