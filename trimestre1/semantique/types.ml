(**
   TP Inférence de types
   ---------------------

   L'objectif est de réaliser un programme indiquant si un lambda-terme
   donné peut être typé. Dans un premier temps on considère uniquement les
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
   types simples, et en deuxième partie on ajoute une forme restreinte de
=======
   types simples, et en deuxième partie on ajoute une forme restreint de
>>>>>>> ccc7bd4... tp sémantique
=======
   types simples, et en deuxième partie on ajoute une forme restreinte de
>>>>>>> 701b775... début du tp sur les types
=======
   types simples, et en deuxième partie on ajoute une forme restreinte de
>>>>>>> e3c8d49... début du tp sur les types
   polymorphisme. 
*)


(**
   Partie I. Inférence de types par résolution de contraintes
   ----------------------------------------------------------
   
   On oublie les annotations de types, et on essaye simplement de s'assurer
   qu'il existe une série d'annotations rendant le programme bien typé.

   Pour effectuer cette analyse, on procède en deux étapes :
   - construction d'un ensemble de contraintes d'égalité entre types
   - résolution des contraintes par unification
   Pour exprimer dans les contraintes des types pas encore connus, on ajoute
   des variables de type à la syntaxe des types.
*)
  
module LambdaInfer = struct

  type typ =
    | TypVar of string
    | TypArrow of typ * typ

  type term =
    | Var of string
    | Abs of string * term
    | App of term * term

  (* Une contrainte est une paire de types (ty₁, ty₂) à interpréter comme
     l'égalité ty₁ = ty₂ à satisfaire. Le module CSet manipule des ensembles
     de telles contraintes. *)
  type type_constraint = typ * typ
  module CSet = Set.Make(struct type t=type_constraint let compare=compare end)

  let cset_map f cset =
    CSet.fold (fun c cset -> CSet.add c cset) cset CSet.empty

  (* Environnement de typage *)
  module Env = Map.Make(String)
  type type_env = typ Env.t

  (* Fonction auxiliaire de création d'une nouvelle variable de type *)
  let fresh_type_variable =
    let cpt = ref 0 in
    fun () -> incr cpt; Printf.sprintf "?%d" !cpt

  (**
     Génération de contraintes :

     - Dans le cas d'une variable, pas de nouvelle contrainte (on renvoie le
       type enregistré dans l'environnement).

     - Dans le cas d'une abstraction on crée une nouvelle variable de type pour
       le type du paramètre, et on enrichit l'environnement pour l'analyse du
       corps de la fonction. 

     - Dans le cas d'une application on introduit une contrainte : le type
       du membre gauche doit être égal à un type de fonction, dont le type
       d'entrée est le type du membre droit. On crée une nouvelle variable de
       type pour le type du résultat.
  *)
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 701b775... début du tp sur les types
=======
>>>>>>> e3c8d49... début du tp sur les types
  let generate_constraints (t : term) =
    (* contrainte : type * type *) 
    let (constraints : CSet.t ref)  = ref CSet.empty in
    (* renvoie le type de la sous-expression *)
    let rec gen (t : term) (env : type_env) : typ =
<<<<<<< HEAD
<<<<<<< HEAD
      match t with
      | Var(str) -> (Env.find str env)
      | Abs(str, term) -> let new_vartype = fresh_type_variable () in
                          let type_terme = gen term (Env.add str (TypVar(new_vartype)) env) in
                          TypArrow(TypVar(new_vartype) , type_terme)
      | App(term1, term2) -> let t1, t2 = gen term1 env, gen term2 env in
                             let type_out = TypVar(fresh_type_variable ()) in
                             begin
                               constraints := CSet.add (TypArrow(t2, type_out), t1) (!constraints);
                               type_out
                             end
    in
    let _ =  gen t Env.empty in
    !constraints
  exception EugneuMalType
=======
  let generate_constraints t =
    let constraints = ref CSet.empty in 
    let rec gen t env =
=======
>>>>>>> 701b775... début du tp sur les types
=======
>>>>>>> e3c8d49... début du tp sur les types
      match t with
      | Var(str) -> (Env.find str env)
      | Abs(str, term) -> let new_vartype = fresh_type_variable () in
                          let type_terme = gen term (Env.add str (TypVar(new_vartype)) env) in
                          TypArrow(TypVar(new_vartype) , type_terme)
      | App(term1, term2) -> let t1, t2 = gen term1 env, gen term2 env in
                             let type_out = TypVar(fresh_type_variable ()) in
                             begin
                               constraints := CSet.add (TypArrow(t2, type_out), t1) (!constraints);
                               type_out
                             end
    in
    let _ =  gen t Env.empty in
    !constraints
<<<<<<< HEAD
<<<<<<< HEAD
    
>>>>>>> ccc7bd4... tp sémantique
=======
  exception EugneuMalType
>>>>>>> 701b775... début du tp sur les types
=======
  exception EugneuMalType
>>>>>>> e3c8d49... début du tp sur les types
  (**
     Résolution des contraintes :
     
     - Une contrainte identifiant une variable de type X et un type ty est :
       * insoluble si X apparaît dans ty
       * résolue si X n'apparaît pas dans ty
       Dans ce dernier cas on peut propager l'équation en remplaçant X par ty
       dans toutes les autres contraintes.

     - Une contrainte entre deux types TypArrow peut être décomposée en deux
       contraintes.
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 701b775... début du tp sur les types
=======
>>>>>>> e3c8d49... début du tp sur les types
   *)
  
  let resolve_constraints (cset : CSet.t) =
    let remaining_const = ref cset in
    (* dit si la variable de type vartyp est contenue dans t2*)
    let rec contains str_vartyp =
<<<<<<< HEAD
<<<<<<< HEAD
      function
      | TypVar(str_t) -> str_t = str_vartyp
      | TypArrow(t1, t2) -> contains str_vartyp t1 or contains str_vartyp t2
    in
    let remplacement str_vartype new_type =
      let rec remp =
        function
        | TypVar(s2) -> if s2 = str_vartype
                        then new_type
                        else TypVar(s2)
        | TypArrow(t1, t2) -> TypArrow(remp t1, remp t2)
      in
      remaining_const := cset_map (fun (a,b) -> (remp a, remp b)) (!remaining_const)
    in
    
    let rec resolution_contraintes () = 
      match CSet.min_elt_opt (!remaining_const) with
      | None -> true
      | Some((t1, t2)) ->
         begin
           begin
             match t1, t2 with
             | TypArrow(t1a, t1b), TypArrow(t2a, t2b)
               -> (
               remaining_const := CSet.add (t1a, t2a) (!remaining_const);
               remaining_const := CSet.add (t1b, t2b) (!remaining_const);

             )
             | TypVar(str), TypArrow(ta, tb)
               | TypArrow(ta, tb), TypVar(str)
               -> if contains str ta or contains str tb
                  then
                    raise EugneuMalType
                  else
                    remplacement str (TypArrow(ta, tb))
             | TypVar(s1), TypVar(s2)
               -> remplacement s1 (TypVar(s2))
           end;
         remaining_const := CSet.remove (t1, t2) (!remaining_const)   
         end;
         resolution_contraintes ()
    in
      let _ = resolution_contraintes () in
      true
              
=======
   *)      
  let resolve_constraints cset =
    
    let remplacement_contraintes tvarname type_remplacant cset = 
      let rec remplacement_dans_type  = function
        | TypVar(s) -> if s = tvarname
                       then type_remplacant
                       else TypVar(s)
        | TypArrow(t1, t2) -> TypArrow(remplacement_dans_type t1, remplacement_dans_type t2)
      in
      CSet.map
        (fun (t1, t2) -> (remplacement_dans_type t1, remplacement_dans_type t2))
        cset
    in
    let rec contains nom_var =
=======
>>>>>>> 701b775... début du tp sur les types
=======
>>>>>>> e3c8d49... début du tp sur les types
      function
      | TypVar(str_t) -> str_t = str_vartyp
      | TypArrow(t1, t2) -> contains str_vartyp t1 or contains str_vartyp t2
    in
    let remplacement str_vartype new_type =
      let rec remp =
        function
        | TypVar(s2) -> if s2 = str_vartype
                        then new_type
                        else TypVar(s2)
        | TypArrow(t1, t2) -> TypArrow(remp t1, remp t2)
      in
      remaining_const := cset_map (fun (a,b) -> (remp a, remp b)) (!remaining_const)
    in
    
    let rec resolution_contraintes () = 
      match CSet.min_elt_opt (!remaining_const) with
      | None -> true
      | Some((t1, t2)) ->
         begin
<<<<<<< HEAD
<<<<<<< HEAD
           match t1, t2 with
           | TypVar(x1), TypVar(x2)
             -> 
              let new_cset = remplacement_contrainte x1 t2 cset' in
              resc new_cset
           | TypVar(x1), TypArrow(ta1, ta2) | TypArrow(ta1, ta2, TypVar(x1))
             -> if contains x1 ta1 or contains x1 ta2
                then failwith ""
                else
                  let new_cset = remplacement_contraintes x1 (TypArrow(ta1, ta2)) cset' in
                  resc new_cset
           | TypArrow(t1a, t1b), TypArrow(t2a, t2b)
             -> let cset1 = CSet.add (t1a, t2a) cset' in
                let cset2 = CSet.add (t1b, t2b) cset1 in
                resc cset2
         end
           (* gestion des exceptions *)
    in resc cset
    
          
      
                     
>>>>>>> ccc7bd4... tp sémantique
=======
=======
>>>>>>> e3c8d49... début du tp sur les types
           begin
             match t1, t2 with
             | TypArrow(t1a, t1b), TypArrow(t2a, t2b)
               -> (
               remaining_const := CSet.add (t1a, t2a) (!remaining_const);
               remaining_const := CSet.add (t1b, t2b) (!remaining_const);

             )
             | TypVar(str), TypArrow(ta, tb)
               | TypArrow(ta, tb), TypVar(str)
               -> if contains str ta or contains str tb
                  then
                    raise EugneuMalType
                  else
                    remplacement str (TypArrow(ta, tb))
             | TypVar(s1), TypVar(s2)
               -> remplacement s1 (TypVar(s2))
           end;
         remaining_const := CSet.remove (t1, t2) (!remaining_const)   
         end;
         resolution_contraintes ()
    in
      let _ = resolution_contraintes () in
      true
              
<<<<<<< HEAD
>>>>>>> 701b775... début du tp sur les types
=======
>>>>>>> e3c8d49... début du tp sur les types
    
  let typecheck_expression t = resolve_constraints (generate_constraints t)

  let idB = Abs("x", Var "x")
  let doubleB = Abs("f", Abs("x", App(Var "f", App(Var "f", Var "x"))))
  let autoApply = Abs("x", App(Var "x", Var "x"))
        
  let _ = typecheck_expression idB
  let _ = typecheck_expression doubleB
  let _ = typecheck_expression (App(doubleB, idB))
  let _ = typecheck_expression (App(idB, doubleB))
  let _ = typecheck_expression (App(idB, idB))
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
  let _ = print_string "Exemples LambdaInfer ok\n";
          (*    let _ = typecheck_expression autoApply *)
=======
  let _ = print_string "Exemples LambdaInfer ok\n"
  (* let _ = typecheck_expression autoApply *)
    
    
>>>>>>> ccc7bd4... tp sémantique
=======
  let _ = print_string "Exemples LambdaInfer ok\n";
          (*    let _ = typecheck_expression autoApply *)
>>>>>>> 701b775... début du tp sur les types
=======
  let _ = print_string "Exemples LambdaInfer ok\n";
          (*    let _ = typecheck_expression autoApply *)
>>>>>>> e3c8d49... début du tp sur les types
end

  
(**
   Partie II. Inférence de types avec [let]-polymorphisme
   ------------------------------------------------------

   Version simple : on n'exprime pas explicitement le polymorphisme mais on
   duplique les termes liés par [let] pour typer chaque version indépendamment.
*)

module LambdaLetPoly = struct
  
  type typ =
    | TypVar of string
    | TypArrow of typ * typ

  type term =
    | Var of string
    | Abs of string * term
    | App of term * term
    (* Ce qui change *)
    | Let of string * term * term

  type type_constraint = typ * typ
  module CSet = Set.Make(struct type t=type_constraint let compare=compare end)

  let cset_map f cset =
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    CSet.fold (fun c cset -> CSet.add (f c) cset) cset CSet.empty
=======
    CSet.fold (fun c cset -> CSet.add c cset) cset CSet.empty
>>>>>>> ccc7bd4... tp sémantique
=======
    CSet.fold (fun c cset -> CSet.add (f c) cset) cset CSet.empty
>>>>>>> 701b775... début du tp sur les types
=======
    CSet.fold (fun c cset -> CSet.add (f c) cset) cset CSet.empty
>>>>>>> e3c8d49... début du tp sur les types
    
  module Env = Map.Make(String)
  type type_env = typ Env.t

  (**
     Substitution de termes : [term_substitution t x u] substitue par [u] les
     occurrences de [x] dans [t] en évitant les captures.
     
     Attention, cela signifie qu'il faut d'abord vérifier qu'il n'y a pas de
     capture, ou renommer certaines variable liées.
  *)
  let term_substitution t x u =
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    ()
=======
    failwith "not implemented"
    
>>>>>>> ccc7bd4... tp sémantique
=======
    ()
>>>>>>> 701b775... début du tp sur les types
=======
    ()
>>>>>>> e3c8d49... début du tp sur les types
  (**
     La génération est la même qu'avant.
     Le nouveau cas [Let(x, t1, t2)] doit donner lieu à la substitution
     [term_substitution t2 x t1] avant poursuite de la génération.
  *)
  let generate_constraints t =
    failwith "not implemented"
    
  (** 
      Résolution : identique.
  *)
  let resolve_constraints cset =
    failwith "not implemented"
    
  let typecheck_expression t = resolve_constraints (generate_constraints t)


  let idB = Abs("x", Var "x")
  let doubleB = Abs("f", Abs("x", App(Var "f", App(Var "f", Var "x"))))        
  let doubleIdB = Let("d", doubleB, App(Var "d", idB))
  let ddoubleIdB = Let("d", doubleB, App(App(Var "d", Var "d"), idB))
  let autoApply = Abs("x", App(Var "x", Var "x"))

  (* Pour du polymorphisme en actes ici il faut une abstraction non annotée,
     ce qu'on n'a pas autorisé tant qu'il n'y a pas d'inférence. *)
    
  let _ = typecheck_expression idB
  let _ = typecheck_expression doubleB
  let _ = typecheck_expression (App(doubleB, idB))
  let _ = typecheck_expression (App(idB, doubleB))
  let _ = typecheck_expression doubleIdB
  let _ = typecheck_expression ddoubleIdB
  let _ = print_string "Exemples LambdaLetPoly ok\n"
  (* let _ = typecheck_expression autoApply *)

end

(** 
    Partie III. Extensions
    ----------------------

    Étendre la génération et la résolution de contraintes avec un calcul
    étendu des constructions suivantes :
    - type de base (constantes entières et arithmétique)
    - type produit (avec paires et projections)
    - type somme (avec filtrage et injections)

    Ajouter de la reconstruction : renvoyer un type aussi général que possible
    pour un terme bien typé.
*)
