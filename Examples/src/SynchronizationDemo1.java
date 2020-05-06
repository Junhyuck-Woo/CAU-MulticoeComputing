class SynchronizationDemo1 {
    public static void main (String [] args) {
        FinTrans3 ft = new FinTrans3();
        TransThread3 tt1 = new TransThread3(ft, "Deposit Thread");
        TransThread3 tt2 = new TransThread3(ft, "Withdrawal Thread");
        tt1.start();
        tt2.start();
    }
}

class FinTrans3 {
    public static String transName;
    public static double amount;
}

class TransThread3 extends Thread {
    private FinTrans3 ft;

    TransThread3 (FinTrans3 ft, String name) {
        super (name); // Save thread's name
        this.ft = ft; // Save reference to financial transaction object
    }

    public void run() {
        for (int i=0; i<10; i++) {
            if (getName().equals("Deposit Thread")) {
                synchronized (ft) {
                    ft.transName = "Deposit";
                    try {
                        Thread.sleep((int) (Math.random() * 1000));
                    }
                    catch (InterruptedException e) {}
                    ft.amount = 2000.0;
                    System.out.println(ft.transName + " " + ft.amount);
                }
            }
            else {
                synchronized (ft) {
                    ft.transName = "Withdrawal";
                    try {
                        Thread.sleep((int)(Math.random()*1000));
                    }
                    catch (InterruptedException e) {}
                    ft.amount = 250.0;
                    System.out.println(ft.transName + " " + ft.amount);
                }
            }
        }
    }
}