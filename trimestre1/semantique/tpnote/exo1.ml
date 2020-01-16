open Other

(**
   Vérificateur de types.
 **)
module BaseTypeChecker = struct
  open SimpleTypes
  open BaseAST
  exception Bad_type of string 
  module Env = Map.Make(String)
  type type_env = SimpleTypes.typ Env.t

                              

  let rec type_expression (env: type_env) (e: expression) : typ =
    match e with
    | Int(_) -> TInt
    | Bool(_) -> TBool
    | Unit -> TUnit
    | Var(str) -> Env.find str env
    | App(e1, e2) -> let t1, t2 = type_expression env e1, type_expression env e2 in
                     begin
                       match t1 with
                       | TFun(tparam, tret) -> if tparam = t2
                                               then tret
                                               else raise (Bad_type("Mauvais type de param"))
                       | _ -> raise (Bad_type("Application d'une valeur non fonctionnelle"))
                     end
    | Fun(nom_var, type_var, term) -> let nouvel_evt = Env.add (nom_var) (type_var) (env) in
                                      let nouveau_type = type_expression nouvel_evt term in
                                      TFun(type_var, nouveau_type)
    | Let(nom_var, e1, e2) -> let type_var = type_expression env e1 in
                              type_expression (Env.add nom_var type_var env) e2
    | Op(str) ->
       begin
         match str with
         | _ -> failwith "flemme" 
       end
    | Pair(e1, e2) -> TPair(type_expression env e1, type_expression env e2)
    | NewRef(e) -> TRef(type_expression env e)
    | Sequence(e1, e2) -> let type_e1 = type_expression env e1 in (* type_e1 ?= () *) 
                          type_expression env e2
    | If(cond, e1, e2) -> let f = type_expression env in
                          if f cond <> TBool
                          then raise (Bad_type("condition non booléenne"))
                          else let t1, t2 = f e1, f e2 in
                               if t1 = t2
                               then t1
                               else raise (Bad_type("deux branches du if pas de meme type"))
    | While(c, e) -> let type_c = type_expression env c in
                     if type_c = TBool
                     then let _ = type_expression env e in
                          (* Type_e1 ?= () *)
                          TUnit
                     else raise (Bad_type("Condition non bool dans le while"))
end



