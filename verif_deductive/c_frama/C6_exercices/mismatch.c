/* Retourne un indice auquel les tableaux [a] et [b]
   ne contiennent pas la même valeur.
   Si les deux tableaux sont égaux, retourne [len]. */
int mismatch(int *a, int *b, int len) {
  for(int i = 0; i < len; i++)
    if (a[i] != b[i])
      return  i;
  return len;
}
