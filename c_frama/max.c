
/* the code is prettier with this global variable :-p */
int G;

/*@
  @ ensures \result == x || \result == y ;
  @ ensures \result >= x && \result >= y ;
  @*/
int max(int x, int y) {
    if (x >= y) return x;
    return y;
}
