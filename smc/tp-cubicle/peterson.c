#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>

#define True 1
#define False 0
#define P1 0
#define P2 1

volatile int Turn;
volatile int Want[2];
volatile int Other[2];

volatile int cpt;

void init () {
  Turn = P1;
  Want[P1] = False;
  Want[P2] = False;
  Other[P1] = P2;
  Other[P2] = P1;
}


void process (int i) {
  int iter = 0;
  while (True) {
  L1:
    Want[i] = True;
  L2:
    Turn = Other[i];
  L3:
    while ( Want[Other[i]] && !(Turn == i) ) { ; }
  L4_crit:
    // section critique
    cpt = cpt + 1;
  L5:
    Want[i] = False;
  L6:
    // section restante
    iter++;
    if ( 1000000 == iter ) { return ; }
  }
}

void * process_th (void * arg) {
  int i =  *((int *)arg);
  process(i);
  return NULL;
}


int main () {
  pthread_t tid[2];
  init();
  int *arg1 = (int *)malloc(sizeof(int));
  *arg1 = P1;
  int *arg2 = (int *)malloc(sizeof(int));
  *arg2 = P2;
  pthread_create(tid, NULL, process_th, arg1);
  pthread_create(tid+1, NULL, process_th, arg2);
  pthread_join(tid[0], NULL);
  pthread_join(tid[1], NULL);
  printf("cpt = %d\n", cpt);
  return 0;
}
