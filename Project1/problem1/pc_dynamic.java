// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: May 10, 2020
// Project #1
//  - problem 1-2

import java.awt.desktop.SystemEventListener;

class pc_dynamic {
    // Given Variables
    private static final int NUM_END = 200000;
    private static final int NUM_THREAD = 16;

    // Main Method
    public static void main(String[] args) {
        PrimeOperator2 po = new PrimeOperator2(NUM_END, NUM_THREAD);
        po.run();
    }
}

class PrimeOperator2 {
    // Variable for getting a number of thread and end of number
    int num_end = 0;
    int num_thread = 0;
    private int[] work_queue = new int[1];

    // Constructor
    public PrimeOperator2(final int NUM_END, final int NUM_THREAD) {
        work_queue[0] = 0;
        num_end = NUM_END;
        num_thread = NUM_THREAD;
    }

    public void run() {
        // Variables
        Prime2[] pn = new Prime2[num_thread];
        int prime_num = 0;

        // Set timer
        long startTime = System.currentTimeMillis();

        // Runner
        for (int i=0; i<num_thread; i++) {
            pn[i] = new Prime2(work_queue, num_end);
            pn[i].start();
        }

        // Wait the work is done
        for (int i=0; i<num_thread; i++) {
            try {
                pn[i].join();
                prime_num += pn[i].getPrimeNum();
            }
            catch (InterruptedException e) {}
        }

        // Finish timer
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;

        // Visualize the execution time of each thread
        for (int i=0; i<num_thread; i++) {
            System.out.println(pn[i].getName() + ": " + pn[i].getRunTime() +"ms");
        }

        // Visualize the total execution time
        System.out.println("Total Execution Time: "+ runTime +"ms");

        // Visualize the total number of prime number for debugging the result
        System.out.println("Total number of prime number: " + prime_num);
    }

}

class Prime2 extends Thread {
    // Variable
    private long runTime = 0;
    private int prime_num = 0;
    private int start = 0;
    private int end = 0;
    private int[] work_queue = new int[1];

    // Constructor
    public Prime2(int[] work_queue, int end_num) {
        end = end_num;
        this.work_queue = work_queue;
    }

    // Runner
    public void run() {
        // Set a timer
        long startTime = System.currentTimeMillis();

        // Check the number is prime or not with synchronization
        while(true) {
            int work = 0;
            synchronized (this.work_queue) {
                work = getWork();
                if (work >= end){
                    break;
                }
            }
            if(isPrime(work)) {
                count();
            }
        }

        // Check the run time
        long endTime = System.currentTimeMillis();
        runTime = endTime - startTime;
    }

    // Getter - work from queue with synchronization
    public synchronized int getWork() {
        this.work_queue[0]++;
        return (this.work_queue[0]-1);
    }

    // Getter - runt time
    public long getRunTime() {
        return runTime;
    }

    // Getter - Prime number
    public long getPrimeNum() {
        return prime_num;
    }

    // Count the prime number with synchronization
    public synchronized void count() {
        prime_num++;
    }

    // Check whether the number is prime
    // If it is prime, then return True
    // Else return false
    private boolean isPrime(final int x) {
        int i;
        if (x<=1) {
            return false;
        }
        for (i=2; i<x; i++) {
            if ((x%i == 0) && (i!=x)) {
                return false;
            }
        }
        return true;
    }
}