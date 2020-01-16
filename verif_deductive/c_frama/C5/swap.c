
/*@
  @ requires \valid(a);
  @ requires \valid(b);

  @ ensures *a == \old(*b) && *b == \old(*a);
  @*/
void swap(int *a, int *b) {
  int tmp = *a;
  *a = *b;
  *b = tmp;
}
