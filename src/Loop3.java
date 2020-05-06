// Prof: Bongsoo Sohn
// Org: College of Software, Chung-Ang University
// Lec: Multicore Computing
// Title: Loop3.java
// Date: May6, 2020

public class Loop3 extends Thread{
    public Loop3(String name) {
        super(name); // pass name to superclass
    }
    public void run() {
        for(int i = 1; i <= 10000; i++) {
            System.out.println(getName() + " (" + i + ")");
            try {
                sleep(10); // sleep for 10 milisecs
            }
            catch(InterruptedException e) {}
        }
    }
    public static void main(String[] args) {
        Loop3 t1 = new Loop3("Thread 1");
        Loop3 t2 = new Loop3("Thread 2");
        Loop3 t3 = new Loop3("Thread 3");
        t1.start();
        t2.start();
        t3.start();
    }
}
