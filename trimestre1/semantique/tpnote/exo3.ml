open Other


(**
   Exercice 3 : sous-typage.

   On ajoute un type optionnel [T?] désignant des valeurs de type [T]
   éventuellement non définies (contrairement au type [T] lui-même pour
   lequel la valeur est à coup sûr définie.

   On a donc la relation de sous-typage [T <: T?], de laquelle on déduit
   une relation plus générale avec les règles habituelles.
*)
module OptionTypes = struct

  (** Les types simples + un type optionnel *)
  type typ =
    | TVar of string      (** Variables de type ['a] *)
    | TInt                (** Entiers [int]          *)
    | TBool               (** Booléens [bool]        *)
    | TUnit               (** Unité [unit]           *)
    | TFun  of typ * typ  (** Fonctions [T₁ ⟶ T₂]  *)
    | TPair of typ * typ  (** Paires [T₁ * T₂]       *)
    | TRef  of typ        (** Références [ref T]     *)

    | TMaybe of typ       (** Type option [T?]       *)
    
end

(**
   Parallèlement à l'introduction du type optionnel, on modifie l'opérateur
   de création de référence, qui crée une référence non initialisée sur un
   type [T] donné en paramètre.
   L'expression [newref T] aura donc le type [ref T?].

   On crée également un opérateur ["isNull"] testant si une valeur donnée
   est indéfinie.
*)
module SubAST = struct

  type expression =
    | Int    of int    (** Entier [n]    *)
    | Bool   of bool   (** Booléen [b]   *)
    | Unit             (** Unité [()]    *)
    | Var    of string (** Variable  [x] *)
    | App    of expression * expression
                       (** Application [e₁ e₂] *)
    | Fun    of string * OptionTypes.typ * expression
                       (** Fonction [fun x:T -> e] *)
    | Let    of string * expression * expression
                       (** Liaison [let x=e₁ in e₂] *)
    | Op     of string (** Opérateur *)
    | Pair   of expression * expression
                       (** Paire [(e₁, e₂)] *)
    | NewRef of OptionTypes.typ
                       (** Création d'une référence non initialisée [newref T] *)
    | Sequence of expression * expression
                       (** Séquence d'expressions [e₁; e₂] *)
    | If     of expression * expression * expression
                       (** Conditionnelle [if c then e₁ else e₂] *)
    | While  of expression * expression
                       (** Boucle [while c do e done] *)

end

(**
   Vérification de types avec sous-typage.

   Ajouter du sous-typage au vérificateur de types simples, avec la règle
   algorithmique standard : le paramètre effectif d'un appel de fonction peut
   être un sous-type du type du paramètre formel.

   On ajoutera les particularités suivantes :
   - Tout opérateur travaillant sur des valeurs concrètes nécessitera des
     opérandes dont le type *n'est pas* un type optionnel.
   - Dans une expression de la forme [if isNull a then e₁ else e₂] avec [a] de
     type [T?], on pourra donner à [a] le type [T] dans la branche [e₂].
*)
module SubTypeChecker = struct
  
  
end
