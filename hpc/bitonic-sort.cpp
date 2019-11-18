#include <algorithm>
#include <fstream>
#include <iostream>
#include <vector>
#include "mpi.h"

int MPI_ScatterSingleInt(
    const int *sendbuf, int *recvbuf, int root, MPI_Comm comm){
    MPI_Scatter(sendbuf, 1, MPI_INTEGER, recvbuf, 1, MPI_INTEGER, root, comm);
  
    return MPI_SUCCESS;
}

int MPI_GatherSingleInt(
    const int *sendbuf, int *recvbuf, int root, MPI_Comm comm){
    MPI_Gather(sendbuf, 1, MPI_INTEGER, recvbuf,1,  MPI_INTEGER, root, comm);
    return MPI_SUCCESS;
}

void bitonicSort(int *arr, int N){
    int procRank;
    
    // ...
    MPI_Comm_rank(MPI_COMM_WORLD, &procRank);
    
    // First scatter the elements of arr in the master process to others so that each process has its element in procElem.
    int procElem;
    MPI_ScatterSingleInt(arr, &procElem, 0, MPI_COMM_WORLD);


    MPI_Request req, req2;
    int received, id_processus_pair;    
  

    // Then perform the bitonic separator for logN steps
    for(int i = N; i > 1; i /= 2 ){

	int group_nb = procRank/i;
	int pos_in_g = procRank % i;
	char sends_first = pos_in_g < i/2;
	int paired_proc = sends_first ? (procRank + (i/2)) : (procRank - (i/2));
	
	MPI_Isend(&procElem, 1, MPI_INT, paired_proc, 0, MPI_COMM_WORLD, &req);
	MPI_Irecv(&received, 1, MPI_INT, paired_proc, 0, MPI_COMM_WORLD, &req2);
	
	MPI_Wait(&req, MPI_STATUS_IGNORE);  
	MPI_Wait(&req2, MPI_STATUS_IGNORE);  
	if((sends_first && received < procElem) || (!sends_first && received > procElem))
	    procElem = received;
	
    }

    // Finally, gather the final results in procElem back in the arr array of the master process
    // ...
    MPI_GatherSingleInt(&procElem, arr, 0, MPI_COMM_WORLD);

    
}

void readArray(std::vector<int> &arr,int N, char *fileName){
    arr.resize(N);
    std::ifstream file(fileName, std::ifstream::in);
    if (!file) { 
	std::cout << "ERROR: Unable to open the file " << fileName << ".\n";
	MPI_Abort(MPI_COMM_WORLD, 1);
    }
    for (int i = 0; i < N; i++) { file >> arr[i]; }
    file.close();
}

void printArray(
    int *arr,
    int N)
{
    for (int i = 0; i < N; i++) { std::cout << arr[i] << " "; }
    std::cout << std::endl;
}

int checkArrSorted(
    int *arr,
    int N)
{
    for (int i = 0; i < N - 1; i++) {
	if (arr[i] > arr[i + 1]) { return 0; }
    }
    return 1;
}

void printUsage()
{
    std::cout << "Usage: mpirun -np [num-procs/elements] ./bitonic-sort [bitonic-array-file] [sequential|parallel]\n";
    std::cout << "  Example: mpirun -np 8 ./bitonic-sort bitonic-array.txt parallel\n";
}

int main(int argc, char **argv)
{
    std::vector<int> arr;
    int N;            // The size of arr. Equally, the number of available MPI ranks.
    int procRank;     // The rank of current process

    // Initialize MPI, get the process rank and the number of processes (which is also the size of arr)
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &procRank);
    MPI_Comm_size(MPI_COMM_WORLD, &N);

    if (argc < 3) { // Error check for command line parameters
	printUsage();
	return 1;
    }
    MPI_Barrier(MPI_COMM_WORLD);

    if (procRank == 0) { // Read the bitonic array from file in the root process
	readArray(arr, N, argv[1]);
	printf("->The array before bitonic sort:\n");
	printArray(&arr[0], N);
    }

    if (strcmp(argv[2], "sequential") == 0) { // Perform a sequential sort
	if (procRank == 0) {
	    std::sort(arr.begin(), arr.end());
	}
    } else if (strcmp(argv[2], "parallel") == 0) { // Perform a parallel bitonic sort
	bitonicSort(&arr[0], N);
    } else {
	if (procRank == 0) {
	    printf("ERROR: Unknown array sort type %s.\n", argv[2]);
	}
	return 1;
    }

    // Print the array and verify if it is sorted
    if (procRank == 0) {
	printf("->The array after bitonic sort:\n");
	printArray(&arr[0], N);
	if (checkArrSorted(&arr[0], N)) {
	    printf("->The array is sorted correctly!\n");
	} else {
	    printf("->The array does not seem to be sorted!\n");
	}
    }

    MPI_Finalize();
    return 0;
}
