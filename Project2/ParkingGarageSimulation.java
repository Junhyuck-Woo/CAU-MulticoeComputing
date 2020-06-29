// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: May 17, 2020
// Project #2
// - BlockingQueue Only

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParkingGarageSimulation {
    // Main
    public static void main(String[] args) {
        // Create hte blocking queue
        BlockingQueue places = new ArrayBlockingQueue<String>(10);

        // Create the garage
        Garage garage = new Garage(places);

        // Create the Car
        for (int i=1; i<=40; i++){
            Car c = new Car("Car " +i, places, garage);
        }
    }
}

class Garage {

    // Variable
    private BlockingQueue places;

    // Constructor
    Garage(BlockingQueue places) {
        this.places = places;
    }

    public synchronized void enter(String name) {

        try{
            // Car enters the garage
            this.places.put(name);
            synchronized (System.out) {
                // Print the status of car: ENTER
                System.out.println(name + ": entered");
            }
        }
        catch (InterruptedException e) {}
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
}


class Car extends Thread {
    // Variables
    private BlockingQueue places;
    private Garage garage;

    // Constructor
    public Car(String name, BlockingQueue places, Garage garage) {
        super(name);
        this.places = places;
        this.garage = garage;
        start();
    }

    // Runner
    public void run() {
        while (true) {
            try {
                sleep((int)Math.random()*10000);
            }
            catch (InterruptedException e) {}
            // Print the status of car: TRY TO ENTER
            System.out.println(getName() + ": trying to enter");
            garage.enter(getName());

            try {
                sleep((int)Math.random()*10000);
            }
            catch (InterruptedException e) {}
            garage.leave();
        }
    }
}
