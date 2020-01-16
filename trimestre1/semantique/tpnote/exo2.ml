open Other

(**
   Moteur d'inférence de types
*)
module BaseTypeReconstruction = struct
  
  open SimpleTypes
  open RawAST
  exception Bad_type of string
  module Env = Map.Make(String)
  type type_env = SimpleTypes.typ Env.t 
  type t_contrainte = SimpleTypes.typ * SimpleTypes.typ (* None = Tout type *) 

  module CSet = Set.Make(struct type t = t_contrainte let compare = compare end)

  (* fonction de création d'un compteur *) 
  let mk_cpt_vt () =
    let x = ref 0 in
    (fun ()
     -> let s = string_of_int !x in
        x := (!x)+1;
        s)

  let str_contrainte (t1, t2) =
    "(" ^ (str_of_type t1) ^ ") ?= (" ^ (str_of_type t2) ^ ")"
    
  let print_ensemble_contraintes =
    CSet.iter (fun c -> print_string ("\n" ^ (str_contrainte c) ^ "\n~~~"))
    
  (** 
      Objectif : compléter la fonction suivante de typage d'une expression.
      Un appel [type_expression e] doit :
      - renvoyer le type de [e] dans l'environnement [env] si [e] est bien typée
      - déclencher une exception sinon
      
      Procéder en deux étapes : génération de contraintes sur les types,
      puis résolution par unification.
   *)

  let type_expression (env: type_env) (e: expression) : SimpleTypes.typ =

    (** I. Génération des contraintes sur l'expression **)
    let generation_contraintes (env:type_env) (e:expression) : (CSet.t * SimpleTypes.typ) =
      (* génération de variables de types uniques *)
      let get_new_vartyp = mk_cpt_vt () in
      (* ensemble des contraintes *) 
      let constraints = ref CSet.empty in 
      (* ajoute une contrainte à l'ensemble *) 
      let add_cst c = constraints := CSet.add c (!constraints) in
      let rec build_cst (evt : type_env) (exp : expression) : SimpleTypes.typ =
        match exp with 
        | Int(i) -> TInt
        | Int(_) -> TInt
        | Bool(_)-> TBool
        | Unit -> TUnit
        | Var(nom_var) ->  (Env.find nom_var evt)

        | App(f_exp, param_exp)
          -> let type_f = build_cst evt f_exp in
             let type_param = build_cst evt param_exp in
             (* ajout d'une variable de type représentant le type de retour *) 
             let type_retour= TVar(get_new_vartyp()) in
             begin
               (* ajout de la contrainte liant le type de l'expression fonctionnelle, celui 
                  du paramètre ainsi que le retour *)
               add_cst (type_f, TFun(type_param, type_retour));
               (* une fois la fonction appliquée, le type de l'expression est celle du type de retour
                  de la fonction *)
               type_retour
             end
        | Fun(nom_var, expr)
(* On définit le type du paramètre *)
          -> let type_param = TVar(get_new_vartyp()) in
             (* environnement visible depuis l'intérieur de la fonction : 
                ajout de la variable de type du paramètre *)
             let env' = Env.add nom_var type_param evt in
             let type_retour = build_cst env' expr in
             TFun(type_param, type_retour)
        | Let(s, e1, e2)
          -> let type_s = build_cst env e1 in
             let evt' = Env.add (s) (type_s) evt in
             build_cst evt' e2
        | Op(str)
          -> failwith "flemme"
        | Pair(e1, e2) -> TPair(build_cst evt e1, build_cst evt e2)
        | Newref(e) -> TRef(build_cst evt e)
        | Sequence(e1, e2)
          -> let t1, t2 = build_cst evt e1, build_cst evt e2 in
             begin
               (* On part du principe que e1 doit être un effet de bord *)
               add_cst (t1, TUnit);
               t2
             end
        | If(condition, e1, e2)
          -> let tb = build_cst evt condition in
             let t1 = build_cst evt e1 in
             let t2 = build_cst evt e2 in
             begin
               add_cst (tb, TBool);
               add_cst (t1, t2);
               t1
             end
        | While(condition, e)
          -> let tb = build_cst evt condition in
             let texp = build_cst evt e in
             begin
               add_cst (tb, TBool);
               (* on part du principe que e est un effet de bord et ne doit pas renvoyer de valeur *)
               add_cst (texp, TUnit);
               TUnit
             end
        | _ -> failwith "" 
      in
      let t = build_cst env e in
      (!constraints, t)
    in
    
    (** II. Résolution des contraintes *) 

    let resolution_contraintes cset t_exp =
      (* type de retour *) 
      let t_retour_gen = ref t_exp in 
      (* liste des contraintes à éliminer *) 
      let constraints = ref cset in

      let cset_map f cset = CSet.fold (fun c cset -> CSet.add (f c) cset) cset CSet.empty in
      let constraints_map f = (constraints := cset_map f (!constraints)) in 
      (** suppression et ajout de contraintes dans l'ensemble des contraintes **)
      let rm ct = constraints := CSet.remove ct (!constraints) in
      let add ct = constraints := CSet.add ct (!constraints) in

      (** fonction déterminant si une variable de type est contenue dans le type t **)
      let rec contains str_var t =
        match t with
        | TVar(str) -> str = str_var
        | TInt | TBool | TUnit -> false
                                
        | TFun(t1, t2) | TPair(t1, t2) -> (contains str_var t1 || contains str_var t2) 
        | TRef(t) -> contains str_var t
      in
      (* t[TypVar(str_var) <- t_remp]. str_var pas contenu dans t_remp *)
      let substitution str_var t_remp t =
        let rec subst t = 
          match t with
          | TBool | TInt | TUnit -> t
          | TVar(s) -> if(s = str_var) then t_remp else t
          | TFun(t1, t2) -> TFun(subst t1, subst t2)
          | TPair(t1, t2) -> TPair(subst t1, subst t2)
          | TRef(t') -> TRef(subst t')
        in
        subst t
      in

      
      (** Fonction de résolution des contraintes **)
      let rec res () =
          match CSet.min_elt_opt (!constraints) with
          | None -> ()
          | Some(cst)
            -> let _ = rm cst in
               let a, b = cst in
               let _ = if a = b
                       then ()
                       else
                         match cst with
                         | TVar(s), other 
                           -> if other = TVar(s) (* tautologie, rien à faire *)
                              then ()
                              (* Cas où un type est contenu dans lui même : insulter l'utilisateur *)
                              else if contains s other 
                              then
                                raise (Bad_type("You have two parts of brain, 'left' and 'right'." ^
                                                  " In the left side, there's nothing right."^
                                                    " In the right side, there's nothing left."))
                              else (* On peut faire la substitution dans ce cas *)
                                let _ = constraints_map
                                          (fun (a,b) -> (substitution s other a,
                                                         substitution s other b)) in
                                (*     let _ = print_string "substitution de type \n" in
                                let _ = print_string ((str_of_type (!t_retour_gen))^"\n") in
                                let _ = print_string ("remplacement de " ^ s ^ " par " ^
                                                        (str_of_type other) ^ " dans " ^
                                                          (str_of_type (!t_retour_gen))  ^
                                                            "\n") in *)
                                let _ =  t_retour_gen := substitution s other (!t_retour_gen)
                                in ()

                         | TPair(t1a, t1b), TPair(t2a, t2b) | TFun(t1a, t1b), TFun(t2a, t2b) 
                           -> let _ = add (t1a, t2a) in 
                              let _ = add (t1b, t2b) in ()
                         | TRef(t1), TRef(t2)
                           -> let _ = add (t1,t2) in ()
                         | other, TVar(s) -> let _ = add (TVar(s), other) in ()
                         | _ -> raise (Bad_type("Your birth certificate is an"^
                                                  " apology letter from the condom factory."))
               in res ()
      in
      let _ = res () in
      (!t_retour_gen)
    in
    let (contraintes, t) = generation_contraintes env e in
    (*let _ = print_string "CONTRAINTES:\n\n\n" in
    let _ = print_ensemble_contraintes contraintes in
    let _ = print_string "FIN DES CONTRAINTES\n" in*)
    let  type_retour = resolution_contraintes contraintes t in
    type_retour

end

