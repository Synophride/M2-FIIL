

/*@  
  @requires x > 0;
  @ensures \result == x * y;
  @*/
int m(int x, int y) {
    int res = 0;
  int a = 0;
  while (a < x) {
    res += y;
    a++;
  }
  return res;
}
