(**
   TP : expliciter le flot de contrôle par des continuations dans
   des petits parcours d'arbres.
*)

(* Arbres binaires *)
type 'a tree =
  | Leaf 
  | Node of 'a * 'a tree * 'a tree

(**
   Fonction simple de parcours d'arbre en profondeur, qui produit une
   liste des étiquettes des nœuds rencontrés.
*)
let rec dfs t =
  match t with
    | Leaf -> []
    | Node(a, t, u) -> a :: (dfs t) @ (dfs u)

(**
   Exemple de transformation de [dfs] en style par passage de continuations.
   Remarquez que la fonction obtenue est récursive terminale.
*)
let tailrec_dfs t =
  (* La fonction [cps_dfs] prend en paramètre supplémentaire une
     continuation, qui à partir d'une liste d'élément de types ['a]
     produit un résultat de type ['b].
     On force ce changement de type pour que le typeur de Caml nous
     prévienne en cas d'oubli d'application d'une continuation. *)
  let rec cps_dfs (t: 'a tree) (k: 'a list -> 'b) : 'b =
    match t with
      (* Un arbre réduit à une feuille donne une liste vide d'étiquettes.
	 On applique la continuation à ce résultat. *)
      | Leaf -> k []
      | Node(a, t, u) ->
	(* On produit d'abord la liste d'étiquettes correspondant au sous-
	   arbre [t]. Le résultat est noté [dfst] et est transmis à la
	   continuation, qui commence par produire la liste d'étiquettes
	   correspondant au sous-arbre [u]. Le résultat est noté [dfsu]
	   est transmis à la continuation en charge de la dernière étape :
	   construire la liste résultat [a :: dfst @ dfsu] et la transmettre
	   à la continuation globale [k]. *)
	cps_dfs t (fun dfst ->
	  cps_dfs u (fun dfsu ->
	    k (a :: dfst @ dfsu)
	  )
	)
  in
  cps_dfs t (List.iter print_int)


(**
   Question 1 :
   - Modifier la fonction [cps_dfs] pour faire le parcours de droite à gauche
     (mais toujours en profondeur).

   Question 1bis pour creuser un peu plus :
   - Modifier la fonction [cps_dfs] pour faire un parcours en largeur.
     Indication : commencer par coder le parcours en largeur en style direct.
*)

let tailrec_dfs_droite t =
  (* La fonction [cps_dfs] prend en paramètre supplémentaire une
     continuation, qui à partir d'une liste d'élément de types ['a]
     produit un résultat de type ['b].
     On force ce changement de type pour que le typeur de Caml nous
     prévienne en cas d'oubli d'application d'une continuation. *)
  let rec cps_dfs (t: 'a tree) (k: 'a list -> 'b) : 'b =
    match t with
      (* Un arbre réduit à une feuille donne une liste vide d'étiquettes.
	 On applique la continuation à ce résultat. *)
      | Leaf -> k []
      | Node(a, t, u) ->
	(* On produit d'abord la liste d'étiquettes correspondant au sous-
	   arbre [t]. Le résultat est noté [dfst] et est transmis à la
	   continuation, qui commence par produire la liste d'étiquettes
	   correspondant au sous-arbre [u]. Le résultat est noté [dfsu]
	   est transmis à la continuation en charge de la dernière étape :
	   construire la liste résultat [a :: dfst @ dfsu] et la transmettre
	   à la continuation globale [k]. *)
	 cps_dfs u (fun dfst ->
	    cps_dfs t (fun dfsu ->
	        k (a :: dfst @ dfsu)
	      )
	  )
  in
  cps_dfs t (List.iter print_int)
  
let t = Node(1,
             Node(2,
                  Leaf,
                  Leaf),
             Node(5,
                  Node(6,
                       Node(7,
                            Leaf,
                            Leaf),
                       Leaf),
                  Node(8, Leaf, Leaf))) ;;

let c () = Printf.printf "%s" (String.make 2 '\n');;
tailrec_dfs t;
c();
tailrec_dfs_droite t;
c();
    
(** 
    Question 2 : écrire en CPS une fonction
    [prefix: 'a -> 'a tree -> 'a list] 
     qui affiche les éléments rencontrés avant le premier paramètre.
 *)

let prefixe_direct elt l = 
  let rec prefix_aux elt acc=
    function
    | Leaf -> (acc)
    | Node(s, sag, sad)
      -> if s = elt
         then (true, s::acc)
         else let (b, acc_) = prefix_aux elt sag in
              if b
              then true, acc_
              else let b, acc_ = prefix_aux elt sad in
                   if b
                   then true, acc_ 
                   else false, acc
  in prefix_aux 
    

(**
   Question 3 : écrire en CPS une fonction
     [find: 'a -> 'a tree -> 'a list option]
   telle que, si [a] existe dans [t], alors [find a t] renvoie la liste des
   nœuds sur un chemin de la racine vers [a].
*)

(**
   Cadeau : voici une fonction qui le fait en style direct, en utilisant
   des exceptions.
*)
exception Found of int list
let find a t =
  let rec find t = match t with
    | Leaf -> ()
    | Node(b, g, d) ->
      if a = b
      then raise (Found [])
      else
	try find g; find d
	with Found(path) -> raise (Found(b::path))
  in
  try find t; None
  with Found(path) -> Some path

(**
   Technique : CPS à deux canons.
   La fonction auxiliaire avec continuations prend deux continuations en
   paramètres :
   - la continuation [kf] est à utiliser si l'élément cherché a été trouvé
   - la continuation [knf] est à utiliser si l'élément n'a pas été trouvé
*)
let double_barrel_cps_find a t =
  let rec find t kf knf = match t with
    | Leaf -> ()(* À compléter *)
    | Node(b, g, d) -> ()(* À compléter *)
  in
  find t (* À compléter *)
