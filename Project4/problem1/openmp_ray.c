#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <omp.h>

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

float hit( float x, float y, float z, float ox, float oy, float *n,  float radius ) {
    float dx = ox - x;
    float dy = oy - y;
    if (dx*dx + dy*dy < radius*radius) {
        float dz = sqrtf( radius*radius - dx*dx - dy*dy );
        *n = dz / sqrtf( radius * radius );
        return dz + z;
    }
    return -INF;
}

void kernel(int x, int y, struct Sphere* s, unsigned char* ptr)
{
    int offset = x + y*DIM;
    float ox = (x - DIM/2);
    float oy = (y - DIM/2);

    //printf("x:%d, y:%d, ox:%f, oy:%f\n",x,y,ox,oy);

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
    int no_threads;
    int option;
    int x,y;
    unsigned char* bitmap;
    double start_time, end_time;

    srand(time(NULL));

    if (argc!=3) {
        printf("> a.out [option] [filename.ppm]\n");
        printf("[option] 0: CUDA, 1~16: OpenMP using 1~16 threads\n");
        printf("for example, '> a.out 8 result.ppm' means executing OpenMP with 8 threads\n");
        exit(0);
    }
    FILE* fp = fopen(argv[2],"w");

    if (strcmp(argv[1],"0")==0) option=CUDA;
    else {
        // Set the thread number
        option=OPENMP;
        no_threads=atoi(argv[1]);
        omp_set_num_threads(no_threads);
    }
    
    // Set the timer
    start_time = omp_get_wtime( );
    struct Sphere *temp_s = (struct Sphere*)malloc( sizeof(struct Sphere) * SPHERES );
    bitmap=(unsigned char*)malloc(sizeof(unsigned char)*DIM*DIM*4);
    #pragma omp parallel private(x) private(y)
    {
        #pragma omp for 
        for (int i=0; i<SPHERES; i++) {
            temp_s[i].r = rnd( 1.0f );
            temp_s[i].g = rnd( 1.0f );
            temp_s[i].b = rnd( 1.0f );
            temp_s[i].x = rnd( 2000.0f ) - 1000;
            temp_s[i].y = rnd( 2000.0f ) - 1000;
            temp_s[i].z = rnd( 2000.0f ) - 1000;
            temp_s[i].radius = rnd( 200.0f ) + 40;
        }
        
        #pragma omp for 
        for (x=0;x<DIM;x++)
            for (y=0;y<DIM;y++)
                kernel(x,y,temp_s,bitmap);
    }
    
    ppm_write(bitmap,DIM,DIM,fp);
    end_time = (omp_get_wtime() - start_time);

    printf("OpenMP (%d threads) ray tracing: %.2lf sec\n", no_threads, end_time);
    printf("[%s] was generated\n", argv[2]);

    fclose(fp);
    free(bitmap);
    free(temp_s);

    return 0;
}