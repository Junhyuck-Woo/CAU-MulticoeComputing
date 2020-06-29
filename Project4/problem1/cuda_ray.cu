#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>

#define CUDA 0
#define OPENMP 1
#define SPHERES 20

#define rnd( x ) (x * rand() / RAND_MAX)
#define INF 2e10f
#define DIM 2048

struct Sphere {
    float   r,b,g;
    float   radius;
    float   x,y,z;
};

__device__ float hit( float x, float y, float z, float ox, float oy, float *n,  float radius ) {
    float dx = ox - x;
    float dy = oy - y;
    if (dx*dx + dy*dy < radius*radius) {
        float dz = sqrtf( radius*radius - dx*dx - dy*dy );
        *n = dz / sqrtf( radius * radius );
        return dz + z;
    }
    return -INF;
}

__global__ void kernel(struct Sphere* s, unsigned char* ptr)
{
    int x = blockIdx.x;
    int y = blockIdx.y;
    int offset = x + y*DIM;
    float ox = (x - DIM/2);
    float oy = (y - DIM/2);

    float r=0, g=0, b=0;
    float   maxz = -INF;
    for(int i=0; i<SPHERES; i++) {
        float   n;
        float   t = hit( s[i].x, s[i].y, s[i].z, ox, oy, &n, s[i].radius );
        if (t > maxz) {
            float fscale = n;
            r = s[i].r * fscale;
            g = s[i].g * fscale;
            b = s[i].b * fscale;
            maxz = t;
        }
    }

    ptr[offset*4 + 0] = (int)(r * 255);
    ptr[offset*4 + 1] = (int)(g * 255);
    ptr[offset*4 + 2] = (int)(b * 255);
    ptr[offset*4 + 3] = 255;
}

void ppm_write(unsigned char* bitmap, int xdim,int ydim, FILE* fp)
{
    int i,x,y;
    fprintf(fp,"P3\n");
    fprintf(fp,"%d %d\n",xdim, ydim);
    fprintf(fp,"255\n");
    for (y=0;y<ydim;y++) {
        for (x=0;x<xdim;x++) {
            i=x+y*xdim;
            fprintf(fp,"%d %d %d ",bitmap[4*i],bitmap[4*i+1],bitmap[4*i+2]);
        }
        fprintf(fp,"\n");
    }
}

int main(int argc, char* argv[])
{
    double exe_time;
    clock_t start_time, end_time;
    struct Sphere *temp_s;
    unsigned char* bitmap;
    struct Sphere *d_temp_s;
    unsigned char* d_bitmap;
    dim3 blocks(DIM,DIM,1);

    // Error detection code
    if (argc!=2) {
        printf("> a.out [filename.ppm]\n");
        printf("for example, '> a.out result.ppm' means executing CUDA\n");
        exit(0);
    }

    // Start Timer
    srand(time(NULL));
    start_time = clock();

    // Allocate the memory on host
    bitmap = (unsigned char*)malloc(sizeof(unsigned char)*DIM*DIM*4);
    temp_s = (struct Sphere*)malloc(sizeof(struct Sphere) * SPHERES);
    // Allocate the memory on device
    cudaMalloc( (void**)&d_temp_s, sizeof(struct Sphere) * SPHERES );
    cudaMalloc( (void**)&d_bitmap, sizeof(unsigned char)*DIM*DIM*4 );

    // Generate the spheres
    for (int i=0; i<SPHERES; i++) {
        temp_s[i].r = rnd( 1.0f );
        temp_s[i].g = rnd( 1.0f );
        temp_s[i].b = rnd( 1.0f );
        temp_s[i].x = rnd( 2000.0f ) - 1000;
        temp_s[i].y = rnd( 2000.0f ) - 1000;
        temp_s[i].z = rnd( 2000.0f ) - 1000;
        temp_s[i].radius = rnd( 200.0f ) + 40;
    }

    //  Move data to device
    cudaMemcpy ( d_temp_s, temp_s, sizeof(struct Sphere) * SPHERES, cudaMemcpyHostToDevice );

    // Calculate the ray
    kernel<<<blocks, 1>>>(d_temp_s, d_bitmap);
    cudaDeviceSynchronize();
    cudaMemcpy ( bitmap, d_bitmap, sizeof(unsigned char)*DIM*DIM*4, cudaMemcpyDeviceToHost );

    // open the file
    FILE* fp = fopen(argv[1],"w");
    ppm_write(bitmap,DIM,DIM,fp);   // Write the image

    // Stop Timer
    end_time = clock();
    exe_time = ((double)(end_time - start_time)) / CLOCKS_PER_SEC;

    // Print the result
    printf("CUDA ray tracing: %f sec\n", exe_time);
    printf("[%s] was generated\n", argv[1]);

    // Close the file and free the memory
    fclose(fp);
    free(bitmap);
    free(temp_s);
    cudaFree(d_bitmap);
    cudaFree(d_temp_s);
    return 0;
}