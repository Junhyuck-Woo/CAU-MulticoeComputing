// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: May 17, 2020
// Project #2
// - BlockingQueue with Producer-Consumer Structure

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParkingGarageSimulationPC {
    public static void main(String[] args) {
        // Create hte blocking queue
        BlockingQueue places = new ArrayBlockingQueue<String>(10);

        // Create the garage - consumer
        Garage garage = new Garage(places);

        // Create the Car - producer
        for (int i=1; i<=40; i++){
            Car c = new Car("Car " +i, places);
        }
    }
}

class Garage extends Thread{

    // Variable
    private BlockingQueue places;

    // Constructor
    Garage(BlockingQueue places) {
        this.places = places;
        start();
    }

    public void leave() {
        synchronized (System.out) {
            try {
                // Print the status of car: LEAVE
                System.out.println(this.places.take() + ": left");
            }
            catch (InterruptedException e) {}
        }
    }

    public void run() { // enter parking garage
        while (true) {
            try {
                if (this.places.size() != 0) {
                    sleep((int)Math.random()*10000);
                    leave();
                }
            }
            catch (InterruptedException e) {}
        }
    }
}


class Car extends Thread {
    // Variable
    private BlockingQueue places;

    // Constructor
    public Car(String name, BlockingQueue places) {
        super(name);
        this.places = places;
        start();
    }

    public synchronized void enter() {
        try{
            this.places.put(getName());
            synchronized (System.out) {
                // Print the status of car: ENTER
                System.out.println(getName() + ": entered");
            }
        }
        catch (InterruptedException e) {}
    }

    // Runner
    public void run() {
        while (true) {
            try {
                if (!this.places.contains(getName())){
                    sleep((int)Math.random()*10000);
                    // Print the status of car: TRY TO ENTER
                    System.out.println(getName() + ": trying to enter");
                    enter();
                }
            }
            catch (InterruptedException e) {}
        }
    }
}
