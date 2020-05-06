// Prof: Bongsoo Sohn
// Org: College of Software, Chung-Ang University
// Lecture: Multicore Computing
// Title: ThreadDemo.java
// Date: May6, 2020

class ThreadDemo {
    public static void main (String [] args) {
        MyThread mt = new MyThread ();
        mt.start();
        for (int i = 0; i < 50; i++) {
            System.out.println("i = " + i + ", i * i = " + i * i);
        }
    }
}
class MyThread extends Thread {
    public void run () {
        for (int count = 1, row = 1; row < 20; row++, count++) {
            for (int i = 0; i< count; i++) {
                System.out.print ('*');
            }
            System.out.print('\n');
        }
    }
}