class SynchronizationDemo2 {
    public static void main (String [] args) {
        FinTrans2 ft = new FinTrans2();
        TransThread2 tt1 = new TransThread2(ft, "Deposit Thread");
        TransThread2 tt2 = new TransThread2(ft, "Withdrawal Thread");
        tt1.start();
        tt2.start();
    }
}

class FinTrans2 {
    private String transName;
    private double amount;
    synchronized void update (String transName, double amount) {
        this.transName = transName;
        this.amount = amount;
        System.out.println(this.transName + " " + this.amount);
    }
}

class TransThread2 extends Thread {
    private FinTrans2 ft;
    TransThread2 (FinTrans2 ft, String name) {
        super (name); // Save thread's name
        this.ft = ft; // Save reference to financial transaction object
    }
    public void run () {
        for (int i=0; i<100; i++) {
            if (getName().equals("Deposit Thread")) {
                ft.update("Deposite", 2000.0);
            }
            else {
                ft.update("Withdrawal", 250.0);
            }
        }
    }
}