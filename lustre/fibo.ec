node fibo
  (input: int)
returns
  (N: int);

var
  V4_pn: int;

let
  N = (1 -> ((pre V4_pn) + V4_pn));
  V4_pn = (1 -> (pre N));
tel

