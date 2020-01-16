/*@ axiomatic abs {
  @   logic int abs(int x);
  @   axiom pos: \forall int x; x >= 0 ==> abs(x) == x;
  @   axiom neg: \forall int x; x < 0 ==> abs(x) == -x;
  @ }
*/
int abs(int x);

//@ ensures (\result == x || \result == y) && (\result >= x && \result >= y);
int max(int x, int y);

/*@ 
  @ ensures \result >= 0;
*/
int max_abs(int x, int y) {
  x = abs(x);
  y = abs(y);
  return max(x, y);
}
