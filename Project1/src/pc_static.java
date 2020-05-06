// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization:
//
// Deadline: May 10, 2020

import java.awt.desktop.SystemEventListener;

class pc_static {
    // Given Variable
    private static final int NUM_END = 200000;
    private static final int NUM_THREAD = 1;

    // Main Method
    public static void main(String[] args) {
        PrimeOperator po = new PrimeOperator(NUM_END, NUM_THREAD);
        po.run();
    }
}

class PrimeOperator {
    long total_time = 0;
    int num_thread = 0;
    int num_end = 0;

    public PrimeOperator(final int NUM_END, final int NUM_THREAD) {
        num_end = NUM_END;
        num_thread = NUM_THREAD;
    }

    // Variable
    public void run() {
        Prime[] pn = new Prime[num_thread];
        int[] start_num = new int[num_thread];
        int[] end_num = new int[num_thread];
        int prime_num = 0;
        long total_time = 0;

        for (int i=0; i<num_thread; i++) {
            int gap = num_end / num_thread;
            start_num[i] = gap * i;
            end_num[i] = start_num[i] + gap;
            if (i == num_thread-1) {
                end_num[i] = num_end;
            }
        }

        // Set timer
        long startTime = System.currentTimeMillis();

        // Run
        for (int i=0; i<num_thread; i++) {
            // Split the work
            pn[i] = new Prime(start_num[i], end_num[i]);
            pn[i].start();
        }

        for (int i=0; i<num_thread; i++) {
            try {
                pn[i].join();
            }
            catch (InterruptedException e) {}
        }

        // Finish timer
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;

        // Visualize the execution time of each thread
        for (int i=0; i<num_thread; i++) {
            prime_num += pn[i].getPrimeNum();
            System.out.println(pn[i].getName() + ": " + pn[i].getRunTime() +"ms");
        }

        // Visualize the total execution time
        System.out.println("Total Execution Time: "+ runTime +"ms");

        // Visualize the total number of prime number
        System.out.println("Total number of prime number: " + prime_num);
    }
}

class Prime extends Thread {
    private long runTime = 0;
    private int prime_num = 0;
    private int start = 0;
    private int end = 0;

    public Prime(int start_num, int end_num) {
        start = start_num;
        end = end_num;
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        for (int i=start; i<end; i++) {
            if (isPrime(i)) {
                count();
            }
        }
        long endTime = System.currentTimeMillis();
        runTime = endTime - startTime;
    }

    public long getRunTime() {
        return runTime;
    }

    public long getPrimeNum() {
        return prime_num;
    }

    public synchronized void count() {
        prime_num++;
    }

    private boolean isPrime(int x) {
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