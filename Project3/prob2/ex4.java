// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: June 13, 2020
// Project #3
//  - problem 4: AtomicInteger
import java.util.concurrent.atomic.AtomicInteger;

public class ex4 {
    public static void main(String[] args) {
        AtomicInteger init = new AtomicInteger(10);
        Worker c1 = new Worker(init, "1", 4);
        Worker c2 = new Worker(init, "2", 5);

        c1.start();
        c2.start();
    }
}

class Worker extends Thread {
    private AtomicInteger num;
    private String thread_id;
    private int buf;
    public Worker(AtomicInteger init, String id, int tmp) { num = init; thread_id=id; buf = tmp; }

    public void run() {
        // Get
        System.out.println("Worker " + thread_id + " has a shared num: " + num.get());

        // Set
        num.set(buf);
        System.out.println("Worker " + thread_id + " sets " + buf + " | Result: " + num.get());

        // getAndAdd
        System.out.println("Worker " + thread_id + " getAndAdd " + 10 + " | Call Func: " + num.getAndAdd(10) + " Result: " + num.get());

        // AddAndGet
        System.out.println("Worker " + thread_id + " addAndGet " + 10 + " | Call Func: " + num.addAndGet(10) + " Result: " + num.get());

        // Get
        System.out.println("Worker " + thread_id + " has " + num.get());
    }
}