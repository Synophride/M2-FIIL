/*@ requires \valid(a+(0..length-1));
  @
  @ ensures \result == -1 || 0 <= \result < length;
  @ ensures \result == -1 
  @           ==> \forall integer i; 0 <= i < length 
  @                 ==> a[i] != key;
  @ ensures \result != -1 ==> a[\result] == key;
  @
  @ assigns \nothing;
*/
int binary_search(int* a, int length, int key) {
  int low = 0, high = length - 1;

  /*@ loop invariant low <= high+1;
    @ loop invariant 
    @    \forall integer b; (0<=b < low ==> a[b] < key) && (high < b <length ==> a[b] > key) ;
    @ loop assigns low, high;
    @ loop variant high - low;
   */
  while (low<=high) {
    int mid = (low+high)/2;
    if (a[mid] == key) return mid;
    if (a[mid] < key) { low = mid+1; }
    else { high = mid - 1; }
  }
  return -1;
}
