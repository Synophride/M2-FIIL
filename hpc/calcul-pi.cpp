#include <chrono>
#include <iostream>
#include "omp.h"

inline double f(double x)
{
  return (4 / (1 + x * x));
}

int main()
{
    omp_set_num_threads(4);
    int i;
    const int N = 100000000;
    double pi = 0.0;
    auto start = std::chrono::high_resolution_clock::now();
#pragma omp parallel reduction(+:pi)
    {

	// En premiere partie, calculer le pi avec omp for et reduction
	double s = 1./N;
	for(i = 0; i<N; i++){
	    pi += (s * f(i*s) + f((i+1.) * s))/2;
	}
    }
    std::cout << "pi = " << pi << std::endl;
    std::chrono::duration<double> tempsOmpFor = std::chrono::high_resolution_clock::now() - start;
    std::cout << "Temps parallel omp for: " << tempsOmpFor.count() << "s\n";

    // En deuxieme partie, calculer le pi avec boucle parallele faite a la main
    pi = 0.0;
    start = std::chrono::high_resolution_clock::now();
    // A FAIRE ...
    std::cout << "pi = " << pi << std::endl;
    std::chrono::duration<double> tempsForMain = std::chrono::high_resolution_clock::now() - start;
    std::cout << "Temps parallel for a la main: " << tempsForMain.count() << "s\n";

    return 0;
}
