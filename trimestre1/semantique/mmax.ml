

(**
   Référence biblio :
   'Why functional programming matters' (John Hughes)

   Lien vers le fichier :
   http://www.lri.fr/~blsk/minmax.ml

   Cet exercice est organisé autour de l'algorithme MinMax,
   qui a pour but de décider d'un coup à jouer dans un jeu
   à deux joueurs.

   Idée principale de l'algorithme : explorer l'arbre des suites de parties
   possibles jusqu'à une profondeur de [k] coups, et attribuer une valeur à
   chaque position du jeu explorée de la manière suivante :
   
   - les feuilles de l'arbre, c'est-à-dire les positions limites
     de l'exploration, ont une valeur donnée par une estimation grossière
     de la qualité de la position elle-même (par exemple : la différence
     entre les scores du joueur et de l'adversaire),

   - les positions internes de l'arbres ont une valeur calculée à partir
     des valeurs des positions suivantes, en supposant que le joueur et
     l'adversaire jouent 'bien' : lors d'un coup du joueur on garde la
     valeur maximisant son gain, et lors d'un coup de l'adversaire, on
     garde la valeur minimisant le gain du joueur (parmi les valeurs
     préalablement estimées pour les positions suivantes).

   On propose d'abord un code dit 'spaghetti', incluant toute l'exploration
   dans une unique fonction. L'objectif de l'exercice est d'écrire une
   nouvelle version au code plus modulaire, puis d'ajouter de l'évaluation
   paresseuse à cette deuxième version modulaire afin de la rendre efficace.
*)

(* Signature décrivant un jeu. *)
module type GameSig = sig
  (* Le type [position] désigne une configuration du jeu. *)
  type position
  (* Étant donnée une position du jeu la fonction [moves] donne la liste
     des coups possibles, sous la forme des nouvelles positions résultant de
     ces coups. *)
  val moves: position -> position list
  (* La fonction [eval] donne une évaluation grossière de la qualité d'une
     position donnée, du point de vue du joueur. *)
  val eval : position -> int
end

(* Un module instanciant GameSig avec le jeu de Nim. *)
module Nim : GameSig = struct
  (* Position du jeu : nombre d'allumettes présentes, et booléen valant
     [true] si c'est au tour du joueur de choisir le prochain coup. *)
  type position = int * bool
  (* Jouer un coup consiste à retirer 1, 2 ou 3 allumettes. *)
  let moves (p, b) =
    let nb = not b in
    if p>=3
    then [ p-1,nb ; p-2,nb ; p-3,nb ]
    else if p=2
    then [ 0,nb ; 1,nb ]
    else if p=1
    then [ 0,nb ]
    else []
  (* Une position est gagnante pour le joueur si
     - l'adversaire est le prochain à jouer et les allumettes sont en
       nombre divisible par 4
     - le joueur est le prochain à jouer et les allumettes sont en
       nombre non divisible par 4
  *)
  let eval (p, b) =
    if b
    then if p mod 4 = 0 then 0 else 1
    else if p mod 4 = 0 then 1 else 0
end

module ListExtraOps = struct
  (* Équivalent des fonctions [fold] habituelles, mais suppose que
     la liste est non vide et prend le premier élément comme valeur
     initiale de l'accumulateur. *)
  let lfold f l =
    let rec fold f acc = function
      | []   -> acc
      | a::l -> fold f (f acc a) l
    in
    match l with
      | []   -> failwith "Empty list"
      | a::l -> fold f a l

  (* Renvoie le maximum/minimum d'une liste non vide. *)
  let lmax = lfold max
  let lmin = lfold min
end

(** 
    Première version de l'exploration
    ---------------------------------
    Tout l'algorithme MinMax dans deux fonctions mutuellement récursives  :
    une maximisante pour les coups du joueur, une minimisante pour les coups
    de l'adversaire.
    Accessoirement : ce que vous trouverez dans tous les blogs sur le sujet.
*)
module SpaghettiMinMax (G : GameSig) = struct
  (* [evaluate_max k p] calcule la valeur de la position [p] en
     explorant les [k] prochains coups possibles.
     Cette fonction suppose que c'est au tour du joueur de choisir un coup.
  *)
  let rec evaluate_max k p =
    (* Si [k] est nul, utiliser la fonction d'évaluation grossière. *)
    if k <= 0 then G.eval p
    else
      let next_list = G.moves p in
      if next_list = []
      (* S'il n'y a aucun coup à jouer à partir de [p], utiliser également
	 le fonction d'évaluation grossière. *)
      then G.eval p
      (* Évalue les valeurs des positions suivantes, en explorant [k-1] coups
	 et en utilisant la fonction [evaluate_min] qui suppose que c'est à
	 l'adversaire de jouer. Puis prendre le maximum de cette liste. *)
      else let next_values = List.map (evaluate_min (k-1)) next_list in
	   ListExtraOps.lmax next_values

  and evaluate_min k p =
    (* Même chose, mais du point de vue de l'adversaire, donc en inversant
       les recours à [min] et [max]. *)
    if k <= 0 then G.eval p
    else 
      let next_list = G.moves p in
      if next_list = []
      then G.eval p
      (* Au passage, observez l'utilisation de l'opérateur de composition |>
	 qui prend le résultat de l'expression de gauche et le donne en
	 argument à l'expression de droite. *)
      else List.map (evaluate_max (k-1)) next_list |> ListExtraOps.lmin

  (* Par défaut, on considère que c'est le tour du joueur. *)
  let evaluate k p = evaluate_max k p
end


(**
   Exercice 1
   ----------
   Algorithme MinMax, version modulaire.
   
   Découpage en quatre étapes :
     1. Construire l'arbre des parties possibles
     2. Couper à profondeur [k]
     3. Affecter à chaque position sa valeur grossière
     4. Calculer les maximisations/minisations

   L'exécution de cette version pourra être très longue si l'arbre des parties
   possibles est profond, mais la technique est conceptuellement plus propre.
*)
module MinMax (G : GameSig) = struct

  (* Type de l'arbre des parties possibles : un nœud est formé par la position
     courante et une liste d'arbre correspondant aux suites possibles après
     chaque coup légal dans la position courante. *)
  type 'a tree = Node of 'a * ('a tree list)
                      
  (* Étape 1 : Construire l'arbre des parties possibles
       mk_tree: ('a -> 'a list) -> 'a -> 'a tree
     Cette fonction construit l'arbre à partir d'une fonction [next] qui
     calcule les fils d'une position [pos] donnée.
     Le principe sera d'instancier [next] par [G.moves].
  *)
  let rec mk_tree next pos =
    Node(pos, List.map (mk_tree next) (next pos))

    
  (* Étape 2 : Couper l'arbre [t] à profondeur [k]
       prune: int -> 'a tree -> 'a tree
  *)
  let rec prune k t =
    match t with
    | Node(pos, fils)
      -> if k = 0 then Node(pos, [])
         else Node(pos, List.map (prune (k-1)) fils)

  (* Étape 3 : Affecter à chaque position sa valeur grossière
       map_tree: ('a -> 'b) -> 'a tree -> 'b tree
     La fonction [map_tree] construit un nouvel arbre dont les étiquettes
     sont données par une fonction [f].
     Le principe sera d'instancer [f] par [G.eval].
  *)
  let rec map_tree f t =
    let Node(deep, purple) = t in
    Node(f deep, List.map (map_tree f) purple) 

  (* Étape 4 : Calculer les maximisations/minisations dans l'arbre [t]
       maximize: int tree -> int
       minimize: int tree -> int
  *)
  let rec maximize t =
    let (value, sous_arbre) = t in
    if List.len sous_arbre > 0
    then
      List.fold_left (fun acc elt -> let value = minimize elt in if value > acc then value else acc) min_int
    else
      value
  and minimize t =
        List.fold_left (fun acc elt -> let value = maximize elt in if value < acc then value else acc) max_int
        
    
  (* Fonction finale [evaluate] : appelle les quatre fonctions précédentes
     dans l'ordre, en instanciant les fonctions spécifiques au jeu par
     [G.moves] et [G.eval] *)
  let evaluate k p =
    mk_tree G.moves p |> prune k |> map_tree G.eval |> maximize

(* Note.
   L'enchaînement de compositions |> est équivalent au code suivant :
   let tree  = mk_tree G.moves p in
   let ptree = prune k tree in
   let etree = map_tree G.eval ptree in
   maximize etree

   On pourrait également l'écrire comme cela :
   maximize (map_tree G.eval (prune k (mk_tree G.moves p)))
*)	

end

(**
   Exercice 2
   ----------
   Création d'un module permettant d'introduire de l'évaluation paresseuse
   dans Caml.

   Ce qu'on veut représenter : un calcul suspendu, qu'on n'effectue pas tout
   de suite, et dont on mémorisera le résultat une fois qu'il sera connu.
*)
  
module Lazy = struct

  (* Étape 1 : définir un type ['a t] représentant une suspension qui
     produira une valeur de type ['a] lorsque qu'elle sera évaluée.
     Les suspensions doivent pouvoir être mises à jour pour mémoriser
     les valeurs obtenues lors de leur évaluation.
  *)    
  type 'a t =
      'a (* À remplacer *)

  (* Étape 2 : définir une fonction
       mk_lazy: (unit -> 'a) -> 'a t
     qui place un calcul de type ['a] dans une suspension. *)
  let mk_lazy f =
    failwith "Not implemented"

  (* Étape 3 : définir une fonction d'évaluation d'une suspension.
       force: 'a t -> 'a
     Cette fonction ne doit pas recalculer la valeur d'une suspension déjà
     forcée, et doit mémoriser le résultat d'une suspension forcée pour la
     première fois.
  *)
  let force e =
    failwith "Not implemented"
      
end

(**
   Exercice 3
   ----------
   Modification du module MinMax modulaire pour rendre paresseux le calcul de
   l'arbre des parties possibles.

   Ainsi, ne seront construites de l'arbre complet que les parties requises
   par la suite du calcul. En l'occurrence, il s'agit de l'arbre complet
   jusqu'à une profondeur de [k] coups.
*)
  
module LazyMinMax (G : GameSig) = struct

  (* Étape 1 : écrire une nouvelle définition pour le type ['a tree], 
     telle que chaque nœud soit en réalité une suspension qui ne calculera
     la liste de ses fils qu'une fois forcée. *)
  type 'a tree =
      Node of 'a * ('a tree list) (* À remplacer *)

  (* Étape 2 : modifier les fonctions [mk_tree] et [map_tree] pour les
     adapter aux arbres paresseux. *)
  (* mk_tree: ('a -> 'a list) -> 'a -> 'a tree *)
  let rec mk_tree next p =
    failwith "Not implemented"
      
  (* map_tree: ('a -> 'b) -> 'a tree -> 'b tree *)
  let rec map_tree f t =
    failwith "Not implemented"

  (* Étape 3 : les autres fonctions n'ont pas besoin d'être modifiées,
     vous pouvez les copier ! *)
      
  (* maximize: int tree -> int *)
  (* minimize: int tree -> int *)
  let rec maximize t =
    failwith "Not implemented"
  and minimize t =
    failwith "Not implemented"

  (* prune: int -> 'a tree -> 'a tree *)
  let rec prune k t =
    failwith "Not implemented"

  let evaluate k p =
    mk_tree G.moves p |> prune k |> map_tree G.eval |> maximize
	
end

(**
   Suggestion une fois que vous aurez fini : lisez l'article mentionné
   en introduction, et suivez l'approche proposée pour passer du simple
   algorithme MinMax à l'algorithme amélioré AlphaBeta pour diminuer le
   nombre de positions explorées (et donc le nombre de positions construites
   dans l'arbre paresseux !).
*)
  

