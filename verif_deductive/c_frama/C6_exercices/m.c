
// Que fait cette fonction ?
// La spécifier avec un contrat en ACSL
// La prouver avec WP
//
// facultatif et très compliqué, après l'exo 6 : 
// prouver l'absence d'erreur à l'exécution
int m(int x, int y) {
  int res = 0;
  int a = 0;
  while (a < x) {
    res += y;
    a++;
  }
  return res;
}
