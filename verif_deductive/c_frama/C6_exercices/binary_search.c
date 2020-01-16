/* takes as input a sorted array a, its length, 
   and a value key to search,
   returns the index of a cell which contains key,
   returns -1 iff key is not present in the array 
*/
int binary_search(int* a, int length, int key) {
  int low = 0, high = length - 1;
  while (low<=high) {
    int mid = (low+high)/2;
    if (a[mid] == key) return mid;
    if (a[mid] < key) { low = mid+1; }
    else { high = mid - 1; }
  }
  return -1;
}
