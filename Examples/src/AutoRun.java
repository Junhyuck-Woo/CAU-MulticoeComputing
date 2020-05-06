// Prof: Bongsoo Sohn
// Org: College of Software, Chung-Ang University
// Lecture: Multicore Computing
// Title: AutoRun.java
// Date: May6, 2020

class AutoRun implements Runnable {
    public AutoRun() {
        new Thread(this).start();
    }
    public void run() {
        System.out.println("AutoRun.run()");
    }
}

class AutoRunDemo {
    public static void main(String[] args) {
        AutoRun t1 = new AutoRun();
        System.out.println("InsideMain()");
    }
}
