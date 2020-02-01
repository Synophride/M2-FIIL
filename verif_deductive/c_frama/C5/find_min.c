/*@ requires \valid(a+(0..length-1));
  @ requires \initialized(a+(0..length-1)); // pê pas utile 
  @ 
  @ ensures \forall integer j; 0 <= j < length ==> a[j] <= a[\result];
  @ assigns \nothing;
*/
int find_min(int* a, int length) {
  int min, min_idx;
  min_idx = 0;
  min = a[0];

  /*@ loop invariant \forall integer j; 0 <= j < i ==> a[min_idx] <= a[j];
    @ loop invariant a[min_idx] == min;
    @ loop invariant 0 < i <= length;
    @ loop invariant min_idx >= ;

    @ loop assigns a, i, min_idx, min;
    @ loop variant length - i;

  */
  for (int i = 1; i < length; i++) {
    if (a[i] < min) {
      min_idx = i;
      min = a[i];
    }
  }
  return min_idx;
}
