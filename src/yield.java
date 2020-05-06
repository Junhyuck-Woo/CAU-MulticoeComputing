class YieldThread extends Thread {
    public void run() {
        for ( int count = 0; count < 4; count++) {
            System.out.println( count + " From: " + getName() );
            yield();
        }
    }
}
class TestThread {
    public static void main( String[] args ) {
        YieldThread first = new YieldThread();
        YieldThread second = new YieldThread();
        first.start(); second.start();
        System.out.println("End");
    }
}