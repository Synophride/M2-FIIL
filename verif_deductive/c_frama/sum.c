
/*@ axiomatic sum {
  @   logic integer sum(integer n);
  @   axiom zero: sum(0) == 0;
  @   axiom ind: \forall integer i; i>=0 ==> sum(i+1) == sum(i) + i;
  @ } 
*/

/*@ requires n >= 0;
  @ requires (integer) sum(n) < 0x8000
  @ ensures \result == sum(n);
  @ assigns \nothing;

*/
int sum(int n) {
  int i = 0;
  int res = 0;
  
  /*@ loop invariant res == sum(i);
    @ loop invariant 0 <= i <= n;
    @ 
    @ 
    @ loop assigns res, i;
    @ loop variant n-i;
  */
  for(; i < n; i++){
     res += i;
  }
  return res;
}
