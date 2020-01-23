/* copie les [len] premières cases du tableau [src]
   dans [dst] en remplaçant les valeurs [old] par [new]. */
int replace_copy
  (int *src, int *dst, int len, int old, int new)
{
  for(int i = 0; i < len; i++)
    dst[i] = (src[i] == old ? new : src[i]);
}
