open Other

   
module DifficultTypes = struct
  open SimpleTypes
  type dtyp =
    | Forall of string * dtyp
    | T of typ
         
end

module PTypeChecker = struct
  open DifficultTypes
  open SimpleTypes
  open RawAST

  exception Bad_type of string
  module Env = Map.Make(String)
             
  type evt = DifficultTypes.dtyp Env.t
  type simple_evt = SimpleTypes.typ Env.t

  type contrainte = typ * typ
  module CSet = Set.Make(struct type t = contrainte let compare = compare end)

  let mk_cpt () =
    let x = ref 0 in
    (fun ()
     -> let s = string_of_int !x in
        x := (!x)+1;
        s)
  let rec print_contraintes cset =
    CSet.iter (fun (a,b) -> print_string ((str_of_type a) ^ " ?= " ^ (str_of_type b) ^ "\n____\n" )

  let type_expression (env: simple_evt) (e : expression) : typ =
    (* Transition d'un type simple à un type simple, mais de bon type  *)
    let mk_new_str_vartyp = mk_cpt() in
    let gen_st = (fun st -> T(st)) in
    let env' = Env.map (gen_st) (env) in

    let substitution (str_var_a_remplacer, type_remplacant) t =
      let rec sub t = 
        match t with
        | TVar(s) -> if str_var_a_remplacer = s
                     then type_remplacant
                     else t
        | TFun(t1, t2) -> TFun (sub t1, sub t2)
        | TPair(t1, t2)-> TPair(sub t1, sub t2)
        | TRef(t) -> TRef(sub t)
        | _ -> t 
      in
      sub t
    in
    
    let generalisation styp var =
      Forall(var, T(styp))  in
    let specialisation dtyp =      
      let rec spec (liste_substitutions : (string * typ) list) =
        function
        | Forall(str, t2) -> let v = mk_new_str_vartyp() in 
                             spec ((str, TVar(v))::liste_substitutions) t2
        | T(typ) -> List.fold_left
                      (fun t (subst_elt) -> substitution subst_elt t )
                      typ
                      liste_substitutions
      in
      spec [] dtyp 
    in

    
    (***************************************
     *** I . GenEraTiOn dEs CoNtRaiNTeS ****
     ***************************************)
    let generation_contraintes () : (CSet.t * SimpleTypes.typ) =

      let constraints = ref CSet.empty in
      
      let add c = constraints := CSet.add c (!constraints) in
      let rm c = constraints := CSet.remove c (!constraints) in

      let rec gen (env: evt) (e:expression) : typ =
        match e with
        | Int(i) -> TInt
        | Int(_) -> TInt
        | Bool(_)-> TBool
        | Unit -> TUnit
        | Var(nom_var) -> specialisation (Env.find nom_var env)
        | App(e1, e2)
          -> let type_f = gen env e1 in
             let type_param = gen env e2 in
             let type_retour = TVar(mk_new_str_vartyp()) in
             let _ = add (type_f, TFun(type_param, type_retour)) in
             type_retour
        | Fun(str, e)
          -> let vt = mk_new_str_vartyp() in
             let env' = Env.add str (T(TVar(vt))) env in
             TFun(TVar(vt), gen env' e)
        | Let(s, e, e2)
          -> let vt  = mk_new_str_vartyp() in
             let env'= Env.add s (T(TVar(vt))) env in
             let t_e = gen env' e in
             let gen_e = generalisation t_e vt in
             let evt'' = Env.add s gen_e env in
             gen evt'' e2

        | Op(str) -> failwith "flemme"
        | Pair(e1, e2) -> TPair(gen env e1, gen env e2)
        | Newref(e) -> TRef(gen env e)
        | Sequence(e1, e2)
          -> let t_e1, t_e2 = gen env e1, gen env e2 in
             let _ = add (TUnit, t_e1) in
             t_e2
        | If(condition, e1, e2)
          -> let tb = gen env condition in
             let t1 = gen env e1 in
             let t2 = gen env e2 in
             let _ = add (tb, TBool) in 
             let _ = add (t1, t2) in 
             t1
             
        | While(condition, e)
          -> let tb = gen env condition in
             let texp = gen env e in
             let _ = add (tb, TBool) in
             let _ = add (texp, TUnit) in 
             TUnit
      in
      let t = gen env' e in
      (!constraints, t)
    in
    
    let resolution_contraintes cset t_exp =
      (* type de retour *) 
      let t_retour_gen = ref t_exp in 
      (* liste des contraintes à éliminer *) 
      let constraints = ref cset in

      let cset_map f cset = CSet.fold (fun c cset -> CSet.add (f c) cset) cset CSet.empty in
      let constraints_map f = (constraints := cset_map f (!constraints)) in
      
      let rm ct = constraints := CSet.remove ct (!constraints) in
      let add ct = constraints := CSet.add ct (!constraints) in

      
      let rec contains str_var t =
        match t with
        | TVar(str) -> str = str_var
        | TInt | TBool | TUnit -> false
                                
        | TFun(t1, t2) | TPair(t1, t2) -> (contains str_var t1 || contains str_var t2) 
        | TRef(t) -> contains str_var t
      in
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
    let (cset, t) = generation_contraintes () in
    resolution_contraintes cset t
end
