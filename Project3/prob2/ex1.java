// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: June 13, 2020
// Project #3
//  - problem 2: BlockingQueue
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ex1 {

    public static void main(String[] args) {
        BlockingQueue table = new ArrayBlockingQueue<String>(3);
        Cook cook = new Cook(table);
        Philosopher p1 = new Philosopher(table, "1");
        Philosopher p2 = new Philosopher(table, "2");
        Philosopher p3 = new Philosopher(table, "3");

        cook.start();
        p1.start();
        p2.start();
        p3.start();
    }
}

class Cook extends Thread {
    private BlockingQueue dish;
    public Cook(BlockingQueue table) { dish = table; }
    public void run() {
        for (int i=0; i < 12; i++) {
            System.out.println("Chef: Cook cuisine(" + i + ")");
            try {
                dish.put("cuisine ("+i+")");
                Thread.sleep((int)(Math.random()*2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Philosopher extends Thread {
    private BlockingQueue dish;
    private String id;
    public Philosopher(BlockingQueue table, String num) { dish = table; id = num; }
    public void run() {
        String str;
        for (int i=0; i<4; i++) {
            try {
                str=(String)dish.take();
                System.out.println("Philosopher" + id + ": Eat " + str);
                Thread.sleep((int)(Math.random()*2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}