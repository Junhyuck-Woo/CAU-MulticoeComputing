import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParkingGarageSimulation {
    public static void main(String[] args) {
        Garage garage = new Garage(40);
        for (int i=1; i<=40; i++){
            Car c = new Car("Car " +i, garage);
        }
    }
}

class Garage {
    
    private int places;
    
    Garage(int num_place) {
        if (num_place < 0) {
            places = 0;
        }
        places = num_place;
    }

    public synchronized void enter() { // enter parking garage
        while (places == 0) {
            try {
                wait();;
            }
            catch (InterruptedException e) {}
        }
        places--;
    }
    public synchronized void leave() { // leave parking garage
        places++;
        notify();
    }
}


class Car extends Thread {
    private Garage garage;

    public Car(String name, Garage p) {
        super(name);
        this.garage = p;
        start();
    }
    public void run() {
        while (true) {
            try {
                sleep((int)Math.random()*10000); // derive before parking
            }
            catch (InterruptedException e) {}
            garage.enter();
            System.out.println(getName() + ": entered");
            try {
                sleep((int)Math.random()*10000); // stay within the parking garage
            }
            catch (InterruptedException e) {}
            garage.leave();
            System.out.println(getName() +": left");
        }
    }
}
