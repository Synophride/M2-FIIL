(* Commande de compilation:
   ocamlopt -o msi -thread unix.cmxa threads.cmxa  msi.ml 
*)

let delay () = Thread.delay (Random.float 0.01)

let ram = ref 0
let nb_procs = 4

module Cache = struct
  type msi = M | S | I
      
  type cache = { mutable status : msi ; mutable value : int }
      
  let caches = 
    Array.init nb_procs (fun _ -> { status = I ; value = 0 }) 
      
  let current = ref None
  let shr = ref []
      
  let m = Mutex.create ()
        
  let read i =
    begin
      match caches.(i).status with
      | M | S -> ()
      | I -> 
	 begin
	   (* Mutex.lock m; *)
	   match !current with
	   | Some j ->
	      caches.(j).status <- S;
	      current := None;	      
	      delay ();
	      ram := caches.(j).value; (* flush *)
	   | None -> ()
	 end;
	 caches.(i).status <- S;
	 caches.(i).value <- !ram;
	 shr := i :: !shr;
	 (* Mutex.unlock m; *)
    end;
    caches.(i).value

 let write i v = 
    begin
      match caches.(i).status with
      | S | I ->
	     (* Mutex.lock m; *)
	     List.iter (fun k -> caches.(k).status <- I) !shr;
	     delay ();
	     current := Some i;
	     shr := [i];
	     caches.(i).status <- M;
	     (* Mutex.unlock m; *)
      | M -> ()
    end;

    caches.(i).value <- v
	
  let check () = 
    Mutex.lock m;
    let v = ref false in
    for i = 0 to nb_procs - 1 do
      if caches.(i).status = M then
	begin
	  if !v then begin
	      Mutex.unlock m;
	      Format.eprintf "Erreur@.";
	      exit 1
	    end;
	  v := true
	end
    done;
    Mutex.unlock m

end

let run i = 
  Format.printf "T%d starts@." i;
  for k = 0 to 10 do
    let v = Cache.read i in
    for j = 0 to 3 do 
      ignore (Cache.read i);
      Cache.write i (v+1);
      Cache.check()
    done;
  done
  
let () = 
  let l = ref [] in
  for i = 0 to nb_procs - 1 do
    l := (Thread.create run i) :: !l;
  done;
  List.iter Thread.join !l
  

