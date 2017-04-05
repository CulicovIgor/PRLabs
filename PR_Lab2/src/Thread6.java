public class Thread6 extends Thread {
    private final Object waiter;

    Thread6(Object waiter){
        this.waiter = waiter;
    }

    @Override
    public void run(){
        System.out.println("Thread 6 begins!");
        synchronized (waiter) {
            try {
                System.out.println("Thread 6 awaits");
                waiter.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Thread 6 finishes!");
    }
}