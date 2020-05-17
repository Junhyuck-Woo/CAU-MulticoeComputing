// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: May 17, 2020
// Project #2

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParkingGarageSimulation {
    public static void main(String[] args) {
        BlockingQueue places = new ArrayBlockingQueue<String>(1);
        Garage garage = new Garage(places);

        for (int i=1; i<=10; i++){
            Car c = new Car("Car " +i, places);
        }
    }
}

class Garage extends Thread{
    
    private BlockingQueue places;

    Garage(BlockingQueue places) {
        this.places = places;
        start();
    }

    public synchronized void leave() {
        String car_num;
        try {
            //car_num = (String)this.places.element();
            System.out.println(this.places.take() + ": left");
        }
        catch (InterruptedException e) {}
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
    private BlockingQueue places;

    public Car(String name, BlockingQueue places) {
        super(name);
        this.places = places;
        start();
    }

    public synchronized void enter() {
        String car_num = getName();
        System.out.println(car_num + ": trying to enter");
        try {
            this.places.put(car_num);
            System.out.println(car_num + ": entered");
        }
        catch (InterruptedException e) {}
    }

    public void run() {
        while (true) {
            try {
                if (!this.places.contains(getName())){
                    sleep((int)Math.random()*10000);
                    enter();
                }
            }
            catch (InterruptedException e) {}
        }
    }
}
