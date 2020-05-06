// Prof: Bongsoo Sohn
// Org: College of Software, Chung-Ang University
// Lecture: Multicore Computing
// Title: AutoRunCurrent.java
// Date: May6, 2020

class AutoRun implements Runnable {
    private Thread _me;
    public AutoRun() {
        _me = new Thread(this);
        _me.start();
    }
    public void run() {
        if (_me == Thread.currentThread()) {
            System.out.println(("AutoRun.run()"));
        }
    }
}

class Main {
    public static void main (String[] args) {
        AutoRun t1 = new AutoRun(); // printout
        t1.run(); // no printout
        System.out.println("InsideMain()");
    }
}