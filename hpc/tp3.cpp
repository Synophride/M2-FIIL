#include <iostream>
#include <stdlib.h>
#include <stdio.h>
#include <immintrin.h>
#include <assert.h>
#include <chrono>

const int N = 500000;
const int ITER = 10000;
void copie_non_vectorisee(float*a, float* b){
    for(int i = 0; i<N; i++)
	b[i] = a[i];
}

void copie_vectorisee(float* a, float* b){
    __m256 arr;

    int i;
    for(i = 0; i<N-7; i+=8){
	arr =_mm256_load_ps(a+i);
	_mm256_store_ps(b+i, arr);
    }

    for(; i<N; i++)
	b[i] = a[i];
    
}

void copie_deroulee(float* a, float*b){
    __m256 arr[4];
    int i;
    for(i = 0; i<N-8*4; i+=8*4){
	arr[0] =_mm256_load_ps(a + i);
	_mm256_store_ps(b + i, arr[0]);	
	
	arr[1] =_mm256_load_ps(a+i+8);
	_mm256_store_ps(b + i + 8, arr[1]);

	arr[2] =_mm256_load_ps(a + i +2*8);
	_mm256_store_ps(b + i + 2*8, arr[2]);

	arr[3] =_mm256_load_ps(a + i + 3*8);
	_mm256_store_ps(b + i + 3*8, arr[3]);
    }
    
    for(; i<N-7; i+=8){
	arr[0] =_mm256_load_ps(a+i);
	_mm256_store_ps(b+i, arr[0]);
    }

    for(; i<N; i++)
	b[i] = a[i];
    
}


///
bool eq(float* a, float*b){
    for(int i = 0; i<N; i++)
	if(a[i] != b[i])
	    return false;
    
    return true;
}

void init(float*a){
    for (int i = 0; i<N; i++)
	a[i] = i;
}

float* new_tab(float* b){
    free(b);
    return (float*) _mm_malloc(sizeof(float)*N, 32);
}

///
int main_exo1(){
    float* a = (float*) _mm_malloc(sizeof(float) * N, 32);
    float* b = (float*) _mm_malloc(sizeof(float) * N, 32);
    std::chrono::time_point<std::chrono::steady_clock> begin, end;
    std::chrono::duration<double> duree;

    init(a);
    begin = std::chrono::steady_clock::now();
    for(int j = 0; j<ITER; j++)
	copie_non_vectorisee(a, b);
    end = std::chrono::steady_clock::now();
    duree = end - begin;
    assert(eq(a, b));
    std::cout << "Copie non vectorisée : \t" << duree.count() << std::endl;


    b = new_tab(b);
    begin = std::chrono::steady_clock::now();
    for(int j = 0; j<ITER; j++)
	copie_vectorisee(a, b);
    end = std::chrono::steady_clock::now();
    
    duree = end - begin;
    assert(eq(a, b));
    std::cout << "Copie oui-vectorisée : \t" << duree.count() << std::endl;


    begin = std::chrono::steady_clock::now();
    for(int j = 0; j<ITER; j++)
	copie_deroulee(a, b);
    end = std::chrono::steady_clock::now();
    duree = end - begin;
    assert(eq(a, b));
    std::cout << "Copie oui-vectorisée avec déroulage : \t" << duree.count() << std::endl;
    
    free(a);
    free(b);
    return 0;
}

/******* EXERCICE 2 **********/
const int z = 1536;
const int M = z * 8;
const int ITER2 = 1500;

float produit_scalaire_sequentiel(float* a, float*b){
    float sum = 0.;
    for (int i = 0; i<M; i++)
	sum += a[i] * b[i];

    return sum;
}

float produit_scalaire_vectorise(float* a, float* b){
    float sul = 0;
    __m256 arr_a, arr_b, tmpr;
    
    for(int i = 0; i<M; i+=8){
	arr_a =_mm256_load_ps(a + i);
	arr_b =_mm256_load_ps(b+i);
	tmpr = _mm256_mul_ps(ar_a, arr_b);
	
	
    }
}  
////

void init(float* a,float* b){
    
    for(int i = 0; i<M; i++){
	a[i] = i;
	b[i] = M-i;
    }
}

float print_time(float (*fptr)(float*, float*) , float* a, float* b ){
    std::chrono::time_point<std::chrono::steady_clock> begin, end;
    std::chrono::duration<double> duree;
    begin = std::chrono::steady_clock::now();
    float z;
    for(int i = 0; i<ITER2; i++)
	z = (*fptr)(a, b);
    
    end = std::chrono::steady_clock::now();
    duree = end - begin;
    std::cout << duree.count() << std::endl;
    return z;
}

int main_e2(){
    float * a = new float[M];
    float * b = new float[M];
    float res_seq, res_vect, res_dev;
    // float (*f)(float*, float*)
    
    std::cout << "Produit séquentiel: \t";
    res_seq = print_time(&produit_scalaire_sequentiel, a, b);


    
    return 0;
}

int main(){
    return main_e2();
}
