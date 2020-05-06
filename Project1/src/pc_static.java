// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization:
//
// Deadline: May 10, 2020

import java.awt.desktop.SystemEventListener;

class pc_static {
    // Given Variable
    private static final int NUM_END = 200000;
    private static final int NUM_THREAD = 3;

    // Main Method
    public static void main(String[] args) {
        PrimeOperator po = new PrimeOperator(NUM_END, NUM_THREAD);
        po.run();
    }
}

//
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

        for (int i=0; i<num_thread; i++) {
            int gap = num_end / num_thread;
            start_num[i] = gap * i;
            end_num[i] = start_num[i] + gap;
            if (i == num_thread-1) {
                end_num[i] = num_end;
            }
        }

        for (int i=0; i<num_thread; i++) {
            // Split the work
            pn[i] = new Prime(start_num[i], end_num[i]);
            pn[i].start();
        }
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
        long timeDiff = endTime - startTime;
        System.out.println(getName() + " "+ timeDiff +"ms");
        System.out.println(getName() + " "+ prime_num);
        runTime = timeDiff;
    }

    public long result() {
        //System.out.println(getName() + " "+ runTime +"ms");
        return runTime;
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