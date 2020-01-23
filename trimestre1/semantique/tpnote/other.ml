
(** Types simples *)
module SimpleTypes = struct    
  type typ =
    | TVar of string      (** Variables de type ['a] *)
    | TInt                (** Entiers [int]          *)
    | TBool               (** Booléens [bool]        *)
    | TUnit               (** Unité [unit]           *)
    | TFun  of typ * typ  (** Fonctions [T₁ ⟶ T₂]  *)
    | TPair of typ * typ  (** Paires [T₁ * T₂]       *)
    | TRef  of typ        (** Références [ref T]     *)

  let rec (str_of_type : typ -> string) = 
    function
    | TInt -> "int"
    | TBool -> "bool"
    | TUnit -> "unit"
    | TVar(str) -> str ^ "'"
    | TFun(t1, t2)  -> "( " ^ str_of_type t1 ^ " -> " ^ str_of_type t2 ^ " )"
    | TPair(t1, t2) -> "( " ^ str_of_type t1 ^ " * " ^ str_of_type t2 ^ " )"
    | TRef(t) -> str_of_type t ^ " ref"  
  (** 
      Objectif : compléter la fonction suivante de typage d'une expression.
      Un appel [type_expression env e] doit :
      - renvoyer le type de [e] dans l'environnement [env] si [e] est bien typée
      - déclencher une exception sinon 
      todo: remettre ce commentaire à la bonne place (laquelle ?) 
  *)
end
        
(** Expressions avec annotations de types *)
module BaseAST = struct
  
  type expression =
    | Int    of int    (** Entier [n]    *)
    | Bool   of bool   (** Booléen [b]   *)
    | Unit             (** Unité [()]    *)
    | Var    of string (** Variable  [x] *)
    | App    of expression * expression
    (** Application [e₁ e₂] *)
              
    | Fun    of string * SimpleTypes.typ * expression
                       (** Fonction [fun x:T -> e] *)
    | Let    of string * expression * expression
                       (** Liaison [let x=e₁ in e₂] *)
    | Op     of string (** Opérateur *)
    | Pair   of expression * expression
                       (** Paire [(e₁, e₂)] *)
    | NewRef of expression
                       (** Création d'une référence initialisée [ref e] *)
    | Sequence of expression * expression
                       (** Séquence d'expressions [e₁; e₂] *)
    | If     of expression * expression * expression
                       (** Conditionnelle [if c then e₁ else e₂] *)
    | While  of expression * expression
                       (** Boucle [while c do e done] *)


  let rec (str_expression : expression -> string) =
    function
    | Int(i) -> string_of_int i
    | Bool(b) -> string_of_bool b
    | Unit -> "()"
    | Var(str) -> str
    | App(e1, e2) -> "( " ^ str_expression e1 ^ " ) ( " ^ str_expression e2 ^ " )"
    | Fun(str, t, e) -> "fun (" ^ str ^ " : " ^ str_of_type t ^ ") -> " ^ str_expression e
    | Let(str, e1, e2) -> "let " ^ str ^ " = " ^ str_expression e1 ^ " in \n" ^ str_expression e2
    | Op(str) -> str
    | Pair(e1, e2) -> "(" ^ str_expression e1 ^ ", " ^ str_expression e2 ^ " )"
    | NewRef(exp) -> str_expression exp ^ " ref"
    | Sequence(e1, e2) -> str_expression e1 ^ ";\n" ^ str_expression e2
    | If(eb, e1, e2) -> "if( " ^ str_expression eb
                        ^ " )\nthen " ^ str_expression e1
                        ^ "\nelse " ^ str_expression e2
    | While(eb, e) -> "While ( " ^ str_expression eb ^ " ) do \n " ^ str_expression e ^ " \n end \n" 

end

                   
(** 
    Exercice 2 : inférence des types simples.

    Ci-dessous, une syntaxe quasi-identique à [BaseAST].
    A disparu : l'annotation du paramètre formel d'une fonction par son type.

    Objectif : inférence de types.
 *)
module RawAST = struct
  open SimpleTypes
  (** Expressions *)
  type expression =
    | Int    of int    (** Entier [n]    *)
    | Bool   of bool   (** Booléen [b]   *)
    | Unit             (** Unité [()]    *)
    | Var    of string (** Variable  [x] *)
    | App    of expression * expression
    (** Application [e₁ e₂] *)
    | Fun    of string * expression
    (** Fonction [fun x -> e] *)
    | Let    of string * expression * expression
    (** Liaison [let x=e₁ in e₂] *)
    | Op     of string (** Opérateur *)
    | Pair   of expression * expression
    (** Paire [(e₁, e₂)] *)
    | Newref of expression
    (** Création d'une référence initialisée [ref e] *)
    | Sequence of expression * expression
    (** Séquence d'expressions [e₁; e₂] *)
    | If     of expression * expression * expression
    (** Conditionnelle [if c then e₁ else e₂] *)
    | While  of expression * expression
                               (** Boucle [while c do e done] *)
     
  let rec (str_expression : expression -> string) =
    function
    | Int(i) -> string_of_int i
    | Bool(b) -> string_of_bool b
    | Unit -> "()"
    | Var(str) -> str
    | App(e1, e2) -> "( " ^ str_expression e1 ^ " ) ( " ^ str_expression e2 ^ " )"
    | Fun(str, e) -> "fun (" ^ str ^ ") -> " ^ str_expression e
    | Let(str, e1, e2) -> "let " ^ str ^ " = " ^ str_expression e1 ^ " in \n" ^ str_expression e2
    | Op(str) -> str
    | Pair(e1, e2) -> "(" ^ str_expression e1 ^ ", " ^ str_expression e2 ^ " )"
    | Newref(exp) -> str_expression exp ^ " ref"
    | Sequence(e1, e2) -> str_expression e1 ^ ";\n" ^ str_expression e2
    | If(eb, e1, e2) -> "if( " ^ str_expression eb
                        ^ " )\nthen " ^ str_expression e1
                        ^ "\nelse " ^ str_expression e2
    | While(eb, e)
      -> "While ( " ^ str_expression eb ^ " ) do \n " ^ str_expression e ^ " \n end \n"

end
  
