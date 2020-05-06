// Prof: Bongsoo Sohn
// Org: College of Software, Chung-Ang University
// Lecture: Multicore Computing
// Title: MyRunnable.java
// Date: May6, 2020

class MyRunnable implements Runnable{
    public void run() {
        System.out.println("MyRunnable.run()");
    }
    //other methods and data for this class
}

class MyRunnableDemo {
    public static void main(String[] args) {
        MyRunnable myrun = new MyRunnable();
        Thread t1 = new Thread(myrun);
        t1.start();
        System.out.println("InsideMain()");
    }
}