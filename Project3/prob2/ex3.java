// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: June 13, 2020
// Project #3
//  - problem 3: ReadWriteLock
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ex3 {

    public static void main(String[] args) {
        int[] book = new int[1];
        book[0] = 0;
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Lock writeLock = lock.writeLock();
        Lock readLock = lock.readLock();

        Student std1 = new Student(writeLock, readLock,"1", book);
        Student std2 = new Student(writeLock, readLock, "2", book);
        Student std3 = new Student(writeLock, readLock, "3", book);
        Student std4 = new Student(writeLock, readLock, "4", book);

        std1.start();
        std2.start();
        std3.start();
        std4.start();
    }
}

class Student extends Thread {
    private Lock writing;
    private Lock reading;
    private String id;
    private int[] page;
    public Student(Lock writelock, Lock readlock, String num, int[] book) {
        writing = writelock;
        reading = readlock ;
        id = num;
        page = book;
    }

    public void run() {
        for (int i=1; i < 3; i++) {

            // Write the pages
            writing.lock();
            try {
                page[0] += 1;
                System.out.println("Student " + id + ": writes (" + page[0] + ")");
            } finally {
                writing.unlock();
            }

            try {
                Thread.sleep((int)(Math.random()*2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Read pages
            reading.lock();
            try {
                System.out.println("Student " + id + ": reads [" + page[0] + "]");
            }  finally {
                reading.unlock();
            }

            try {
                Thread.sleep((int)(Math.random()*2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
