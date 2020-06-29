// Writer: Junhyuck Woo
// Lecture: Multicore Computing
// Organization: Chung-Ang University
// Deadline: June3 20, 2020
// Project #4
// - Thrust

#include <thrust/fill.h>
#include <thrust/reduce.h>
#include <thrust/sequence.h>
#include <thrust/transform.h>
#include <thrust/device_vector.h>
#include <iostream>
#include <time.h>

using namespace std;
#define N 2000000.0

int main(int argc, char* argv[])
{
    float sum = 0;
    clock_t start_time, end_time;
    double exec_time = 0;
    
    // allocate three device_vectors
    thrust::device_vector<float> X(N);
    thrust::device_vector<float> Y(N);
    thrust::device_vector<float> Z(N);

    // Start timer;
    start_time = clock();

    // initialize X to 0,1,2,3, ....
    thrust::sequence(X.begin(), X.end());
    thrust::fill(Y.begin(), Y.end(), N);
    // Divide X as N
    thrust::transform(X.begin(), X.end(), Y.begin(), X.begin(), thrust::divides<float>());

    // Calculation
    thrust::transform(X.begin(), X.end(), X.begin(), X.begin(), thrust::multiplies<float>());// X = X*X 
    thrust::fill(Y.begin(), Y.end(), 1.0); // Y <- 1.0
    thrust::transform(X.begin(), X.end(), Y.begin(), X.begin(), thrust::plus<float>()); // X = X + 1
    thrust::fill(Y.begin(), Y.end(), 4.0); // Y <- 4.0
    thrust::transform(Y.begin(), Y.end(), X.begin(), Z.begin(), thrust::divides<float>()); // z = 4.0 / X
    thrust::fill(Y.begin(), Y.end(), N);
    thrust::transform(Z.begin(), Z.end(), Y.begin(), Z.begin(), thrust::divides<float>()); // z = z / N

    // Sum the calculation result
    sum = thrust::reduce(Z.begin(), Z.end(), (float)0.0, thrust::plus<float>());

    // End timer
    end_time = clock();
    exec_time = (double)(end_time - start_time)*1000 / CLOCKS_PER_SEC;

    // Print the result
    cout << "N: 2000000.0" << endl;
    cout << "Excution Time: " << exec_time << " ms" << endl;
    cout << "Result: " << sum << endl;

    return 0;
}