/* put 0 in each cell of [t] with an even index
   and 1 in the other cells.
   [n] is the length of [t]. */
void parity_reset(int *t, int n) {
  int i;
  for(i = 0; i < n; i++)
    t[i] = i % 2;
}
