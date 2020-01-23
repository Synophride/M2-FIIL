/* put *a into *b, and reset *a to 0. */
/*@
  @ requires \valid(a) ;
  @ requires \valid(b) ;
  @ requires \separated(a, b) ; 
  @ ensures *a == 0 && *b == \old(*a) ;
  @*/
void reset_swap(int *a, int *b) {
  int tmp = *a;
  *a = 0;
  *b = tmp;
}

int main(void) {
  int x = 1, y = 2;

  reset_swap(&x, &y);
  /*@ assert x == 0 && y == 1; */

  reset_swap(&y, &y);
  /*@ assert y == 0; */

  return 0;
}
