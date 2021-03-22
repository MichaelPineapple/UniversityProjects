import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main
{
    static boolean RUNNING = true;
    static int BUFFER_MAX_SIZE = 10;
    static ArrayList<Integer> BUFFER = new ArrayList<>();

    public static void main(String[] args)
    {
        // start threads
        Thread producerThread = new Producer();
        Thread consumerThread = new Consumer();
        producerThread.start();
        consumerThread.start();

        try { Thread.sleep(50000); }
        catch (InterruptedException e) { e.printStackTrace(); }
        RUNNING = false;

        System.out.println("Done");
    }

    static class Producer extends Thread
    {
        public void run()
        {
            try
            {
                while (RUNNING)
                {
                    int randomNum = ThreadLocalRandom.current().nextInt(-100, 100);
                    BUFFER.add(randomNum);
                    System.out.println("Producer produced: "+randomNum);

                    if (BUFFER.size() >= BUFFER_MAX_SIZE)
                    {
                        System.out.println("Buffer full, producer waiting... ");
                        synchronized (BUFFER)
                        {
                            BUFFER.notifyAll();
                            BUFFER.wait();
                        }
                    }
                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    static class Consumer extends Thread
    {
        public void run()
        {
            try
            {
                while (RUNNING)
                {
                    if (BUFFER.size() > 0)
                    {
                        int data = BUFFER.get(0);
                        BUFFER.remove(0);
                        System.out.println("Consumer consumed: " + data);
                    }
                    else System.out.println("Buffer empty, consumer waiting... ");
                    
                    synchronized (BUFFER)
                    {
                        BUFFER.notifyAll();
                        BUFFER.wait();
                    }
                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
