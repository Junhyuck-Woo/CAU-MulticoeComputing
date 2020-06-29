// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: May 10, 2020
// Project #1
//  - problem 2

import java.util.*;
import java.lang.*;

// command-line execution example) java MatmultD 6 < mat500.txt
// 6 means the number of threads to use
// < mat500.txt means the file that contains two matrices is given as standard input
//
// In eclipse, set the argument value and file input by using the menu [Run]->[Run Configurations]->{[Arguments], [Common->Input File]}.

// Original JAVA source code: http://stackoverflow.com/questions/21547462/how-to-multiply-2-dimensional-arrays-matrix-multiplication
public class MatmultD_dynamic
{

    public static void main(String [] args)
    {
        int thread_no=0;
        if (args.length==1) thread_no = Integer.valueOf(args[0]);
        else thread_no = 1;

        // Create class for calculation
        matrixOperator mo = new matrixOperator(thread_no);

        // Run the operation
        mo.run();

        // Dealocate the memory
        mo = null;
    }
}

class matrixOperator {
    // Variable
    private int matrix_sum = 0;
    private int thread_num = 0;
    private int[] work = new int[1];
    private int[][] ans;
    private static Scanner sc = new Scanner(System.in);

    // Constructor
    public matrixOperator(int thread_no) {
        thread_num = thread_no;
        work[0] = 0;
    }

    // Read Matrix method
    public int[][] readMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = sc.nextInt();
            }
        }
        return result;
    }

    // Runner
    public void run() {
        // Variable
        int[][] a=readMatrix();
        int[][] b=readMatrix();
        Matirx[] m = new Matirx[thread_num];
        ans = new int[a.length][b[0].length];

        // Set timer
        long startTime = System.currentTimeMillis();

        // Run
        for (int i=0; i<thread_num; i++) {
            m[i] = new Matirx(work, a, b, ans);
            m[i].start();
        }

        // Wait the work is done
        for (int i=0; i<thread_num; i++) {
            try {
                m[i].join();
                matrix_sum += m[i].getResult();
            }
            catch (InterruptedException e) {}
        }

        // Finish timer
        long endTime = System.currentTimeMillis();

        // Visualize the execution time of each thread
        System.out.println("1. Execution time of each thread");
        for (int i=0; i<thread_num; i++) {
            System.out.println(m[i].getName() + " : " + m[i].getRuntime() +"ms");
        }

        // Visualize the total execution time
        System.out.println("\n2. Total Execution Time: " + (endTime-startTime) +"ms\n");

        // Visualize the sum of elements
        System.out.println("3. Matrix Sum = " + matrix_sum + '\n');

        // Dealocate the memory
        a = null;
        b = null;
        m = null;
        ans = null;
    }
}

class Matirx extends Thread {
    // Variable
    private int length;
    int result = 0;
    private int[] work;
    private int[][] mat_a;
    private int[][] mat_b;
    private int[][] ans;
    private long runtime = 0;
    private String ID;

    // Constructor
    public Matirx(int[] work, int[][] a, int[][] b, int[][] ans) {
        this.work = work;
        this.ans = ans;
        mat_a = a;
        mat_b = b;
        ID = getName();
    }

    public int multMatrix(int i, int j) {
        // Variable
        int n = mat_a[0].length;
        int buf = 0;

        for(int k = 0; k < n; k++){
            buf += mat_a[i][k] * mat_b[k][j];
        }
        synchronized (ans){
            ans[i][j] = buf;
        }
        return buf;
    }

    // Getter - Work
    public int getWork() {
        synchronized(this.work) {
            this.work[0]++;
            return this.work[0]-1;
        }
    }

    // Getter - Result
    public int getResult(){
        return result;
    }

    // Getter - Runtime
    public long getRuntime(){
        return runtime;
    }

    // Getter - ID
    public String getID() {
        return ID;
    }

    // Runner
    public void run(){
        // Variable
        length = ans.length;
        int i_max = mat_a.length;
        int j_max = mat_b[0].length;

        // Set timer
        long startTime = System.currentTimeMillis();
        while(true) {
            int work = getWork();
            final int i = work/length;
            final int j = work%length;

            if((i>=i_max) || (j>=j_max)){
                break;
            }
            result += multMatrix(i, j);
        }

        // Finish timer
        long endTime = System.currentTimeMillis();
        runtime = endTime - startTime;

        // Dealocate the memory
        work = null;
        mat_a = null;
        mat_b = null;
        ans = null;
    }
}