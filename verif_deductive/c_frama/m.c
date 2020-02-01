/*@  
  @ requires x > 0;
  @ ensures \result == x * y;
  @*/
int m(int x, int y) {
  int res = 0;
  int a = 0;

  /*@ loop invariant res == y * a;
    @ loop invariant a<=x;
    @ loop assigns res, a;
    @ loop variant x - a;
  */
  while (a < x) {
    res += y;
    a++;
  }
  return res;
}
