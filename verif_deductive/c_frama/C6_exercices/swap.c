/* swap the contents of [a] and [b]. */
void swap(int *a, int *b) {
  int tmp = *a;
  *a = *b;
  *b = tmp;
}
