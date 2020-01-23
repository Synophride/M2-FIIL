int max_elt(int *t, int len) {
  int max = 0;
  if (len == 0) return 0;
  for(int i = 0; i < len; i++)
    if (a[max] < a[i])
      max = i;
  return max;
}

/* Retourne la valeur maximale contenue dans 
   le tableau [t] de longueur [len].
   Retourne 0 si le tableau est vide. */
int max_seq(int *t, int len) {
  return t[max_elt(t, len)];
}
