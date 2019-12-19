/* returns the absolute value of [x]. */



/*@ 
  @ ensures \result >= 0;
  @ ensures \result == x || \result == -x; 
  @*/
int abs(int x) {
  if (x >= 0) return x;
  return -x;
}
