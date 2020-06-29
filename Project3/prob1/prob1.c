// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: Jun 20, 2020
// Project #3
//  - problem 1

# include <stdio.h>
# include <omp.h>
# include <stdlib.h>

int isPrime(int x);

int main(int argc, char *argv[]) {
    
    // Variables
    int i = 0;
    int tid = 0;
    int type = 0;
    int count = 0;
    int num_prime = 0;
    int num_thread = 0;
    double start_time, end_time;

    // Check the input argument
    if (argc != 3) {
        printf("ERROR: Wrong number of arguments\n");
        return 0;
    }

    // Check the schedule type
    type = atoi(argv[1]);
    if (type < 0 || type > 3) {
        printf("ERROR: Wrong schedule type\n");
        return 0;
    }

    // Set the thread number
    num_thread = atoi(argv[2]);
    omp_set_num_threads(num_thread);

    start_time = omp_get_wtime( );
    #pragma omp parallel private(count) private(tid) 
    {
        count = 0;
        tid = omp_get_thread_num();

        // Static
        if (type == 1) {
            #pragma omp for schedule(static)
            for (i = 0; i < 200000; i++) {
                if (isPrime(i)) {
                    count++;
                }
            }
        } 
        // Dynamic
        else if (type == 2) {
            #pragma omp for schedule(dynamic, 4)
            for (i = 0; i < 200000; i++) {
                if (isPrime(i)) {
                    count++;
                }
            }
        } 
        // Guided
        else if (type == 3) {
            #pragma omp for schedule(guided, 4)
            for (i = 0; i < 200000; i++) {
                if (isPrime(i)) {
                    count++;
                }
            }
        }
        
        printf("%d Done: %d\n", tid, count);
        #pragma omp atomic
        num_prime += count;
    }
    /* implicit barrier */
    printf("Total: %d\n", num_prime);

    end_time = omp_get_wtime();
	printf("time elapsed: %.3lf ms\n", (end_time-start_time)*1000);
    
    return 0;
}

int isPrime(int x) {
    int i;
    if (x<=1) {
        return 0;
    }
    for (i=2; i<x; i++) {
        if ((x%i == 0) && (i!=x)) {
            return 0;
        }
    }
    return 1;
}