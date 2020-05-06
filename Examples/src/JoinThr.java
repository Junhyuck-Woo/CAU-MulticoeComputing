// Prof: Bongsoo Sohn
// Org: College of Software, Chung-Ang University
// Lecture: Multicore Computing
// Title: JoinThr.java
// Date: May6, 2020

public class JoinThr {
    static public void main (String a[]) {
        MyThread1 Thread_a; // Define a Thread
        MyThread2 Thread_b; // Define another Thread

        Thread_a = new MyThread1();
        Thread_b = new MyThread2(Thread_a);

        // Start the threads
        System.out.println("Starting the threads...");
        Thread_a.start();
        Thread_b.start();
    }
}

// Thread class that just prints a message 5 times
class MyThread1 extends Thread {
    public void run() {
        System.out.println(getName() + " is runnging...");
        for (int i=0; i<4; i++) {
            try { // Sleep a bit
                sleep(500);
            }
            catch (InterruptedException e) {}
            System.out.println(("Hello there, from" + getName()));
        }
    }
}

// Thread class that just prints a message 5 times
class MyThread2 extends Thread {
    private Thread wait4me; // Thread to wait for

    // Constructor
    MyThread2(Thread target) {
        super();
        wait4me = target;
    }

    public void run() {
        System.out.println(getName() + " is waiting for " + wait4me.getName() + "...");
        try { // wait for target thread to finish
            wait4me.join();
        }
        catch (InterruptedException e) {}
        System.out.println(wait4me.getName() + " has finished...");
        // print message 4 times

        for (int i=0; i<4; i++) {
            try { // Sleep a bit
                sleep(500);
            }
            catch (InterruptedException e) {}
            System.out.println(("Hello there, from" + getName()));
        }
    }
}