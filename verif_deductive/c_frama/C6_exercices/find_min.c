/* returns the index of the minimal element 
   of the given array [a] of size [length] */
int find_min(int* a, int length) {
  int min, min_idx;
  min_idx = 0;
  min = a[0];
  /*@ assert min_idx == 0; */
  for (int i = 1; i < length; i++) {
    if (a[i] < min) {
      min_idx = i;
      min = a[i];
    }
  }
  return min_idx;
}
