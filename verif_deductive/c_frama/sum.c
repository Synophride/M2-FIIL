
/*@ axiomatic sum{
  @ logic integer sum(integer n);
  @ axiom zero: sum(0) == 0;
  @ axiom ind: \forall integer i; i>=0 ==> sum(i+1) == sum(i) + i;
  @ } 
*/

/*@ requires n >= 0;
  @ ensures \result == sum(n-1);
  @ assigns \nothing;
*/
int sum(int n) {
  int i = 0;
  int res = 0;

  /*@ loop invariant res == sum(i);
    @ loop invariant 0 <= i <= n;
    @ loop variant n-i;
    
  */
  for(int i = 0; i < n; i++)
     res += i;
  return res;
}
