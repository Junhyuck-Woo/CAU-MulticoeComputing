// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: June 13, 2020
// Project #3
//  - problem 2: Semaphore
import java.util.concurrent.Semaphore;

public class ex2 {

    public static void main(String[] args) {
        Semaphore kitchen = new Semaphore(1);
        Chef cook1 = new Chef(kitchen, "1");
        Chef cook2 = new Chef(kitchen, "2");
        Chef cook3 = new Chef(kitchen, "3");

        cook1.start();
        cook2.start();
        cook3.start();
    }
}

class Chef extends Thread {
    private Semaphore fire;
    private String id;
    public Chef(Semaphore kitchen, String num) { fire = kitchen; id = num; }
    public void run() {
        for (int i=0; i < 5; i++) {
            try {
                fire.acquire();
                System.out.println("Chef" + id + ": Cook cuisine(" + i + "th)");
                Thread.sleep((int)(Math.random()*2000));
                fire.release();
                Thread.sleep((int)(Math.random()*2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
